/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.Sugar;
import asia.redact.bracket.properties.io.AsciiToNativeFilter;
import asia.redact.bracket.properties.io.NativeToAsciiFilter;
import asia.redact.bracket.properties.io.OutputAdapter;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.Entry;
import asia.redact.bracket.properties.values.KeyValueModel;
import asia.redact.bracket.properties.values.ValueModel;

/**
 * An array-backed implementation. This is used as a base class for source-code generated subclasses.
 * 
 * @author Dave
 *
 */
public abstract class PojoPropertiesImpl implements Properties, Serializable {
	
	// code gen will populates these fields in the subclass. For example:
	//public final Entry key0 = new Entry("key.0", new Comment(""), "value0");
	//public final Entry key1 = new Entry("key.1", new Comment(""), "value1");
	
	private static final long serialVersionUID = 1L;
	
	protected Entry[] entries;

	public PojoPropertiesImpl() {}
	
	  // code generation populates these in init() on the subclass;
	  //	entries = new Entry[2];
	  //    entries[0] = key0;
	  //	entries[1] = key1;
	  //    etc.
	public abstract Properties init();
	
	/**
	 * Find the index of the first matching KeyValueModel with this key. Return -1 if not present
	 * 
	 * @param key
	 * @return
	 */
	protected int find(String key){
		synchronized(entries){
		 for(int i =0; i<entries.length;i++){
			 if(entries[i].getKey().equals(key)) return i;
		 }
		}
		return -1;
	}

	@Override
	public String get(String key) {
		int i = find(key);
		if(i == -1) throw new RuntimeException("key not found: "+key);
		else return entries[i].getValue();
	}

	@Override
	public String get(String key, String defaultVal) {
		int i = find(key);
		if(i == -1) return defaultVal;
		else return entries[i].getValue();
	}

	@Override
	public List<String> getValues(String key) {
		int i = find(key);
		if(i == -1) throw new RuntimeException("key not found: "+key);
		else return entries[i].getValues();
	}

	@Override
	public Comment getComments(String key) {
		int i = find(key);
		if(i == -1) throw new RuntimeException("key not found: "+key);
		else return entries[i].getComments();
	}

	@Override
	public char getSeparator(String key) {
		int i = find(key);
		if(i == -1) throw new RuntimeException("key not found: "+key);
		else return entries[i].getSeparator();
	}

	@Override
	public ValueModel getValueModel(String key) {
		int i = find(key);
		if(i == -1) throw new RuntimeException("key not found: "+key);
		else return entries[i];
	}

	@Override
	public void put(String key, String... values) {
		int i = find(key);
		if(i == -1) throw new RuntimeException("key not found: "+key+". Cannot add keys in this implementation.");
		Entry e = entries[i];
		List<String> list = new ArrayList<>();
		for(String s: values) list.add(s); 
		e.setValues(list);
	}

	@Override
	public void put(String key, Comment comment, String... values) {
		int i = find(key);
		if(i == -1) throw new RuntimeException("key not found: "+key+". Cannot add keys in this implementation.");
		Entry e = entries[i];
		List<String> list = new ArrayList<>();
		for(String s: values) list.add(s); 
		e.setValues(list);
		e.setComment(comment);
		
	}

	@Override
	public void put(String key, char separator, Comment comment, String... values) {
		int i = find(key);
		if(i == -1) throw new RuntimeException("key not found: "+key+". Cannot add keys in this implementation.");
		Entry e = entries[i];
		List<String> list = new ArrayList<>();
		for(String s: values) list.add(s); 
		e.setValues(list);
		e.setComment(comment);
		e.setSeparator(separator);
	}

	@Override
	public void put(KeyValueModel model) {
		int i = find(model.getKey());
		if(i == -1) throw new RuntimeException("key not found: "+model.getKey()+". Cannot add keys in this implementation.");
		Entry e = entries[i];
		List<String> list = new ArrayList<>();
		for(String s: model.getValues()) list.add(s); 
		e.setValues(list);
		e.setComment(model.getComments());
		e.setSeparator(model.getSeparator());
	}

	@Override
	public void put(String key, ValueModel model) {
		int i = find(key);
		if(i == -1) throw new RuntimeException("key not found: "+key+". Cannot add keys in this implementation.");
		Entry e = entries[i];
		List<String> list = new ArrayList<>();
		for(String s: model.getValues()) list.add(s); 
		e.setValues(list);
		e.setComment(model.getComments());
		e.setSeparator(model.getSeparator());
	}

	@Override
	public void forEach(BiConsumer<String, ValueModel> action) {
		for(KeyValueModel model: entries){
			action.accept(model.getKey(), model);
		}
	}

	@Override
	public boolean containsKey(String key) {
		return find(key) != -1; 
	}

	@Override
	public boolean hasNonEmptyValue(String key) {
		String val = get(key);
		return val.trim().length()>0;
	}

	@Override
	public int size() {
		return entries.length;
	}

	@Override
	public boolean hasKeyLike(String leftEdge) {
		for(KeyValueModel model: entries){
			if(model.getKey().startsWith(leftEdge)) return true;
		}
		return false;
	}

	@Override
	public List<String> getMatchingKeys(String regex) {
		 ArrayList<String> array = new ArrayList<>();
		synchronized(entries){
		 for(KeyValueModel model: entries){
			if(model.getKey().matches(regex)) array.add(model.getKey());
		 }
		}
		return array;
	}

	/**
	 * In this context clear() is an undefined operation
	 */
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteKey(String key) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation will fail if any of the keys is unknown to us. I.e., all keys must be within the
	 * set of pre-defined fields for the subclass. 
	 *  
	 * @param props
	 * @return
	 */
	@Override
	public Properties merge(Properties props) {
		synchronized(entries){
			Map<String,ValueModel> map = props.asMap();
			map.forEach((k,v)->{
				this.put(k, v);
			});
		}
		return this;
	}

	@Override
	public Properties slice(String keyBase) {
		ListBackedPropertiesImpl impl = new ListBackedPropertiesImpl();
		for (KeyValueModel model: entries) {
			if (model.getKey().startsWith(keyBase)) {
				impl.put(model);
			}
		}
		return impl;
	}

	@Override
	public Map<String, ValueModel> asMap() {
		Map<String,ValueModel> map = new LinkedHashMap<>();
		synchronized(entries){
			for(KeyValueModel model: entries){
				map.put(model.getKey(), model);
			}
		}
		return map;
	}

	@Override
	public Map<String, String> asFlattenedMap() {
		Map<String,String> map = new LinkedHashMap<>();
		synchronized(entries){
			for(KeyValueModel model: entries){
				map.put(model.getKey(), model.getValue());
			}
		}
		return map;
	}

	@Override
	public List<KeyValueModel> asList() {
		ArrayList<KeyValueModel> list = new ArrayList<>();
		for(Entry e: entries){
			list.add(e);
		}
		return list;
	}

	@Override
	public java.util.Properties asLegacy() {
		java.util.Properties legacy = new java.util.Properties();
		synchronized(entries){
			for(Entry e: entries)
				legacy.put(e.getKey(),e.getValue());
		}
		return legacy;
	}

	@Override
	public Properties asciiToNative() {
		ListBackedPropertiesImpl impl = new ListBackedPropertiesImpl();
		synchronized(entries){
		 for(KeyValueModel model: entries){
			List<String> values = model.getValues();
			ArrayList<String> newValues = new ArrayList<String>();
			if(values.size()>0){
				values.forEach(item->{
					String newVal = new AsciiToNativeFilter(item).read();
					newValues.add(newVal);
				});
			}
			String comments = model.getComments().comments;
			StringBuffer buf = new StringBuffer();
			if (comments != null && comments.length() > 4) {
				buf.append(new AsciiToNativeFilter(comments).read());
			}
			Comment newComments = new Comment(buf.toString());
			impl.put(new Entry(model.getKey(), model.getSeparator(),newComments, newValues));
		 }
		}
		
		return impl;
	}

	@Override
	public Properties nativeToAscii() {
		ListBackedPropertiesImpl impl = new ListBackedPropertiesImpl();
		synchronized(entries){
		 for(KeyValueModel model: entries){
			List<String> values = model.getValues();
			ArrayList<String> newValues = new ArrayList<String>();
			if(values.size()>0){
				values.forEach(item->{
					newValues.add(new NativeToAsciiFilter().write(item).getResult());
				});
			}
			String comments = model.getComments().comments;
			StringBuffer buf = new StringBuffer();
			if (comments != null && comments.length() > 4) {
				buf.append(new NativeToAsciiFilter().write(comments).getResult());
			}
			Comment newComments = new Comment(buf.toString());
			impl.put(new Entry(model.getKey(), model.getSeparator(),newComments,newValues));
		 }
		}
		
		return impl;
	}

	@Override
	public String toXML() {
		return OutputAdapter.toXML(this);
	}

	@Override
	public String toJSON() {
		return OutputAdapter.toJSON(this);
	}

	@Override
	public String toYAML() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Sugar sugar() {
		return new Sugar(this);
	}

}
