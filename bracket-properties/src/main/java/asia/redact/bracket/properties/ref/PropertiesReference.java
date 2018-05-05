/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.ref;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 
 * <p>Data type to represent one of:</p>
 * 
 * <table>
 * <tr>
 * <td>
 * ReferenceType.EXTERNAL, in which case data is understood to be an Operating system path to a 
 * properties file
 * </td>
 * </tr>
 * <tr>
 * <td>
 * ReferenceType.CLASSLOADED, in which case data is understood as a path to a class 
 * loadable properties file
 * </td>
 * </tr>
 * <tr>
 * <td>
 * ReferenceType.DIRECT, in which case data is understood as raw properties name-value 
 * pairs to be parsed as such
 * </td>
 * </tr>
 * <tr>
 * <td>
 * ReferenceType.COMMANDLINE_OVERRIDE in which case data is the name of a property to be 
 * searched for from the command line 
 * </td>
 * </tr>
 * </table>
 * 
 * @author Dave
 *
 */
public class PropertiesReference implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public final ReferenceType type;
	public final String data;
	public final Charset charset;
	
	/**
	 * Assume UTF-8 as a default
	 * @param type
	 * @param data
	 */
	public PropertiesReference(ReferenceType type, String data) {
		super();
		this.type = type;
		this.data = data;
		this.charset = StandardCharsets.UTF_8;
	}
	
	public PropertiesReference(ReferenceType type, String data, Charset charset) {
		super();
		this.type = type;
		this.data = data;
		this.charset = charset;
	}
	
	public PropertiesReference(File data, Charset charset) {
		super();
		this.type = ReferenceType.EXTERNAL;
		this.charset = charset;
		try {
			this.data = data.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
