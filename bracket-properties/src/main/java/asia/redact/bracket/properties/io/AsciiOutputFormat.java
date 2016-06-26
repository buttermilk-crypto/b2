/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.io;

import java.text.SimpleDateFormat;
import java.util.List;

import asia.redact.bracket.properties.values.Comment;

/**
 * Compatibility format for simulating java.util.Properties output by encoding
 * characters above ASCII 127 with escapes. Used in conjunction with
 * OutputAdapter.writeAsciiTo().
 * 
 * @author Dave
 *
 */
public class AsciiOutputFormat implements OutputFormat {

	protected final static SimpleDateFormat dateFormatISO8601 = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	public AsciiOutputFormat() {
		super();
	}

	public String formatContentType() {
		 StringBuffer buf = new StringBuffer();
		 buf.append(lineSeparator);
		 buf.append("#;; charset=ISO-8859-1");
		 buf.append(lineSeparator);
		 return buf.toString();
	}

	public String formatHeader() {
		StringBuffer buf = new StringBuffer("#;; generated=");
		buf.append(dateFormatISO8601.format(new java.util.Date()));
		buf.append(lineSeparator);
		buf.append(lineSeparator);
		return buf.toString();
	}

	public String format(String key, char separator, List<String> values, Comment comments) {

		if (key == null)
			throw new RuntimeException("Key cannot be null in a format");

		StringBuffer buf = new StringBuffer();
		if (comments != null && comments.size() > 0) {
			buf.append(new NativeToAsciiFilter().write(comments.comments)
					.getResult());
		}
		StringBuilder keyBuilder = new StringBuilder();
		for (int i = 0; i < key.length(); i++) {
			char ch = key.charAt(i);
			if (ch == ':' || ch == '=') {
				keyBuilder.append('\\');
			}
			keyBuilder.append(ch);
		}
		buf.append(keyBuilder.toString());
		buf.append(separator);

		if (values != null && values.size() > 0) {
			int count = values.size();
			int i = 0;
			for (String s : values) {
				buf.append(new NativeToAsciiFilter().write(s).getResult());
				if (i < count - 1) {
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
