/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

import asia.redact.bracket.properties.Properties;

/**
 * Value obfuscation or encryption support. Care must be taken by the client to not
 * obfuscate or encrypt values more than once (or keep track of the count if you do).  
 * 
 * @author dave
 *
 */
public interface Sec {

	// simple obfuscation
	public void obfuscate(String key);
	public void deobfuscate(String key);
	public char[] deobfuscateToChar(String key);
	
	// encryption - for this to work you must set a password using the correct constructor. 
	// Client must retain the password value, there is no recovery option
	public void encrypt(String key);
	public void decrypt(String key);
	public char[] decryptToChar(String key);
	
	/**
	 * Use this for obfuscation
	 * 
	 * @param props
	 * @return
	 */
	public static Sec instance(Properties props) {
		return new SecAdapter(props);
	}
	
	/**
	 * Use this for password-based encryption
	 * 
	 * @param props
	 * @param password
	 * @return
	 */
	public static Sec instance(Properties props, char [] password) {
		return new SecAdapter(props, password);
	}
	
	/**
	 * Clear the password from heap memory after use. Client to call if desired
	 */
	public void clearPassword();
	
}
