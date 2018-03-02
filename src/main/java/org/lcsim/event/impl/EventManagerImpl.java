package org.lcsim.event.impl;

import hep.lcio.event.LCEvent;
import hep.lcio.event.LCRunHeader;
import hep.lcio.implementation.event.ILCRunHeader;
import hep.lcio.implementation.io.LCFactory;
import hep.lcio.io.LCReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.lcsim.event.api.EventManager;
import org.lcsim.event.api.EventProcessor;
import org.lcsim.service.api.DetectorService;
import org.lcsim.service.api.ServiceLocator;
import org.lcsim.service.impl.ServiceLocatorFactory;

/**
 * 
 * Implementation of {@link org.lcsim.EventManager}.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class EventManagerImpl implements EventManager {

	List<File> eventSources = new ArrayList<File>();
	List<EventProcessor> processors = new ArrayList<EventProcessor>();
	LCReader reader = LCFactory.getInstance().createLCReader();
	LCRunHeader currentRunHeader = null; 
	LCEvent currentLCEvent = null;
	int numberOfEventsProcessed = 0;
	int totalNumberOfEventsProcessed = 0;
	Iterator<File> sourceIterator; 
	private static Logger logger = null;	
	boolean wasInitialized = false;
	ServiceLocator serviceLocator;
		
	public EventManagerImpl() {
		
		// Setup logger with default level.
		setupLogger(Level.INFO);
		
		// Initialize service lookup subsystem. 
		serviceLocator = ServiceLocatorFactory.createServiceLocator();
	}	
		
	public static void setupLogger(Level newLevel) {
		if (logger == null) {
			logger = Logger.getLogger(EventManager.class.getName());
			StreamHandler handler = new StreamHandler(System.out, new SimpleFormatter());
			logger.addHandler(handler);
			logger.setUseParentHandlers(false);
		}
		logger.setLevel(newLevel);
		logger.getHandlers()[0].setLevel(newLevel);		
	}
	
	public static void flushLogger() {
		logger.getHandlers()[0].flush();
	}
	
	public void initializeProcessors() {
		for (EventProcessor processor : processors) {
			processor.initialize();
		}
		wasInitialized = true;
	}
					
	public void processEvents(int maxEvents) {
		if (!wasInitialized) {
			initializeProcessors();
		}
		if (sourceIterator == null)
			sourceIterator = eventSources.iterator();
		numberOfEventsProcessed = 0;
		while (sourceIterator.hasNext()) {
			File file = sourceIterator.next();
			try {
				//logger.finest("Reading next file: " + file.getCanonicalPath());
				reader.open(file.getCanonicalPath());
			} catch (IOException e) {
				throw new RuntimeException("Error opening file.", e);
			}			
			try {
				// Try to read the RunHeader from the front of this file.
				//logger.finest("Reading next RunHeader ...");
				LCRunHeader nextRunHeader = reader.readNextRunHeader(); 
				if (nextRunHeader == null) {
					//logger.finest("WARNING: There was no RunHeader!");
					//logger.finest("Injecting dummy RunHeader ...");
					ILCRunHeader dummyRunHeader = new ILCRunHeader();
					dummyRunHeader.setDescription("Dummy RunHeader.");
					dummyRunHeader.setRunNumber(0);
					//dummyRunHeader.setDetectorName("NONE");
					currentRunHeader = dummyRunHeader;					
				} else {
					currentRunHeader = nextRunHeader;
				}
				flushLogger();
			} catch (IOException e) {
				throw new RuntimeException("Error reading next LCRunHeader.", e);
			}
			try {
				// Get the first event from the file.
				//logger.finest("Reading next Event ...");
				currentLCEvent = reader.readNextEvent();
				//logger.finest("Read next Event.");
			} catch (IOException e) {
				throw new RuntimeException("Error reading first LCEvent", e);
			}

			// Setup the current detector from the event header.
			setupDetector(currentLCEvent, currentRunHeader);

			// Print run header info.
			logger.info("Run #" + currentRunHeader.getRunNumber() 
					+ "; detector = " + currentRunHeader.getDetectorName() 
					+ "; description = \"" + currentRunHeader.getDescription() + "\"");
			
			// Call being of run now that detector is setup properly.
			beginRun();
			
			// Process events in the file.
			while (currentLCEvent != null) {
				//logger.finest("Processing Event ...");
				process(currentLCEvent);
				if (maxEvents != -1 && numberOfEventsProcessed == maxEvents) {
					logger.info("Maximum number of events was reached: " + maxEvents);
					return;
				}
				//logger.finest("Done processing Event.");
				try {
					// Read the next event.
					//logger.finest("Reading next Event in loop ...");
					long start = System.currentTimeMillis();
					currentLCEvent = reader.readNextEvent();
					long end = System.currentTimeMillis();					
					//logger.finest("Done reading next Event in loop.");
					logger.info("Read Event in " + (end - start) + " milliseconds.");
				} catch (IOException e) {
					throw new RuntimeException("Error reading LCEvent.", e);
				}
				flushLogger();
			}
			// Assume 1 file is a single run.  Call endRun on processors.
			endRun();
		}		
		sourceIterator = null;
		logger.info("Processed " + getNumberOfEventsProcessed() + " events in this job.");
	}
	
	private void process(LCEvent event) {
		for (EventProcessor processor : processors) {
			logger.finest("Calling Processor " + processor.getInstanceName() + " ...");
			processor.process(event);
			logger.finest("Done calling Processor " + processor.getInstanceName() + ".");					
		}		
		++numberOfEventsProcessed;
		++totalNumberOfEventsProcessed;
	}
	
	private void beginRun() {
		for (EventProcessor processor : processors) {
			processor.beginRun(currentRunHeader);
		}
	}
	
	private void endRun() {
		for (EventProcessor processor : processors) {
			processor.endRun(currentRunHeader);
		}
	}

	public void addEventSource(File file) {
		eventSources.add(file);
	}

	public List<File> getEventSources() {
		return eventSources;
	}

	public LCRunHeader getCurrentRunHeader() {
		return currentRunHeader;
	}

	public int getNumberOfEventsProcessed() {	
		return numberOfEventsProcessed;
	}
	
	public int getTotalNumberOfEventsProcessed() {
		return totalNumberOfEventsProcessed;
	}

	public void addProcessor(EventProcessor processor) {	
		processors.add(processor);	
		
		// For processors extending the default implementation, set the 
		// reference to the shared ServiceLocator. 
		if (processor instanceof EventProcessorImpl) {
			((EventProcessorImpl)processor).setServiceLocator(serviceLocator);
		}
	}

	public void removeProcessor(EventProcessor processor) {
		processors.remove(processor);		
	}

	public List<EventProcessor> getProcessors() {
		return processors;
	}
	
	private void setupDetector(LCEvent event, LCRunHeader runHeader) {
		String detectorName = event.getDetectorName();
		DetectorService detectorService = serviceLocator.findService(DetectorService.class);
		detectorService.loadDetector(detectorName);
		logger.config("loaded detector: " + detectorName);
		if (runHeader.getDetectorName() == null || runHeader.getDetectorName() == "") {
			((ILCRunHeader)runHeader).setDetectorName(detectorName);
		}
	}
}