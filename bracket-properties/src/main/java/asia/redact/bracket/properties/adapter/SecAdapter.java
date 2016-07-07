/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

import java.nio.charset.StandardCharsets;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.util.Obfuscater;

public class SecAdapter implements Sec {

	final Properties props;
	final Obfuscater actor;
	
	public SecAdapter(Properties props) {
		this.props = props;
		actor = new Obfuscater();
	}
	
	public SecAdapter(Properties props, char [] password) {
		this.props = props;
		actor = new Obfuscater(password);
	}
	
	public void obfuscate(String key){
		String val = props.get(key);
		if(key != null && !key.equals("")){
			String obfuscated = actor.encrypt(val);
			props.put(key, obfuscated);
		}
	}
	
	public void deobfuscate(String key){
		String val = props.get(key);
		if(key != null && !key.equals("")){
			String deobfuscated = actor.decrypt(val);
			props.put(key, deobfuscated);
		}
	}
	
	public char[] deobfuscateToChar(String key){
		String val = props.get(key);
		return actor.decryptToChar(val,StandardCharsets.UTF_8);
	}

}
