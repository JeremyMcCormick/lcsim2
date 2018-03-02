package org.lcsim.parameters.api;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface Parameter<T> {
	String name();
	T value();
	void setValue(T object);
	T defaultValue();
	boolean isDefaultValue();
	Class<T> getType();
}
