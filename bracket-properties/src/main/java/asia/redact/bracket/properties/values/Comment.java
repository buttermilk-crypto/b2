/*
 *  This file is part of Bracket Properties
 *  Copyright 2014-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.values;

import java.util.Iterator;
import java.util.List;

import asia.redact.bracket.properties.io.OutputFormat;

/**
 * Strong typing for comments - helps with structure
 *  
 * @author Dave
 *
 */
public class Comment implements Iterable<String> {

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
		if(!line.trim().startsWith("#")){
			line += "# "+line;
		}
		this.comments=line;
	}
	
	public Comment addLine(String line) {
		if(!line.trim().startsWith("#")){
			line += "# "+line;
		}
		if(comments.length()>0){
			StringBuffer buf = new StringBuffer(comments);
			buf.append(OutputFormat.lineSeparator);
			buf.append(line);
			comments = buf.toString();
		}
		return this;
	}
	
	/**
	 * Number of lines in the comment
	 * 
	 * @return
	 */
	public int size() {
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
		if(comments.length()==0) return new MyIterator(new String[0]);
		return new MyIterator(comments.split("\\n"));
	}
	
	public class MyIterator implements Iterator<String> {
		
		private final String [] comments;
		private int index;

	    public MyIterator(String[] comments) {
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

}
