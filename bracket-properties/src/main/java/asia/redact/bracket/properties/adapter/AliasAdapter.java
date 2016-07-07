/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

import asia.redact.bracket.properties.Properties;

/**
 * Implement a reference scheme (aliasing) for keys within a properties file set.
 * This allows the same value to have more than one key. 
 * 
 * @author Dave
 *
 */
public class AliasAdapter implements Alias {

	Properties props;
	
	public AliasAdapter(Properties props) {
		this.props = props;
	}
	
	public String getKeyRef(String key){
		String v = props.get(key);
		if(v.startsWith(REF_TOKEN)){
			return getKeyRef(v.substring(2));
		}else return v;
	}
	
	public void putKeyRef(String newRefKey, String existingKey){
		if(!props.containsKey(existingKey)) {
			throw new RuntimeException("Should be existing key: "+existingKey);
		}
		props.put(newRefKey, REF_TOKEN+existingKey);
	}

}
