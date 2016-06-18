/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.mgmt;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * 
 * <p>Data type to represent one of:</p>
 * 
 * <table>
 * <tr>
 * <td>
 * ReferenceType.EXTERNAL, in which case data is understood to be an Operating system path to a properties file
 * </td>
 * </tr>
 * <tr>
 * <td>
 * ReferenceType.CLASSLOADED, in which case data is understood as a path to a class loadable properties file
 * </td>
 * </tr>
 * <tr>
 * <td>
 * ReferenceType.DIRECT, in which case data is understood as raw properties name value pairs to be parsed as such
 * </td>
 * </tr>
 * <tr>
 * <td>
 * ReferenceType.COMMANDLINE_OVERRIDE in which case data is the name of a property to be searched for from the command line 
 * </td>
 * </tr>
 *  <tr>
 * <td>
 * ReferenceType.OBFUSCATED - ALL the properties in the file will be deobfuscated. 
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
	
	
	public PropertiesReference(ReferenceType type, String data) {
		super();
		this.type = type;
		this.data = data;
	}
	
	public PropertiesReference(ReferenceType type, File data) {
		super();
		this.type = type;
		try {
			this.data = data.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
