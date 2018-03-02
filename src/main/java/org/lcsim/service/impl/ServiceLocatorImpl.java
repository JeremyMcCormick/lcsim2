package org.lcsim.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.spi.ServiceRegistry;

import org.lcsim.service.api.Service;
import org.lcsim.service.api.ServiceLocator;

/**
 * This class implements lcsim Service lookup.  It loads concrete classes that extend {@link org.lcsim.service.api.Service}
 * from the file <code>src/main/resources/META-INF/services/org.lcsim.service.api.Service</code>, according to the 
 * conventions of the {@link javax.imageio.spi.ServiceRegistry} class.  Users should actually pass the interface
 * they want to find, NOT the implementation class.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
class ServiceLocatorImpl implements ServiceLocator {
	
	private List<Service> services;
	
	public ServiceLocatorImpl() {
		loadServices();
	}
	
	/**
	 * Find a service by the interface it implements.
	 * 
	 * For instance, to get the <code>LogService</code> use the argument <code>LogService.class</code>, 
	 * which will return an instance of the underlying implementation class <code>LogServiceImpl</code>.
	 * 
	 * @param klass The Class of the </code>Service</code>.
	 * @return The service with Class <code>klass</code> or null if it does not exist.
	 */
	@SuppressWarnings("unchecked")
	public final <T> T findService(Class<? extends Service> klass) {
		if (services == null)
			loadServices();
		for (Service service : services) {
			if (klass.isAssignableFrom(service.getClass())) {
				return (T)service;
			}			
		}
		return null;
	}	
	
	/**
	 * Utility method to load all classes from the service registry that implement 
	 * {@link org.lcsim.service.api.Service}.  This method will actually cache an instance 
	 * of each available <code>Service</code> in the <code>services</code> list.
	 */
	private void loadServices() {
		Iterator<Service> iter = getServices(Service.class);
		services = new ArrayList<Service>();
		while (iter.hasNext()) services.add(iter.next());
	}
	
	/**
	 * Utility method to get services by their class from the registry.  
	 * This is used by {@link #loadServices()} to load lcsim services.
	 * @param providerClass
	 * @return The classes that implement the Class <code>providerClass</code>.
	 */
	private static <T> Iterator<T> getServices(Class<T> providerClass) {
		return ServiceRegistry.lookupProviders(providerClass, ServiceLocatorImpl.class.getClassLoader());
	}				
}
