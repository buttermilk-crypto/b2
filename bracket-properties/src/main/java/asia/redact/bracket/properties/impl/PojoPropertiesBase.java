/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.Sugar;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.Entry;
import asia.redact.bracket.properties.values.KeyValueModel;
import asia.redact.bracket.properties.values.ValueModel;

/**
 * An array-backed implementation. This is used as a base class for source-code generation.
 * 
 * @author Dave
 *
 */
public abstract class PojoPropertiesBase implements Properties, Serializable {
	
	// code gen will populates these fields in the subclass. For example:
	//public final Entry key0 = new Entry("key.0", new Comment(""), "value0");
	//public final Entry key1 = new Entry("key.1", new Comment(""), "value1");
	
	private static final long serialVersionUID = 1L;
	
	protected Entry[]entries;

	public PojoPropertiesBase() {
	  // codegen populates these in init();
	  //	entries = new Entry[2];
	  //    entries[0] = key0;
	  //	entries[1] = key1;
	}
	
	public abstract void init();

	@Override
	public String get(String key) {
		
		return null;
	}

	@Override
	public String get(String key, String defaultVal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getValues(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comment getComments(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getSeparator(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ValueModel getValueModel(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(String key, String... values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(String key, Comment comment, String... values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(String key, char separator, Comment comment,
			String... values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(KeyValueModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(String key, ValueModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forEach(BiConsumer<String, ValueModel> action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsKey(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasNonEmptyValue(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasKeyLike(String leftEdge) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getMatchingKeys(String regex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteKey(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Properties merge(Properties props) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties slice(String keyBase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ValueModel> asMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> asFlattenedMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KeyValueModel> asList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.util.Properties asLegacy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties asciiToNative() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties nativeToAscii() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toYAML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sugar sugar() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
