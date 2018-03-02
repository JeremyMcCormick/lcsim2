package org.lcsim.service.impl;

import org.lcsim.service.api.ServiceLocator;

/**
 * This is a factory for the ServiceLocator, ensuring that there is one globally accessible
 * locator for the entire process or application.  Threads will share the same locator, which
 * is by design.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public final class ServiceLocatorFactory {
	
	private ServiceLocatorFactory() {
	}
	
	private static ServiceLocator locator = null;
	public static ServiceLocator createServiceLocator() {
		if (locator == null) {
			locator = new ServiceLocatorImpl();
		}
		return locator;
	}	
}
