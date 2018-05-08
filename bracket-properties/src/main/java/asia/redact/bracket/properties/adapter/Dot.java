/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

import java.util.List;

import asia.redact.bracket.properties.Properties;

/**
 * Support for the "dot" idiom, which is commonly found in properties files. 
 * 
 * @author dave
 *
 */
public interface Dot {
	
	/**
	 * Return a class which implements the Dot interface.
	 * 
	 * @param props
	 * @return
	 */
	public static Dot instance(Properties props) {
		return new DotAdapter(props);
	}

	/**
	 * <p>
	 * Collect all the keys of the form keyBase.[integer]. For example where base is "wrapper.java.classpath":
	 * </p>
	 * 
	  <ol>
	  	<li>wrapper.java.classpath.1=../lib/wrapper.jar</li>
		<li>wrapper.java.classpath.2=../lib/myapp.jar</li>
		<li>wrapper.java.classpath.3=../lib/mysql.jar</li>
	  </ol>
	
	  <p>Then return a list of the keys wrapper.java.classpath.1, wrapper.java.classpath.2, etc.</p>
	 * 
	 * @param keyBase
	 * @return
	 */
	public List<String> getListKeys(String keyBase);

	/**
	 * <pre>
	 * This method relies on the convention of using numbers at
	 * the end of a property key to represent a list member. The total 
	 * list is the set of all similar keys with key as the base
	 * and dot delimited integers at the end. Suppose the following 
	 * (from, e.g., the Tanukisoft wrapper.conf):
	 * 
	 * wrapper.java.classpath.1=../lib/wrapper.jar
	 * wrapper.java.classpath.2=../lib/myapp.jar
	 * wrapper.java.classpath.3=../lib/mysql.jar
	 * wrapper.java.classpath.4=../classes
	 * 
	 * Then calling valueList("wrapper.java.classpath") would
	 * return all the values above, in numeric order, as a List.
	 * 
	 * If key does not exist but numbered properties exist, the
	 * key is synthesized. if no numbered properties exist, an
	 * empty list is returned. If the key does not exist and no numbered
	 * keys exist, the method returns an empty list
	 * 
	 * Numbers need not be sequential
	 *  
	 *  </pre>
	 * @param key
	 * @return
	 */
	public List<String> valueList(String keyBase);
	
	/**
	 * Create a delimited list of the keyset
	 * 
	 * @param keyBase
	 * @param delim
	 * @return
	 */
	public String delimitedList(String keyBase, String delim);
	
	public String dotList(String keyBase);
	
	/**
	 * Generate a classpath appropriate to this OS using values formatted as a delimited list.
	 * 
	 * @param keyBase
	 * @return
	 */
	public String generateClasspath(String keyBase);
	
	/**
	 * Insert a list of values with automatic indexing
	 * 
	 * @param keyBase
	 * @param items
	 */
	public void putList(String keyBase, List<?> items);
	
	
	

}