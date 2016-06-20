/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.Set;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.values.ValueModel;


/**
 * Output the properties to various data sinks and in various charsets and formats.
 * 
 * @author Dave
 *
 */
public class OutputAdapter {

	final Properties properties;

	public OutputAdapter(Properties properties) {
		super();
		this.properties = properties;
	}
	
	/**
	 * Use to control the format of the output to a stream.
	 */
	public void writeTo(OutputStream out, OutputFormat format, Charset charset) {
		
		try {
			OutputStreamWriter writer = new OutputStreamWriter(out,charset);
			writeTo(writer, format);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * Use to control the format of the output to a file. 
	 * 
	 */
	public void writeTo(File file, OutputFormat format, Charset charset) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(out,charset);
			writeTo(writer, format);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * This is specifically intended for compatibility with java.util.Properties, which outputs in ISO-8859-1 (US-ASCII)
	 * 
	 * Use AsciiOutputFormat to get unicode escapes or an output format with similar filtering, as escapes are required
	 * for compatibility.
	 * 
	 */
	public void writeAsciiTo(File file, OutputFormat format) {
		writeTo(file, format, Charset.forName("ISO-8859-1"));
	}
	
	/**
	 * This is specifically intended for compatibility with java.util.Properties, which outputs in ISO-8859-1 (US-ASCII)
	 * 
	 */
	public void writeAsciiTo(File file) {
		writeTo(file, new AsciiOutputFormat(), Charset.forName("ISO-8859-1"));
	}
	
	/**
	 * This is specifically intended for compatibility with java.util.Properties, which outputs in ISO-8859-1 (US-ASCII)
	 * 
	 */
	public void writeAsciiTo(Writer writer) {
		try {
			writeTo(writer, new AsciiOutputFormat());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeTo(Writer writer, OutputFormat format) throws IOException {
		
		Set<Entry<String,ValueModel>> set = properties.asMap().entrySet();
		
		writer.append(format.formatContentType());
		writer.append(format.formatHeader());
		
		for(Entry<String,ValueModel> e: set) {
			String key = e.getKey();
			ValueModel model = e.getValue();
			writer.append(format.format(key, model.getSeparator(),model.getValues(),model.getComments()));
		}
		
		writer.append(format.formatFooter());
	}
	
	public void writeTo(Writer writer) throws IOException {
		writeTo(writer,new PlainOutputFormat());
	}
	
	public static final String toString(Properties props){
		OutputAdapter out = new OutputAdapter(props);
		StringWriter writer = new StringWriter();
		try {
			out.writeTo(writer);
			return writer.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static final String toXML(Properties props){
		java.util.Properties p = props.asLegacy();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			p.storeToXML(out, "");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return new String(out.toByteArray(), StandardCharsets.UTF_8);
	}

}
