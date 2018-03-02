package org.lcsim.parameters.impl;

import java.util.HashMap;
import java.util.Map;

import org.lcsim.parameters.api.Parameter;
import org.lcsim.parameters.api.Parameters;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class ParametersImpl implements Parameters {

	@SuppressWarnings("rawtypes")
	Map<String, Parameter> propertyMap = new HashMap<String, Parameter>();
	
	@SuppressWarnings("unchecked")
	public <T> Parameter<T> get(String name) {
		return propertyMap.get(name);
	}

	public <T> void add(Parameter<T> property) {
		if (exists(property.name())) {
			throw new IllegalArgumentException("Property already exists with name: " + property.name());
		}
		propertyMap.put(property.name(), property);
	}

	public boolean exists(String name) {
		return propertyMap.get(name) != null;
	}

	public void remove(String name) {
		propertyMap.remove(name);		
	}
}
