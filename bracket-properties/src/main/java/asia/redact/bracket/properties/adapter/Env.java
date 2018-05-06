/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

import java.util.regex.Pattern;

import asia.redact.bracket.properties.Properties;

/**
 * <p>Adapter for simple variable substitution of the form ${var} as commonly found in ant. The 
 * substitutions include both System.getenv() and System.getProperties()</p>
 * 
 * <p>For example: </p>
 * 
 * <pre>
 * 
 *  props.put("user.dir", "${user.dir}"
 *  System.out.println(props.get("user.dir"));
 *  
 *  // outputs C:/Users/dave
 * 
 * </pre>
 * 
 * @author Dave
 *
 */
public interface Env {

	static Pattern antStyleVarPattern = Pattern.compile("\\$\\{(.+)\\}");
	
	public String resolve(String key);
	
	public static Env instance(Properties props) {
		return new EnvAdapter(props);
	}
	
}
