/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.values;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable model
 * 
 * @author Dave
 *
 */
public class ImmutableValueModel implements ValueModel {

	private static final long serialVersionUID = 1L;
	final static String lineSeparator = System.getProperty("line.separator");
	
	private final char separator;
	private final String [] values;
	private final Comment [] comments;

	/**
	 * Produces an empty or null model
	 */
	public ImmutableValueModel() {
		separator = '=';
		values = new String[0];
		comments = new Comment[0];
	}
	
	public ImmutableValueModel(String ...values){
		separator = '=';
		this.values = values;
		comments = new Comment[0];
	}
	
	public ImmutableValueModel(Comment [] comments, String ...values){
		separator = '=';
		this.values = values;
		this.comments = comments;
	}
	
	public ImmutableValueModel(Comment [] comments, char sep, String ...values){
		separator = sep;
		this.values = values;
		this.comments = comments;
	}
	
	public ImmutableValueModel(List<String> comments, char sep, List<String> values){
		separator = sep;
		
		this.values = new String[values.size()];
		this.comments = new Comment[comments.size()];
		int i = 0;
		for(String val : values){
			this.values[i] = val;
			i++;
		}
		i = 0;
		for(String val : comments){
			this.comments[i] = new Comment(val);
			i++;
		}
	}

	@Override
	public char getSeparator() {
		return this.separator;
	}

	@Override
	public List<String> getComments() {
		List<String> list = new ArrayList<String>();
		for(Comment c: comments){
			list.add(c.comment);
		}
		return list;
	}

	@Override
	public List<String> getValues() {
		List<String> list = new ArrayList<String>();
		for(String s: values){
			list.add(s);
		}
		return list;
	}

	public String getValue(){
		StringBuilder b = new StringBuilder();
		for(String value:values) b.append(value);
		return b.toString();
	}
	
	public String toString(){
		return getValue();
	}

	public String asKeyValueRep(String key){
		StringBuffer buf = new StringBuffer();
		if(comments.length > 0){
			for(Comment com: comments){
				buf.append(com.comment);
				buf.append(lineSeparator);
			}
		}
		buf.append(key);
		buf.append(separator);
		int count = 0;
		if(values.length == 0){
			// output nothing
		}else if(values.length == 1){
			for(String val: values){
				buf.append(val);
				buf.append(lineSeparator);
		
			}
		}else {
			for(String val: values){
				buf.append(val);
			    if(count<values.length-1) buf.append("\\");
				buf.append(lineSeparator);
				count++;
			}
		}
		
		return buf.toString();
	}

	@Override
	public ValueModel cloneImmutable() {
		return new ImmutableValueModel(comments, separator, values);
	}

}
