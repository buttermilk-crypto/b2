/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */

package asia.redact.bracket.properties.values;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Representation of a property value.</p>
 * 
 * @author Dave
 *
 */
public class BasicValueModel implements ValueModel {

	private static final long serialVersionUID = 1L;
	final static String lineSeparator = System.getProperty("line.separator");
	protected final Comment comments;
	protected final List<String> values;
	protected char separator = '=';
	
	public BasicValueModel() {
		comments = new Comment();
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
	
    public BasicValueModel(Comment comments, List<String> values) {
		this.comments = comments;
		this.values = values;
	}
    
    public BasicValueModel(Comment comments, String [] values) {
 		this.comments = comments;
 		this.values = new ArrayList<String>();
 		for(String v: values){
 			this.values.add(v);
 		}
 	}
    
    public BasicValueModel(Comment comments, char sep, String [] values) {
    	this.separator = sep;
  		this.comments = comments;
  		this.values = new ArrayList<String>();
  		for(String v: values){
  			this.values.add(v);
  		}
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

	public Comment getComments() {
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
	
	public void addComment(String line){
		comments.addLine(line);
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
	
}
