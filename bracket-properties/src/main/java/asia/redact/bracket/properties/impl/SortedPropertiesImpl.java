/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.io.AsciiToNativeFilter;
import asia.redact.bracket.properties.io.NativeToAsciiFilter;
import asia.redact.bracket.properties.values.BasicValueModel;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.ValueModel;

/**
 * A sortable implementation using a TreeMap
 * 
 * @author dave
 *
 */
public class SortedPropertiesImpl extends PropertiesImpl implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Comparator<CharSequence> comparator;

	public SortedPropertiesImpl(boolean concurrent, Comparator<CharSequence> comparator) {
		super(concurrent);
		this.comparator = comparator;
	}

	@Override
	public Properties init() {
		if(comparator == null) {
			if(concurrent) {
				map = Collections.synchronizedMap(new TreeMap<>());
			}else{
				map = new TreeMap<>();
			}
		}else{
			if(concurrent) {
				map = Collections.synchronizedMap(new TreeMap<>(comparator));
			}else{
				map = new TreeMap<>(comparator);
			}
		}
		return this;
	}

	public Properties asciiToNative() {
		Properties impl = new SortedPropertiesImpl(concurrent,comparator).init();
		for(Map.Entry<String,ValueModel> entry: this.map.entrySet()){
			ValueModel model = entry.getValue();
			List<String> values = model.getValues();
			ArrayList<String> newValues = new ArrayList<String>();
			if(values.size()>0){
				values.forEach(item->{
					newValues.add(new AsciiToNativeFilter(item).read());
				});
			}
			String comments = model.getComments().comments;
			StringBuffer buf = new StringBuffer();
			if (comments != null && comments.length() > 4) {
				buf.append(new AsciiToNativeFilter(comments).read());
			}
			Comment newComments = new Comment(buf.toString());
			impl.put(entry.getKey(), new BasicValueModel(newComments,model.getSeparator(),newValues));
		}
		
		return impl;
	}
	
	public Properties nativeToAscii() {
		Properties impl = new SortedPropertiesImpl(concurrent,comparator).init();
		for(Map.Entry<String,ValueModel> entry: this.map.entrySet()){
			ValueModel model = entry.getValue();
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
			impl.put(entry.getKey(), new BasicValueModel(newComments,model.getSeparator(),newValues));
		}
		
		return impl;
	}
}
