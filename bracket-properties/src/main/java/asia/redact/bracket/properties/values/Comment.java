/*
 *  This file is part of Bracket Properties
 *  Copyright 2014-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.values;

/**
 * Used for programmatic comment support
 * 
 * @author Dave
 *
 */
public class Comment {

	public final String comment;
	
	public Comment() {
		this.comment="";
	}
	
	/**
	 * Adds a # to the front of the string if not found
	 * 
	 * @param comment
	 */
	public Comment(String comment) {
		if(!comment.trim().startsWith("#")){
			comment += "# "+comment;
		}
		this.comment=comment;
	}

}
