package org.lcsim.service.api;

/**
 * A named service which provides some functionality for the lcsim framework, which is entirely
 * left up to sub-classes to implement.  
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface Service {
	/**
	 * The name of the Service.  This will generally be equal to the class's simple name.
	 * @return The name of the service.
	 */
	String name();
		
	//<T> T resource(String resourceName);
}