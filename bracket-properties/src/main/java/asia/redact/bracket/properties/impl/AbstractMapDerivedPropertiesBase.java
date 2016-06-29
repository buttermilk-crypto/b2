/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.values.ValueModel;

/**
 * Base class for Map-derived Implementations. In this class we implement the common map-like facade methods.
 * 
 * @author Dave
 *
 */
public abstract class AbstractMapDerivedPropertiesBase implements Serializable {

	private static final long serialVersionUID = 1L;

	// the data
	protected Map<String, ValueModel> map;
	
	// if set we will initialize a Concurrent map wrapper
	protected boolean concurrent;

	public AbstractMapDerivedPropertiesBase(boolean concurrent) {
		this.concurrent = concurrent;
	}
	
	/**
	 * Initialize the map
	 */
	public abstract Properties init();

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public boolean containsValue(String value) {
		return map.containsValue(value);
	}

	public Object remove(String key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Collection<ValueModel> values() {
		return map.values();
	}

	public String toString() {
		return map.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractMapDerivedPropertiesBase other = (AbstractMapDerivedPropertiesBase) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}

}
