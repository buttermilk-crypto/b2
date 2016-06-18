/*
 *  This file is part of Bracket Properties
 *  Copyright 2013 David R. Smith
 *
 */
package asia.redact.bracket.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * <pre>
 * Rework of the Sun open source code - write UTF escapes for higher than ASCII characters.
 * 
 * Known issues:
 * 
 * The serialization does not use caps for hex A-F as I would like
 * 
 * </pre>
 * 
 * @author Dave
 *
 */
public class NativeToAsciiFilterWriter extends FilterWriter {
	
	final String lineBreak = System.getProperty("line.separator");
	
	public NativeToAsciiFilterWriter(Writer out) {
		super(out);
	}
	
	public void write(char [] buf) {
		write(buf,0,buf.length);
	}
	
	public void write(String str) {
		try {
			write(str,0,str.length());
		} catch (IOException e) {
			throw new RuntimeException("ERROR: Writer failed", e);
		}
	}
	
	  public void write(char[] buf, int off, int len) {

		  try {
		    for (int i = 0; i < len; i++) {
		        if ((buf[i] > '\u007F')) { 
				    out.write('\\');
			        out.write('u');
			        String hex = Integer.toHexString(buf[i]);
			        StringBuilder hex4 = new StringBuilder(hex);
			        hex4.reverse();
			        int length = 4 - hex4.length();
			        for (int j = 0; j < length; j++) hex4.append('0');
			        for (int j = 0; j < 4; j++) out.write(hex4.charAt(3 - j));
		        } else {
		        	out.write(buf[i]);
		        }
		    }
		        
		    } catch (IOException e) {
				throw new RuntimeException("ERROR: Writer failed", e);
			}
		}
	  
	  /**
	     * Writes a portion of a string.
	     *
	     * @param  str  String to be written
	     * @param  off  Offset from which to start reading characters
	     * @param  len  Number of characters to be written
	     *
	     * @exception  IOException  If an I/O error occurs
	     */
	    public void write(String str, int off, int len) throws IOException {
	    	write(str.toCharArray(), off, len);
	    }
}
