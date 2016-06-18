/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */

package asia.redact.bracket.properties.values;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Representation of properties values.</p>
 * 
 * <p>We consider comments above the key/value pair to be a part of the value. This is not really correct,
 * and they should probably be associated with the key, but it generally works because there is a one to many 
 * relation between a key/value pair and a set of comments. It is also nice to keep a key as a simple 
 * String data type.</p>
 * 
 * <p>Only comments above the key/value pair are considered part of the comment set. This is an arbitrary determination,
 * but it places a very light burden on a programmer and it seems a reasonable convention. It means comments at the end 
 * of a file (past any key) may potentially be discarded.</p>
 * 
 * @author Dave
 *
 */
public class BasicValueModel implements ValueModel {

	private static final long serialVersionUID = 1L;
	final static String lineSeparator = System.getProperty("line.separator");
	protected final List<String> comments;
	protected final List<String> values;
	protected char separator = '=';
	
	public BasicValueModel() {
		comments = new ArrayList<String>();
		values = new ArrayList<String>();
	}
	
	public BasicValueModel(String ... value) {
		this();
		for(String v: value){
			values.add(v);
		}
	}
	
	public BasicValueModel(char sep,String... value) {
		this();
		this.separator=sep;
		for(String v: value){
			values.add(v);
		}
	}
	
	public BasicValueModel(Comment comment,String... value) {
		this();
		comments.add(comment.comment);
		for(String v: value){
			values.add(v);
		}
	}
	
	public BasicValueModel(Comment comment, char sep, String... value) {
		this();
		this.separator=sep;
		comments.add(comment.comment);
		for(String v: value){
			values.add(v);
		}
	}
	
	
    BasicValueModel(List<String> comments, List<String> values) {
		this.comments = comments;
		this.values = values;
	}

	@Override
	public synchronized int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + separator;
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	@Override
	public synchronized boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicValueModel other = (BasicValueModel) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (separator != other.separator)
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	public List<String> getComments() {
		return comments;
	}

	public List<String> getValues() {
		return values;
	}
	
	public void addValue(String value){
		values.add(value);
	}
	
	public void addValues(List<String> values){
		this.values.addAll(values);
	}
	
	public void addComment(String comment){
		comments.add(comment);
	}
	
	public void addComments(List<String> comments){
		this.comments.addAll(comments);
	}
	
	public void clearComments() {
		comments.clear();
	}
	
	public void clearValues(){
		values.clear();
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
		if(comments.size() > 0){
			for(String com: comments){
				buf.append(com);
				buf.append(lineSeparator);
			}
		}
		buf.append(key);
		buf.append(separator);
		int count = 0;
		if(values.size() == 0){
			// output nothing
		}else if(values.size() == 1){
			for(String val: values){
				buf.append(val);
				buf.append(lineSeparator);
		
			}
		}else {
			for(String val: values){
				buf.append(val);
			    if(count<values.size()-1) buf.append("\\");
				buf.append(lineSeparator);
				count++;
			}
		}
		
		return buf.toString();
	}
	
	public ValueModel cloneImmutable() {
		return new ImmutableValueModel(comments, separator, values);
	}
	
}
