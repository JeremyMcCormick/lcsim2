package org.lcsim.parameters.api;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface Parameters {
	<T> Parameter<T> get(String name);
	<T> void add(Parameter<T> property);
	boolean exists(String name);
	void remove(String name);
}
