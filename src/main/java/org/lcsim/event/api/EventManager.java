package org.lcsim.event.api;

import hep.lcio.event.LCRunHeader;

import java.io.File;
import java.util.List;

/**
 * This class manages basic event processing.  It has a list of LCIO files to
 * be processed in order.  The EventProcessors registered with this class
 * will be called to process each of these events.
 * 
 * see for instance
 * 
 * https://svnweb.cern.ch/trac/gaudi/browser/Gaudi/trunk/GaudiCoreSvc/src/ApplicationMgr/EventLoopMgr.h
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface EventManager {
		
	/**
	 * Process <code>numberOfEvents</code> events.  Use -1 for all events.
	 * @param maxEvents The maximum number of events to process. 
	 */
	void processEvents(int maxEvents);
			
	/**
	 * Add an event source.
	 * @param file The file containing LCIO events.
	 */
	void addEventSource(File file);
	
	/**
	 * Get the list of LCIO event sources.
	 * @return The list of LCIO event sources.
	 */
	List<File> getEventSources();
		
	/**
	 * Get the current run header.
	 * @return The current <code>LCRunHeader</code> or null if doesn't exist.
	 */
	LCRunHeader getCurrentRunHeader();
	
	/**
	 * Get number of events processed in current job or last job if not currently processing.
	 * @return The number of events processed.
	 */
	int getNumberOfEventsProcessed();
	
	/**
	 * Get total number of events processed since manager instantiation.
	 * @return The total number of events processed.
	 */
	int getTotalNumberOfEventsProcessed();
	
	/**
	 * Add an event processor to the end of the processors list.
	 * @param processor The EventProcessor to be added.
	 */
	void addProcessor(EventProcessor processor);
	
	/**
	 * Remove an event processor.
	 * @param proc The EventProcessor to be removed.
	 */
	void removeProcessor(EventProcessor processor);
	
	/**
	 * Get a list of event processors.  This returns a reference to the
	 * manager's internal list, so processors added to it will be used 
	 * in the job.
	 * @return The list of event processors.
	 */
	List<EventProcessor> getProcessors();		
}
