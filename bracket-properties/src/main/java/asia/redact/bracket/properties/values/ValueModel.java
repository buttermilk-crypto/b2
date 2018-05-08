/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith
 *
 */
package asia.redact.bracket.properties.values;

import java.io.Serializable;
import java.util.List;

/**
 * As of version 1.3.2, ValueModel is an interface. 
 * 
 * @author Dave
 * @see BasicValueModel
 * @see KeyValueModel
 */

public interface ValueModel extends Serializable {

	public char getSeparator();

	public Comment getComments();

	public List<String> getValues();

	/**
	 * Concatenates the values
	 * 
	 */
	public String getValue();
	
	/**
	 * Support to see if we contain any unicode escapes
	 * @return
	 */
	public boolean containsUnicodeEscape();

}