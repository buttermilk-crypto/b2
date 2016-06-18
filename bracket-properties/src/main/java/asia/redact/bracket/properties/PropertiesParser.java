/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties;

import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.line.Line;
import asia.redact.bracket.properties.line.LineScanner;
import asia.redact.bracket.properties.values.BasicValueModel;

public class PropertiesParser {

	final LineScanner scanner;
	final Properties props;
	ModelType modelType;

	/**
	 * Defaults to PropertiesImpl
	 * @param scanner
	 */
	public PropertiesParser(LineScanner scanner) {
		this.scanner = scanner;
		props = new PropertiesImpl();
	}

	/**
	 * Use this constructor to load the SortedPropertiesImpl or ImmutablePropertiesImpl. 
	 * @param scanner
	 * @param propsInstance
	 */
	public PropertiesParser(LineScanner scanner, Properties propsInstance) {
		this.scanner = scanner;
		props = propsInstance;
	}

	public void parse() {

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
	}

	public Properties getProperties() {
		return props;
	}
	
	public enum ModelType {
		Basic, Unsettable, Immutable, Entry;
	}

	public void setModelType(ModelType modelType) {
		this.modelType = modelType;
	}

}
