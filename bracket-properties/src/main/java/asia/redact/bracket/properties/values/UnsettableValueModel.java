/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith
 *
 */
package asia.redact.bracket.properties.values;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A ValueModel in which the internals cannot be changed once the constructor is called.</p>
 * 
 * @author Dave
 *
 */
public class UnsettableValueModel extends BasicValueModel {

	
	private static final long serialVersionUID = 1L;

	public UnsettableValueModel(List<String> comments, List<String> values) {
		super(comments, values);
	}

	public UnsettableValueModel(String... value) {
		super(value);
	}

	public char getSeparator() {
		return separator;
	}

	/**
	 * Return a copy only, do not expose our internals
	 */
	public List<String> getComments() {
		
		List<String> copy = new ArrayList<String>();
		copy.addAll(comments);
		return copy;
	}

	public List<String> getValues() {
		List<String> copy = new ArrayList<String>();
		copy.addAll(values);
		return copy;
	}
	
	public void setSeparator(char separator) {
		throw new UnsupportedOperationException("Cannot set separator, immutable");
	}
	
	public void addValue(String value){
		throw new UnsupportedOperationException("Cannot add value, immutable");
	}
	
	public void addComment(String comment){
		throw new UnsupportedOperationException("Cannot add comment, immutable");
	}
	
	public void clearComments() {
		throw new UnsupportedOperationException("Cannot clear comments, immutable");
	}

}
