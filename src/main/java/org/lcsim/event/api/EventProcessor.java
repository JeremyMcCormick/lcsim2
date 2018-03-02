package org.lcsim.event.api;

import java.util.logging.Logger;

import hep.lcio.event.LCEvent;
import hep.lcio.event.LCRunHeader;

import org.lcsim.parameters.api.Parameters;
import org.lcsim.service.api.ServiceLocator;

/**
 * Interface for an event processor that performs some action or algorithm on an LCEvent.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface EventProcessor {

	/**
	 * This is the canonical class name of the processor.
	 * @return The canonical class name of the processor.
	 */
	String getTypeName();
	
	/**
	 * The name of the instance of this processor.
	 * @return The processor's instance name.
	 */
	String getInstanceName();
		
	/**
	 * Initialize this processor.
	 */
	void initialize();
	
	/**
	 * Process a single event.
	 * @param event
	 */
	void process(LCEvent event);
	
	/**
	 * Perform action at beginning of run.
	 * @param run
	 */
	void beginRun(LCRunHeader run);
	
	/**
	 * Perform action at end of run.
	 * @param run
	 */
	void endRun(LCRunHeader run);
					
	/**
	 * Add a child processor.
	 * @param processor
	 */
	void add(EventProcessor processor);
	
	/**
	 * Remove a child processor object.
	 * @param processor
	 */
	void remove(EventProcessor processor);
	
	/**
	 * Remove a child processor by name.
	 * @param instanceName
	 */
	void remove(String instanceName);
	
	/**
	 * Query whether this processor has a specific child object.
	 * @param processor
	 * @return True if has child processor; false if not.
	 */
	boolean contains(EventProcessor processor);
	
	/**
	 * Query whether this processor has a named child processor.
	 * @param instanceName
	 * @return True if has named child processor; false if not.
	 */
	boolean contains(String instanceName);
	
	/**
	 * Get the parameters for this processor.
	 * @return The parameters for this processor.
	 */
	Parameters parameters();
	
	/**
	 * Get the services locator for the processor.
	 * @return
	 */
	ServiceLocator serviceLocator();
	
	/**
	 * Get the logger for this processor.
	 * @return
	 */
	public Logger logger();
}