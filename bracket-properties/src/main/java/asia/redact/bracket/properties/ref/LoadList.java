/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.ref;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.PropertiesParser;
import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.line.LineScanner;
import asia.redact.bracket.properties.ref.ReferenceType;

/**
 * <p>
 * Container for list of references from which to load properties. The list can contain
 * externalized files, internal files within the classpath (such as in a jar file) and 
 * even direct name value pairs.
 * </p>
 * 
 * <p>
 * The key idea is that if we load references in a given order, the properties values will be
 * added and later additions will overwrite earlier ones. This means you can load a template file
 * first (perhaps from the classpath) and then an override certain values from the file system afterwards. 
 * </p>
 * 
 * @author Dave
 * 
 */
public class LoadList {

	final List<PropertiesReference> list;
	final Properties props;

	public LoadList() {
		list = new ArrayList<PropertiesReference>();
		props = new PropertiesImpl(false).init();
	}
	
	public LoadList(Properties props) {
		list = new ArrayList<PropertiesReference>();
		this.props = props;
	}

	/**
	 * This is understood as a shortcut for
	 * PropertiesReference(ReferenceType.EXTERNAL, filepath);
	 * 
	 */
	public LoadList addReference(File file) {
		try {
			list.add(new PropertiesReference(ReferenceType.EXTERNAL, file.getCanonicalPath()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return this;
	}

	/**
	 * add a reference. The specific value of data depends on the type of reference.
	 * 
	 * @param type
	 * @param data
	 */
	public LoadList addReference(ReferenceType type, String data) {
		list.add(new PropertiesReference(type, data));
		return this;
	}

	public LoadList addReference(PropertiesReference ref) {
		list.add(ref);
		return this;
	}

	public LoadList load() {

		for (PropertiesReference ref : list) {
			switch (ref.type) {
			case CLASSLOADED:
				this.loadFromClasspath(ref);
				break;
			case DIRECT:
				loadFromDirect(ref);
				break;
			case EXTERNAL:
				loadFromExternal(ref);
				break;
			case COMMANDLINE_OVERRIDE:
				loadFromSystemProps(ref.data);
				break;
			default:
				throw new RuntimeException("Unknown type: " + ref);
			}
		}
		
		return this;
	}

	private void loadFromSystemProps(String key) {
		String val = System.getProperty(key);
		if (val != null) {
			props.put(key, val);
		}
	}
	
	/**
	 * interpret ref.data as a class path such as /myprops.properties
	 *  
	 * @param ref
	 */
	private void loadFromClasspath(PropertiesReference ref) {

		InputStream in = getClass().getResourceAsStream(ref.data);

		// try a different approach, sometimes works
		if (in == null) {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(ref.data);
			if (in == null) {
				throw new RuntimeException("path " + ref.data + " was not found as a ResourceStream");
			}
		}
		
		try(
			InputStreamReader reader = new InputStreamReader(in, ref.charset);
			BufferedReader breader = new BufferedReader(reader);
			LineScanner scanner = new LineScanner(breader);
		){
			PropertiesParser parser = new PropertiesParser(scanner);
			props.merge(parser.parse().getProperties());
		}catch(IOException x){
			throw new RuntimeException(x);
		}
		
	}
	
	/**
	 * Interpret ref.data as a stream of properties such as myval.0=1\nmyval1=2 
	 * 
	 * @param ref
	 */
	private void loadFromDirect(PropertiesReference ref) {
		try (
			StringReader reader = new StringReader(ref.data);
			LineScanner scanner = new LineScanner(reader);
		){
			PropertiesParser parser = new PropertiesParser(scanner);
			props.merge(parser.parse().getProperties());
		}catch(IOException x){
			throw new RuntimeException(x);
		}
	}
	
	/**
	 * Support data which is a directory path containing properties file(s) 
	 * or a else file path to a single properties file
	 * 
	 * @param ref
	 */
	private void loadFromExternal(PropertiesReference ref){
		
		File file = new File(ref.data);

			if (file.isDirectory()) {
				File[] mylist = file.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.contains(".properties");
					}
				});
				
				for(File f: mylist){
					try(
							FileInputStream in = new FileInputStream(f);
							InputStreamReader reader = new InputStreamReader(in, ref.charset);
							BufferedReader breader = new BufferedReader(reader);
							LineScanner scanner = new LineScanner(breader);
						){
							PropertiesParser parser = new PropertiesParser(scanner);
							props.merge(parser.parse().getProperties());
						}catch(IOException x){
							throw new RuntimeException(x);
						}
						
				}
				
			}else{
				
				try(
						FileInputStream in = new FileInputStream(file);
						InputStreamReader reader = new InputStreamReader(in, ref.charset);
						BufferedReader breader = new BufferedReader(reader);
						LineScanner scanner = new LineScanner(breader);
					){
						PropertiesParser parser = new PropertiesParser(scanner);
						props.merge(parser.parse().getProperties());
					}catch(IOException x){
						throw new RuntimeException(x);
					}
			}
	}

	public List<PropertiesReference> getList() {
		return list;
	}

	public Properties getProps() {
		return props;
	}


}
