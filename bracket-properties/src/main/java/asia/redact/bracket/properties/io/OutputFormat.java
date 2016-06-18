/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.io;

import java.util.List;

/**
 * You can implement custom output formats using this interface and OutputAdapter.writeTo(Writer, OutputFormat);
 * 
 * @author Dave
 */
public interface OutputFormat {
	
	public final static String lineSeparator = System.getProperty("line.separator");

	public String formatContentType();
	public String formatHeader();
	public String format(String key, char separator, List<String> values, List<String> comments);
	public String formatFooter();
	
}
