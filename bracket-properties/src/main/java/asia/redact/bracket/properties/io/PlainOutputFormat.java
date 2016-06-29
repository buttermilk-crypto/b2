/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.io;

import java.util.List;

/**
 * 
 * Really simple, no frills output similar to legacy.
 * 
 * @author Dave
 *
 */
public class PlainOutputFormat extends HeaderFooterOutputFormat {
	
	
	public String formatContentType() {
		return "";
	}
	
	public String formatHeader() {
		return "";
	}
	
	public String format(String key, char separator, List<String> values, List<String> comments) {
		
		if(key == null) throw new RuntimeException("Key cannot be null in a format");
		
		StringBuffer buf = new StringBuffer();
		if(comments != null && comments.size()>0) {
			for(String c: comments){
			//	buf.append("# ");  
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
		return "";
	}

}
