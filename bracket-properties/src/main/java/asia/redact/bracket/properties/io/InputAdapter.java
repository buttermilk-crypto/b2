/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Utility methods to get data out of different sources. This is pre-java-8 style code, but it still works
 */
public class InputAdapter {

	private static final int DEFAULT_BUFFER_SIZE = 8192;
	private final Lock lock = new ReentrantLock();

	public InputAdapter() {
		super();
	}
	
	/**
	 * Read a file into a String.
	 * 
	 * @param file
	 * @param charset
	 * @return
	 */
	String asString(File file, Charset charset) {
		lock.lock();
		try {
			FileInputStream pStream = null;
			InputStreamReader inReader = null;
			try {
				pStream = new FileInputStream(file);
				inReader = new InputStreamReader(pStream, charset);
				StringBuilder builder = new StringBuilder();
				char [] cbuf = new char[DEFAULT_BUFFER_SIZE];
				int count = 0;
				while((count = inReader.read(cbuf))!=-1){
					builder.append(cbuf, 0, count);
				}
				return builder.toString();
			} catch (IOException x) {
				if (inReader != null) {
					try {
						inReader.close();
					} catch (IOException e) {
					}
				}
				return null;
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Read a reader contents into a String
	 * 
	 * @param reader
	 * @return
	 */
	String asString(Reader reader) {
		lock.lock();
		try {
			try {
				char[] arr = new char[8 * 1024]; // 8K at a time
				StringBuilder buf = new StringBuilder();
				int numChars;

				while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
					buf.append(arr, 0, numChars);
				}

				return buf.toString();
			} catch (IOException x) {
				return null;
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Read a stream and copy the contents into a String
	 * 
	 * @param pStream
	 * @param charsetName
	 * @return
	 */
	String asString(InputStream pStream, String charsetName) {
		lock.lock();
		try {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				copy(pStream, baos, true);
				return baos.toString(charsetName);
			} catch (IOException x) {
				return null;
			}
		} finally {
			lock.unlock();
		}
	}

	private long copy(InputStream pInputStream, OutputStream pOutputStream,boolean pClose) throws IOException {
		return copy(pInputStream, pOutputStream, pClose, new byte[DEFAULT_BUFFER_SIZE]);
	}

	private long copy(InputStream pIn, OutputStream pOut, boolean pClose, byte[] pBuffer) throws IOException {
		lock.lock();
		try {
			OutputStream out = pOut;
			InputStream in = pIn;
			try {
				long total = 0;
				for (;;) {
					int res = in.read(pBuffer);
					if (res == -1) {
						break;
					}
					if (res > 0) {
						total += res;
						if (out != null) {
							out.write(pBuffer, 0, res);
						}
					}
				}
				if (out != null) {
					if (pClose) {
						out.close();
					} else {
						out.flush();
					}
					out = null;
				}
				in.close();
				in = null;
				return total;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Throwable t) {
						/* Ignore me */
					}
				}
				if (pClose && out != null) {
					try {
						out.close();
					} catch (Throwable t) {
						/* Ignore me */
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}

}
