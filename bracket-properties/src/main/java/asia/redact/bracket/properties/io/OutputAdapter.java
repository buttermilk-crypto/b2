/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map.Entry;
import java.util.Set;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.json.JsonObject;
import asia.redact.bracket.properties.json.WriterConfig;
import asia.redact.bracket.properties.values.ValueModel;


/**
 * Output the properties to various data sinks and in various charsets and formats.
 * 
 * @author Dave
 *
 */
public class OutputAdapter {

	final Properties props;

	public OutputAdapter(Properties properties) {
		super();
		this.props = properties;
	}
	
	/**
	 * Use to control the format of the output to a stream.
	 */
	public void writeTo(OutputStream out, OutputFormat format, Charset charset) {
		
		try (
			OutputStreamWriter writer = new OutputStreamWriter(out,charset);
		){
			writeTo(writer, format);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Use to control the format of the output to a file. 
	 * 
	 */
	public void writeTo(File file, OutputFormat format, Charset charset) {
	
		try (
			FileOutputStream out = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(out,charset);
		){
			writeTo(writer, format);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write to a file using a PlainOutputFormat. If the charset is ISO-8859-1 or US-ASCII, this will
	 * call writeAsciiTo(file, charset) which triggers unicode escapes to be written. If the charset
	 * is UTF-8 or something else, no unicode escapes will be generated
	 * 
	 * @param file
	 * @param charset
	 */
	public void writeTo(File file, Charset charset) {
		
		if(charset.name().equals("US-ASCII") || charset.name().equals("ISO-8859-1")) {
			writeAsciiTo(file, new PlainOutputFormat());
			return;
		}
		
		try (
			FileOutputStream out = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(out,charset);
		){
			writeTo(writer, new PlainOutputFormat());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>This method is specifically intended for compatibility with java.util.Properties, 
	 * which outputs in ISO-8859-1 (US-ASCII)</p>
	 * 
	 * <p>Use AsciiOutputFormat to get unicode escapes or an output format with similar 
	 * filtering, as escapes are required
	 * for compatibility.</p>
	 * 
	 */
	public void writeAsciiTo(File file, OutputFormat format) {
		writeTo(file, format, Charset.forName("ISO-8859-1"));
	}
	
	/**
	 * This is specifically intended for compatibility with java.util.Properties, 
	 * which outputs in ISO-8859-1 (US-ASCII)
	 * 
	 */
	public void writeAsciiTo(File file) {
		writeTo(file, new AsciiOutputFormat(), Charset.forName("ISO-8859-1"));
	}
	
	/**
	 * This is specifically intended for compatibility with java.util.Properties, 
	 * which outputs in ISO-8859-1 (US-ASCII) with escapes
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
		
		Set<Entry<String,ValueModel>> set = props.asMap().entrySet();
		
		writer.append(format.formatContentType());
		writer.append(format.formatHeader());
		
		for(Entry<String,ValueModel> e: set) {
			String key = e.getKey();
			ValueModel model = e.getValue();
			writer.append(format.format(key, model.getSeparator(),model.getValues(),model.getComments()));
		}
		
		writer.append(format.formatFooter());
	}
	
	/**
	 * Same as above but with a PlainOutputFormat
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void writeTo(Writer writer) throws IOException {
		writeTo(writer,new PlainOutputFormat());
	}
	
	/**
	 * Write properties in a default manner to a String. No unicode escapes are performed here
	 * 
	 * @param props
	 * @return
	 */
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
	
	/**
	 * Write out a legacy Sun DTD-compatible XML-formatted string representation of the properties. The
	 * comments are not retained. This improves on the legacy Properties class only in 
	 * that order is retained and CDATA is applied on values when required
	 * 
	 * @param props
	 * @return
	 */
	public static final String toXML(Properties props){
		StringWriter writer = new StringWriter();
		try {
			new OutputAdapter(props).writeTo(writer, new XMLOutputFormat(null));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
	
	/**
	 * Simple JSON formatting, emits the properties as an ordered, flattened JSON object. The output
	 * is pretty-printed
	 * 
	 * @param props
	 * @return
	 */
	public static final String toJSON(Properties props){
		return toJSON(props,WriterConfig.PRETTY_PRINT);
	}
	
	/**
	 * Simple JSON formatting, emits the properties as an ordered, flattened JSON object
	 * where you control the output format. Currently there are only two options. 
	 * 
	 * @param props
	 * @param WriterConfig can be either WriterConfig.PRETTY_PRINT or WriterConfig.MINIMAL.
	 * @return
	 */
	public static final String toJSON(Properties props, WriterConfig config){
		JsonObject contents = new JsonObject();
		props.forEach((k,v)->{
			contents.add(k, v.getValue());
			
		});
		return contents.toString(config);
	}

}
