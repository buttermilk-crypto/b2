/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

public interface Sec {

	// obfuscation
	public void obfuscate(String key);
	public void deobfuscate(String key);
	public char[] deobfuscateToChar(String key);
	
}
