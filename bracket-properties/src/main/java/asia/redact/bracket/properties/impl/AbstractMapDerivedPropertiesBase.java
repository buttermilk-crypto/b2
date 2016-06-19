/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.impl;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Set;

import asia.redact.bracket.properties.values.ValueModel;

/**
 * Base class for Map-derived Implementations. In this class we implement the common map-like facade methods.
 * 
 * @author Dave
 *
 */
public abstract class AbstractMapDerivedPropertiesBase extends PropertiesBaseImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	// the data
	protected AbstractMap<String, ValueModel> map;

	public AbstractMapDerivedPropertiesBase() {
		init();
	}
	
	/**
	 * Initialize the map
	 */
	abstract void init();

	public int size() {
		lock.lock();
		try {
			return map.size();
		} finally {
			lock.unlock();
		}
	}

	public boolean isEmpty() {
		lock.lock();
		try {
			return map.isEmpty();
		} finally {
			lock.unlock();
		}
	}

	public boolean containsKey(String key) {
		lock.lock();
		try {
			return map.containsKey(key);
		} finally {
			lock.unlock();
		}
	}

	public boolean containsValue(String value) {
		lock.lock();
		try {
			return map.containsValue(value);
		} finally {
			lock.unlock();
		}
	}

	public Object remove(String key) {
		lock.lock();
		try {
			return map.remove(key);
		} finally {
			lock.unlock();
		}
	}

	public void clear() {
		lock.lock();
		try {
			map.clear();
		} finally {
			lock.unlock();
		}
	}

	public Set<String> keySet() {
		lock.lock();
		try {
			return map.keySet();
		} finally {
			lock.unlock();
		}
	}

	public Collection<ValueModel> values() {
		lock.lock();
		try {
			return map.values();
		} finally {
			lock.unlock();
		}
	}

	public String toString() {
		lock.lock();
		try {
			return map.toString();
		} finally {
			lock.unlock();
		}
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
