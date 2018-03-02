package org.lcsim.parameters.impl;

import org.lcsim.parameters.api.Parameter;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class ParameterImpl<T> implements Parameter<T> {

	String name;
	T value;
	T defaultValue;
	Class<T> type;
	boolean required = false;
		
	public ParameterImpl(String name, T defaultValue, T value, boolean required) {
		this.type = (Class<T>)value.getClass();
		this.name = name;
		this.defaultValue = defaultValue;
		this.value = value;
		this.required = required;
	}
	
	public ParameterImpl(String name, T value) {
		this.type = (Class<T>)value.getClass();
		this.name = name;
		this.value = value;
	}
	
	public ParameterImpl(String name) {
		this.type = (Class<T>)value.getClass();
		this.name = name;
	}
		
	public String name() {
		return name;
	}

	public T value() {
		return value;
	}

	public void setValue(T object) {
		value = object;	
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public T defaultValue() {
		return defaultValue;
	}

	public boolean isDefaultValue() {
		return value.equals(defaultValue);
	}

	public Class<T> getType() {
		return type;
	}	
}
