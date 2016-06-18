/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.mgmt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.mgmt.ReferenceType;

/**
 * <pre>
 * Our list of references from which to load properties. The list can contain
 * externalized files, internal files within the classpath (such as in a jar
 * file) and even direct name value pairs.
 * 
 * Localization is supported, just set attributes.locale to the appropriate locale.
 * 
 * </pre>
 * 
 * @author Dave
 * 
 */
public class LoadList {

	protected final List<PropertiesReference> list;
	protected final Properties props;

	public LoadList() {
		list = new ArrayList<PropertiesReference>();
		props = new PropertiesImpl();
	}

	/**
	 * This is understood as a shortcut for
	 * PropertiesReference(ReferenceType.EXTERNAL,fileloc);
	 * 
	 */
	public void addReference(File file) {
		try {
			list.add(new PropertiesReference(ReferenceType.EXTERNAL, file.getCanonicalPath()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addReference(ReferenceType type, String path) {
		list.add(new PropertiesReference(type, path));
	}

	public void addReference(PropertiesReference ref) {
		list.add(ref);
	}

	public void load() {

		for (PropertiesReference ref : list) {
			switch (ref.type) {
			case CLASSLOADED:
					
				break;
			case DIRECT:
				
				break;
			case EXTERNAL:
				
				break;
			case OBFUSCATED:
				
				break;
			case COMMANDLINE_OVERRIDE:
				loadFromSystemProps(ref.data);
				break;
			default:
				throw new RuntimeException("Unknown type: " + ref);
			}
		}
	}

	private void loadFromSystemProps(String key) {
		String val = System.getProperty(key);
		if (val != null) {
			props.put(key, val);
		}
	}

	public List<PropertiesReference> getList() {
		return list;
	}

	public Properties getProps() {
		return props;
	}


}
