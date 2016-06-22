/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.ref;

/**
 * <table>
 * <tr><td>EXTERNAL</td><td>Used with paths on the OS, such as /home/users/dsmith/my.properties</td></tr>
 * <tr><td>CLASSLOADED</td><td>Used with paths on the java classpath, such as /my.properties</td></tr>
 * <tr><td>DIRECT</td><td>Used with properties file formatted Strings, "mykey=val1\nkey2=val2"</td></tr>
 * <tr><td>COMMANDLINE_OVERRIDE</td><td>Used to indicate a single property we want to look for on the command line, such as "key1"</td></tr>
 * </table>
 * 
 * @author Dave
 *
 */
public enum ReferenceType {
		EXTERNAL,
		CLASSLOADED,
		DIRECT,
		COMMANDLINE_OVERRIDE;
}
