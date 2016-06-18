/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.io;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Default formatting for properties output. You can implement your own if you don't like this one 
 * or it doesn't do what you require.
 * 
 * @author Dave
 *
 */
public class HeaderFooterOutputFormat implements OutputFormat {
	
	protected final static SimpleDateFormat dateFormatISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	public String formatContentType() {
		//StringBuffer buf = new StringBuffer("#;; content=text/x-java-properties");
		//buf.append(lineSeparator);
		//buf.append("#;; charset=UTF-8");
		//buf.append(lineSeparator);
		//return buf.toString();
		return "";
	}
	
	public String formatHeader() {
		StringBuffer buf = new StringBuffer("#;; generated=");
		buf.append(dateFormatISO8601.format(new java.util.Date()));
		buf.append(lineSeparator);
		buf.append(lineSeparator);
		return buf.toString();
	}
	
	public String format(String key, char separator, List<String> values, List<String> comments) {
		
		if(key == null) throw new RuntimeException("Key cannot be null in a format");
		StringBuffer buf = new StringBuffer();
		if(comments != null && comments.size()>0) {
			for(String c: comments){
				buf.append(c);
				buf.append(lineSeparator);
			}
		}
	    StringBuilder keyBuilder=new StringBuilder();
	    for(int i=0;i<key.length();i++){
	    	char ch = key.charAt(i);
	    	if(ch==':'||ch=='='){
	    		keyBuilder.append('\\');	
	    	}
	    	keyBuilder.append(ch);
	    }
		buf.append(keyBuilder.toString());
		buf.append(separator);
		
		if(values != null && values.size()>0){
			int count = values.size();
			int i = 0;
			for(String s: values){
				buf.append(s);
				if(i<count-1) {
					buf.append('\\');
				}
				buf.append(lineSeparator);
				i++;
			}
		}
		
		return buf.toString();
	}

	public String formatFooter() {
		StringBuffer buf = new StringBuffer(lineSeparator);
		buf.append("#;; eof");
		buf.append(lineSeparator);
		return buf.toString();
	}
}
