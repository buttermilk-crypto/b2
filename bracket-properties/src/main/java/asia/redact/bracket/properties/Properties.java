/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import asia.redact.bracket.properties.impl.ListBackedPropertiesImpl;
import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.impl.SortedPropertiesImpl;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.KeyValueModel;
import asia.redact.bracket.properties.values.ValueModel;

public interface Properties {

	//basic accessors
	
	public String get(String key);
	public String get(String key, String defaultVal);
	public List<String> getValues(String key); // for multi-line get the values
	public Comment getComments(String key); // full comment including # or !
	public char getSeparator(String key);
	public ValueModel getValueModel(String key);
	
	public void put(String key, String ... values);
	public void put(String key, Comment comment, String ... values);
	public void put(String key, char separator, Comment comment, String ... values);
	public void put(KeyValueModel model);
	public void put(String key, ValueModel model);
	
	// Java 8
	public void forEach(BiConsumer<String,ValueModel> action);
	
	// tests
	public boolean containsKey(String key); // return true of the key exists
	public boolean hasNonEmptyValue(String key); // return true if the key exists with a non-empty value
	public int size();
	public boolean hasKeyLike(String leftEdge);
	public List<String> getMatchingKeys(String regex);
	
	// actions
	public void clear();
	public void deleteKey(String key);
	public Properties merge(Properties props);
	public Properties slice(String keyBase);
	
	
	// conversions
	public Map<String,ValueModel> asMap();
	public Map<String,String> asFlattenedMap();
	public List<KeyValueModel> asList();
	public java.util.Properties asLegacy();
	
	// character encoding conversions
	public boolean containsUnicodeEscape(); // indicates the presence somewhere of embedded unicode escapes
	public Properties asciiToNative(); // return a new Properties instance and convert any ASCII escapes found to UTF-8
	public Properties nativeToAscii(); // return a new Properties instance and concert all UTF-8 to ASCII escapes where required
	
	// formatting
	public String toXML();
	public String toJSON();
	public String toYAML();
	public String toString();
	
	public static Properties instance() {
		return new PropertiesImpl().init();
	}
	
	public static Properties concurrentInstance() {
		return new PropertiesImpl(true).init();
	}
	
	public static Properties listInstance() {
		return new ListBackedPropertiesImpl();
	}
	
	public static Properties sortedInstance() {
		return new SortedPropertiesImpl(false, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
			
		});
	}
	
	
}
