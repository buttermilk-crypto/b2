/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties;

import java.util.Comparator;

import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.impl.SortedPropertiesImpl;
import asia.redact.bracket.properties.line.Line;
import asia.redact.bracket.properties.line.LineScanner;
import asia.redact.bracket.properties.values.BasicValueModel;

public class PropertiesParser {
	

	final LineScanner scanner;
	Properties props;
	boolean concurrent;
	int options;

	public PropertiesParser(LineScanner scanner) {
		this.scanner = scanner;
		this.concurrent = false;
		this.options = 0;
	}
	
	public PropertiesParser(LineScanner scanner, int options) {
		this.scanner = scanner;
		this.concurrent = false;
		this.options = options;
	}
	
	public PropertiesParser(LineScanner scanner, boolean concurrent, int options) {
		this.scanner = scanner;
		this.concurrent = concurrent;
		this.options = options;
	}
	
	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}
	
	/**
	 * populates a SortedPropertiesImpl (which only makes sense if you need something other than insert-order ordering)
	 * 
	 * @param comparator
	 * @return
	 */
	public PropertiesParser parse(Comparator<String> comparator) {
		
		props = new SortedPropertiesImpl(concurrent, comparator).init();
		
		return parse();
	}

	/**
	 * populates a PropertiesImpl object. If you have previously set concurrent to true, the
	 * result will be wrapped in a Concurrent wrapper
	 * 
	 * @return
	 */
	public PropertiesParser parse() {
		
		if(props == null) props = new PropertiesImpl(concurrent).init();

		Line line = null;
		String key = null;
		BasicValueModel model = new BasicValueModel();
		boolean hasContinuation = false;
		while ((line = scanner.line()) != null) {
			if (hasContinuation) { // previous line has continuation
				model.addValue(line.logicalLineContents()); 
				if (line.hasContinuation()) {
					continue; 								
				} else {
					hasContinuation = false; 				
					// Issue 8 fix
					props.put(key, model);
					key = null;
					model = new BasicValueModel();
					continue;
				}
			}
			if (line.isEmptyLine())
				continue;
			if (line.isPrivateComment())
				continue;
			if (line.isCommentLine()) {
				if (key != null) {
					props.put(key, model);
					key = null;
					model = new BasicValueModel();
				}
				model.addComment(line.commentContents());
				continue;
			} else if (line.isNaturalLine()) {
				if (key != null) {
					props.put(key, model);
					key = null;
					model = new BasicValueModel();
				}
				String[] parts = line.naturalLineContents();
				key = parts[0];
				model.setSeparator(parts[1].charAt(0));
				hasContinuation = line.hasContinuation();
				model.addValue(parts[2]);
			}
		}
		// last one
		if (key != null) {
			props.put(key, model);
		}
		
		return this;
	}

	public Properties getProperties() {
		return props;
	}
	
}
