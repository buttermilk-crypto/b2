/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.io;

import java.io.IOException;

public class NativeToAsciiFilter {

	final static String lineBreak = System.getProperty("line.separator");
	final StringBuffer out;

	public NativeToAsciiFilter() {
		out = new StringBuffer();
	}

	public NativeToAsciiFilter(String initialInput) {
		out = new StringBuffer();
		write(initialInput);
	}

	public NativeToAsciiFilter(StringBuffer buf) {
		out = buf;
	}

	public NativeToAsciiFilter write(char[] buf) {
		write(buf, 0, buf.length);
		return this;
	}

	public NativeToAsciiFilter write(String str) {
		try {
			write(str, 0, str.length());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	public NativeToAsciiFilter write(char[] buf, int off, int len) {

		for (int i = 0; i < len; i++) {
			if ((buf[i] > '\u007F')) {
				out.append('\\');
				out.append('u');
				String hex = Integer.toHexString(buf[i]);
				StringBuilder hex4 = new StringBuilder(hex);
				hex4.reverse();
				int length = 4 - hex4.length();
				for (int j = 0; j < length; j++)
					hex4.append('0');
				for (int j = 0; j < 4; j++)
					out.append(hex4.charAt(3 - j));
			} else {
				out.append(buf[i]);
			}
		}

		return this;
	}

	public NativeToAsciiFilter write(String str, int off, int len) throws IOException {
		write(str.toCharArray(), off, len);
		return this;
	}

	public String getResult() {
		return out.toString();
	}
}
