/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

import asia.redact.bracket.properties.Properties;

public interface Alias {
	
	static final String REF_TOKEN = "_$";
	
	public String getKeyRef(String key);
	
	public void putKeyRef(String newRefKey, String existingKey);
	
	/**
	 * Return a class which implements the Alias Interface
	 * 
	 * @param props
	 * @return
	 */
	public static Alias instance(Properties props) {
		return new AliasAdapter(props);
	}

}
