/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.impl;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import asia.redact.bracket.properties.Sugar;

/**
 * Constants and regex patterns of general applicability to all the Properties implementation classes
 * 
 * @author Dave
 *
 */
public class PropertiesBaseImpl implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected Pattern dotIntegerPattern = Pattern.compile("\\.(\\d+)");
	protected Pattern dotIdentifierPattern = Pattern.compile("\\.([a-zA-Z]+[a-zA-Z0-9]+)");
	protected Pattern dotKeyValuePattern = Pattern.compile("\\.\\d+\\.[kv]");
	protected static final String REF_TOKEN = "_$";
	
	protected Pattern antStyleVarPattern = Pattern.compile("\\$\\{(.+)\\}");
	
	protected final Lock lock = new ReentrantLock();
	
	protected Sugar sugar;


}
