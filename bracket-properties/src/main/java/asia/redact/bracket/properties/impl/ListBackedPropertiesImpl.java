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
import asia.redact.bracket.properties.io.AsciiToNativeFilter;
import asia.redact.bracket.properties.io.NativeToAsciiFilter;
import asia.redact.bracket.properties.io.OutputAdapter;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.Entry;
import asia.redact.bracket.properties.values.KeyValueModel;
import asia.redact.bracket.properties.values.ValueModel;

/**
 * <p>A list-backed implementation. In theory this class could accept duplicate keys but we have
 * guards to prevent it. Also it has got synchronized guards on operations so it
 * should be reasonably thread-safe.</p>
 * 
 * @author Dave
 * @see PropertiesImpl
 * @see SortedPropertiesImpl
 */
public class ListBackedPropertiesImpl implements Properties, Serializable {

	private static final long serialVersionUID = 1L;
	
	protected List<KeyValueModel> list;

	public ListBackedPropertiesImpl() {
		list = new ArrayList<KeyValueModel>();
	}
	
	/**
	 * Find the index of the first matching KeyValueModel with this key. Return -1 if not present
	 * 
	 * @param key
	 * @return
	 */
	protected int find(String key){
		synchronized(list){
		 for(int i =0; i<list.size();i++){
			 if(list.get(i).getKey().equals(key)) return i;
		 }
		}
		return -1;
	}

	@Override
	public String get(String key) {
		return list.get(find(key)).getValue();
	}

	@Override
	public String get(String key, String defaultVal) {
		int index = find(key);
		if(index == -1) return defaultVal;
		else return list.get(index).getValue();
	}

	@Override
	public List<String> getValues(String key) {
		return list.get(find(key)).getValues();
	}

	@Override
	public Comment getComments(String key) {
		return list.get(find(key)).getComments();
	}

	@Override
	public char getSeparator(String key) {
		return list.get(find(key)).getSeparator();
	}

	@Override
	public ValueModel getValueModel(String key) {
		return list.get(find(key));
	}

	@Override
	public void put(String key, String... values) {
		synchronized(list){
			if(containsKey(key)) {
				int index = find(key);
				list.remove(index);
				list.add(index, new Entry(key,values));
			}else{
				list.add(new Entry(key,values));
			}
		}
	}

	@Override
	public void put(String key, Comment comment, String... values) {
		synchronized(list){
			if(containsKey(key)) {
				int index = find(key);
				list.remove(index);
				list.add(index, new Entry(key,comment,values));
			}else{
				list.add(new Entry(key,comment,values));
			}
		}
	}

	@Override
	public void put(String key, char separator, Comment comment,String... values) {
		
		ArrayList<String> listVals = new ArrayList<>();
		for(String s: values){
			listVals.add(s);
		}
		
		synchronized(list){
			if(containsKey(key)) {
				int index = find(key);
				list.remove(index);
				list.add(index, new Entry(key,separator,comment,listVals));
			}else{
				list.add(new Entry(key,separator,comment,listVals));
			}
		}

	}

	@Override
	public void put(KeyValueModel model) {
		synchronized(list){
			if(containsKey(model.getKey())) {
				int index = find(model.getKey());
				list.remove(index);
				list.add(index, model);
			}else{
				list.add(model);
			}
		}
	}

	@Override
	public void put(String key, ValueModel model) {
		synchronized(list){
			if(containsKey(key)) {
				int index = find(key);
				list.remove(index);
				list.add(index, new Entry(key,model.getSeparator(),model.getComments(),model.getValues()));
			}else{
				list.add(new Entry(key,model.getSeparator(),model.getComments(),model.getValues()));
			}
		}
	}

	@Override
	public void forEach(BiConsumer<String, ValueModel> action) {
		for(KeyValueModel model: list){
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
		return list.size();
	}

	@Override
	public boolean hasKeyLike(String leftEdge) {
		for(KeyValueModel model: list){
			if(model.getKey().startsWith(leftEdge)) return true;
		}
		return false;
	}

	@Override
	public List<String> getMatchingKeys(String regex) {
		 ArrayList<String> array = new ArrayList<>();
		synchronized(list){
		 for(KeyValueModel model: list){
			if(model.getKey().matches(regex)) array.add(model.getKey());
		 }
		}
		return array;
	}

	@Override
	public void clear() {
		synchronized(list){
			list.clear();
		}
	}

	@Override
	public void deleteKey(String key) {
		synchronized(list){
			int index = find(key);
			if(index != -1){
				list.remove(index);
			}
		}
	}

	@Override
	public Properties merge(Properties props) {
		synchronized(list){
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
		for (KeyValueModel model: list) {
			if (model.getKey().startsWith(keyBase)) {
				impl.put(model);
			}
		}
		return impl;
	}

	@Override
	public Map<String, ValueModel> asMap() {
		Map<String,ValueModel> map = new LinkedHashMap<>();
		synchronized(list){
			for(KeyValueModel model: list){
				map.put(model.getKey(), model);
			}
		}
		return map;
	}

	@Override
	public Map<String, String> asFlattenedMap() {
		Map<String,String> map = new LinkedHashMap<>();
		synchronized(list){
			for(KeyValueModel model: list){
				map.put(model.getKey(), model.getValue());
			}
		}
		return map;
	}

	@Override
	public List<KeyValueModel> asList() {
		return list;
	}

	@Override
	public java.util.Properties asLegacy() {
		java.util.Properties legacy = new java.util.Properties();
		synchronized(list){
			list.forEach(item->{
				legacy.put(item.getKey(),item.getValue());
			});
		}
		return legacy;
	}

	@Override
	public Properties asciiToNative() {
		ListBackedPropertiesImpl impl = new ListBackedPropertiesImpl();
		synchronized(list){
		 for(KeyValueModel model: list){
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
		synchronized(list){
		 for(KeyValueModel model: list){
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


}
