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

	public PropertiesParser(LineScanner scanner) {
		this.scanner = scanner;
		this.concurrent = false;
	}
	
	public PropertiesParser(LineScanner scanner, boolean concurrent) {
		this.scanner = scanner;
		this.concurrent = concurrent;
	}
	
	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}
	
	/**
	 * populates a SortedPropertiesImpl (which only makes sense if you need other than insert-order ordering)
	 * 
	 * @param comparator
	 * @return
	 */
	public PropertiesParser parse(Comparator<String> comparator) {
		
		props = new SortedPropertiesImpl(concurrent, comparator).init();
		
		return parse();
	}

	/**
	 * populates a PropertiesImpl object
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
				model.addValue(line.logicalLineContents()); // so collect the current line
				if (line.hasContinuation()) { 				// if current line has continuation
					continue; 								// then continue our tight collection loop
				} else {
					hasContinuation = false; 				// the continuation has ended
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
