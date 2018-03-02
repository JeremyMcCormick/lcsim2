package org.lcsim.event.impl;

import hep.lcio.event.LCEvent;
import hep.lcio.event.LCRunHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.lcsim.event.api.EventProcessor;
import org.lcsim.parameters.api.Parameters;
import org.lcsim.parameters.impl.ParametersImpl;
import org.lcsim.service.api.LogService;
import org.lcsim.service.api.ServiceLocator;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class EventProcessorImpl implements EventProcessor {
	
	String instanceName;
	List<EventProcessor> children = new ArrayList<EventProcessor>();
	int numberOfEventsProcessed;
	int numberOfRunsProcessed;
	protected Logger logger;
	Parameters parameters = new ParametersImpl();
	ServiceLocator serviceLocator;
	
	public EventProcessorImpl(String instanceName) {
		this.instanceName = instanceName;
	}	
			
	public String getTypeName() {
		return getClass().getCanonicalName();
	}
	
	public String getInstanceName() {
		return instanceName;
	}
	
	public void initialize() {
		for (EventProcessor processor : children) {
			processor.initialize();
		}
	}
	
	public void process(LCEvent event) {
		for (EventProcessor processor : children) {
			processor.process(event);
		}
	}
		
	public void beginRun(LCRunHeader run) {
	}
	
	public void endRun(LCRunHeader run) {		
	}
				
	public void add(EventProcessor processor) {
		children.add(processor);
	}
	
	public void remove(EventProcessor processor) {
		children.remove(processor);
	}
	
	public void remove(String instanceName) {
		List<EventProcessor> remove = new ArrayList<EventProcessor>();
		for (EventProcessor processor : children) {
			if (processor.getInstanceName().equals(instanceName)) {
				remove.add(processor);
			}
		}
		children.removeAll(remove);
	}
	
	public boolean contains(EventProcessor processor) {
		return children.contains(processor);
	}
	
	public boolean contains(String instanceName) {
		for (EventProcessor processor : children) {
			if (processor.getInstanceName().equals(instanceName)) {
				return true;
			}
		}
		return false;
	}
	
	public Parameters parameters() {
		return this.parameters;
	}
	
	public ServiceLocator serviceLocator() {
		return serviceLocator;
	}	
	
	void setServiceLocator(ServiceLocator serviceLocator) {		
		this.serviceLocator = serviceLocator;
	}
	
	public Logger logger() {
		if (logger == null) {
			LogService logService = serviceLocator().findService(LogService.class);
			logger = logService.getLogger(this);
		}
		return logger;
	}
}