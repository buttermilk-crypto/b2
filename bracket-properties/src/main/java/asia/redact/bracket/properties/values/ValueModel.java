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
 * @see UnsettableValueModel
 */

public interface ValueModel extends Serializable {

	public char getSeparator();

	public List<String> getComments();

	public List<String> getValues();

	/**
	 * Concatenates the values
	 * 
	 */
	public String getValue();
	
	/**
	 * Output a reasonable representation of what the text for this key value pair would look like
	 * 
	 */
	public String asKeyValueRep(String key);
	
	public ValueModel cloneImmutable();

}