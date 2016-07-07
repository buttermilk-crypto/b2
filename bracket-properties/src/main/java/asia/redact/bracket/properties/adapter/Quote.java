/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

public interface Quote {

	public void dq(String key); // same as get() but with quotation marks (\u0022)
	public void sq(String key); //same as get() but with apostrophes. (\u0027)
	public void curly(String key); // same as get() but with curly quotes (\u201C/\u201D)
	public void scurly(String key); // same as get() but with single curly quotes (\u2018/\u2019)
	
	public void dequote(String key);
	public void quoteAll(char start, char end);
	public void dequoteAll();
	
}
