package org.lcsim.service.api;

/**
 * Locate a Service by class (actually the interface it implements).   
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface ServiceLocator {	
	/**
	 * Get the Service that implements the interface <code>klass</code>.
	 * @param klass The Service to find.
	 * @return The Service or null if a service implementing that interface doesn't exist.
	 */
	<T> T findService(Class<? extends Service> klass);
}