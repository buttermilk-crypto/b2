/*
 *  This file is part of Bracket Properties
 *  Copyright 2014-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.values;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import asia.redact.bracket.properties.io.OutputFormat;

/**
 * Strong typing for comments. Comment is a string consisting 
 * of lines, the #  or ! is retained and added if not found
 *  
 * @author Dave
 *
 */
public class Comment implements Serializable, Iterable<String> {

	private static final long serialVersionUID = 1L;
	
	public String comments; // one or more comment lines.
	
	public Comment() {
		this.comments="";
	}
	
	/**
	 * Adds a # to the front of the string if not found
	 * 
	 * @param comments
	 */
	public Comment(String line) {
		if(!(line.trim().startsWith("#") || line.trim().startsWith("!"))){
			line += "# "+line;
		}
		this.comments=line;
	}
	
	public Comment addLine(String line) {
		StringBuffer buf = new StringBuffer(comments);
		if(buf.length()>0) buf.append(OutputFormat.lineSeparator);
		if(!(line.trim().startsWith("#") || line.trim().startsWith("!"))){
			buf.append("# ");
		}
		buf.append(line);
		comments = buf.toString();
		
		return this;
	}
	
	/**
	 * Number of lines in the comment
	 * 
	 * @return
	 */
	public int size() {
		if(comments.length()==0) return 0;
		return comments.split("\\n").length;
	}
	
	public void clear() {
		comments = "";
	}
	
	public void addAll(List<String> list){
		for(String line: list){
			this.addLine(line);
		}
	}

	@Override
	public Iterator<String> iterator() {
		if(comments.length()==0) return new Iter(new String[0]);
		return new Iter(comments.split("\\n"));
	}
	
	private class Iter implements Iterator<String> {
		
		private final String [] comments;
		private int index;

	    public Iter(String[] comments) {
			super();
			this.comments = comments;
			index = 0;
		}

		public boolean hasNext() {
			return index < comments.length;
	    }

	    public String next() {
	    	int i = index;
	    	index++;
	       return comments[i];
	    }

	    public void remove() {
	       throw new UnsupportedOperationException();
	    }
	}
	
	public boolean containsUnicodeEscape() {
		return comments.contains("\\u");
	}

}
