/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

import java.util.regex.Pattern;

/**
 * Addon for simple variable substitution of the form ${var} as commonly found in ant
 * 
 * @author Dave
 *
 */
public interface Env {

	static Pattern antStyleVarPattern = Pattern.compile("\\$\\{(.+)\\}");
	
	public String resolve(String key);
	
}
