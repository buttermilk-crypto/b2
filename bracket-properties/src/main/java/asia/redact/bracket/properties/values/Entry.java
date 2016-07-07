/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.values;

import java.util.ArrayList;
import java.util.List;

public class Entry implements KeyValueModel {

	private static final long serialVersionUID = 1L;
	
	private final String key;
	private Comment comment; // may contain many lines. 
	private List<String> values;
	private char separator = '=';
	
	public Entry(String key, String ... values) {
		super();
		this.key = key;
		comment = new Comment();
		this.values = new ArrayList<String>();
		for(String v: values){
			this.values.add(v);
		}
	}
	
	public Entry(String key, Comment comment, String ... values) {
		super();
		this.key = key;
		this.comment = comment;
		this.values = new ArrayList<String>();
		for(String v: values){
			this.values.add(v);
		}
	}

	public Entry(String key, char separator, Comment comment, List<String> values) {
		super();
		this.key = key;
		this.comment = comment;
		this.values = values;
		this.separator = separator;
	}

	@Override
	public char getSeparator() {
		return separator;
	}

	@Override
	public Comment getComments() {
		return comment;
	}

	@Override
	public List<String> getValues() {
		return values;
	}

	@Override
	public String getValue() {
		StringBuffer buf = new StringBuffer();
		for(String v: values){
			buf.append(v);
		}
		return buf.toString();
	}

	@Override
	public String getKey() {
		return key;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

}
