package org.lcsim.event;

import hep.lcio.event.LCCollection;
import hep.lcio.event.LCEvent;
import hep.lcio.event.LCRunHeader;

import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.lcsim.event.api.EventManager;
import org.lcsim.event.impl.EventManagerImpl;
import org.lcsim.event.impl.EventProcessorImpl;
import org.lcsim.geometry.Detector;
import org.lcsim.parameters.api.Parameter;
import org.lcsim.parameters.api.Parameters;
import org.lcsim.parameters.impl.ParameterImpl;
import org.lcsim.service.api.LogService;

/**
 * Test of {@link org.lcsim.event.api.EventManager}.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class EventManagerTest extends TestCase {
		
	String testFiles[] = new String[] {
        "/nfs/slac/g/lcd/mc/prj/data/ilc_data4/DBD/grid/ilc/user/j/jstrube/SID_DBD/ILC1000/tth-6q-hnonbb/slcio/sidloi3/tth-6q-hnonbb_+80e-_-20e+_093_sidloi3_0_digi_rec.slcio",
		"/nfs/slac/g/lcd/mc/prj/data/ilc_data4/DBD/grid/ilc/user/j/j strube/SID_DBD/ILC1000/tth-6q-hnonbb/slcio/sidloi3/tth-6q-hnonbb_+80e-_-20e+_094_sidloi3_0_digi_rec.slcio"
	};
	
	public void testEventManager() {
		EventManager mgr = new EventManagerImpl();
		mgr.addEventSource(new File(testFiles[0]));
		mgr.addEventSource(new File(testFiles[1]));
		mgr.addProcessor(new DummyProcessor("MyDummyProcessor"));
		mgr.processEvents(2);
		//mgr.processEvents(500);
		System.out.println("processed " + mgr.getNumberOfEventsProcessed() + " events");
	}	
	
	/**
	 * A dummy processor that prints from its hook methods (beginRun, etc.).  
	 * It will also print out collection names, types, and number of elements from its process method.
	 */
	public class DummyProcessor extends EventProcessorImpl {
		
		Detector detector;		
		Logger eventPrinter;
		
		public DummyProcessor(String name) {
			super(name);					
		}
					
		public void initialize() {
			
			// Configure this processor's logger to print header and message on one line.
			LogService logService = serviceLocator().findService(LogService.class);
			logService.configLogger(logger(), new ConsoleHandler(), new LogService.SingleLine(), Level.FINEST);

			// Print initialization message.
			logger().info(DummyProcessor.class.getSimpleName() + ".initialize");
									
			// Setup a logger for printing the event info with no header.
			eventPrinter = logService.getLogger("EventPrinter");
			logService.configLogger(eventPrinter, new ConsoleHandler(), new LogService.MessageOnly(), Level.FINEST);
			
			Parameters params = parameters();
			Parameter<String> specialCollection = new ParameterImpl<String>("SpecialCollection");
			specialCollection.setValue("MCParticles");
		}
		
		public void beginRun(LCRunHeader run) {
			logger().info(DummyProcessor.class.getSimpleName() + 
					".beginRun - #" + run.getRunNumber() + 
					"; detector: " + run.getDetectorName());
		}
		
		public void endRun(LCRunHeader run) {
			logger().info(DummyProcessor.class.getSimpleName() + ".endRun - #" + run.getRunNumber());
		}
						
		public void process(LCEvent event) {				
			logger().info("Event #" + event.getEventNumber() + " in processor " + this.getInstanceName() + ".");			
			for (String collectionName : event.getCollectionNames()) {
				LCCollection collection = event.getCollection(collectionName);
				eventPrinter.info(String.format("%-24s    %-21s    %-6s", collectionName, collection.getTypeName(), String.valueOf(collection.getNumberOfElements())));
			}
			logger().info("total system memory: " + (Runtime.getRuntime().totalMemory() / 1024) + " kB");
		}
	}
}