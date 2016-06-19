package asia.redact.bracket.properties.adapter;

import java.util.regex.Pattern;

/**
 * Addon for simple variable substitution 
 * 
 * @author Dave
 *
 */
public interface Env {

	static Pattern dotIntegerPattern = Pattern.compile("\\.(\\d+)");
	static Pattern dotIdentifierPattern = Pattern.compile("\\.([a-zA-Z]+[a-zA-Z0-9]+)");
	static Pattern dotKeyValuePattern = Pattern.compile("\\.\\d+\\.[kv]");
	static Pattern antStyleVarPattern = Pattern.compile("\\$\\{(.+)\\}");
	
	public String resolve(String key);
	
}
