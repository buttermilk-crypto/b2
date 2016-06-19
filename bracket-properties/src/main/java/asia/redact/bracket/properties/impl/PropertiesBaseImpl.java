/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.impl;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import asia.redact.bracket.properties.adapter.Env;
import asia.redact.bracket.properties.adapter.Ref;
import asia.redact.bracket.properties.adapter.Sugar;

/**
 * general applicability to all the Properties implementation classes
 * 
 * @author Dave
 *
 */
public class PropertiesBaseImpl implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected final Lock lock = new ReentrantLock();
	
	protected Env env;
	protected Ref ref;
	protected Sugar sugar;


}
