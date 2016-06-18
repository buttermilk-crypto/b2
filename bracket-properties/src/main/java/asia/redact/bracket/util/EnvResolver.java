package asia.redact.bracket.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/**
 * Collect environment and system properties values for use in variable expansion. Essentially
 * this class collects and exposes the underlying system values (such as they are).
 * 
 * @author Dave
 *
 */
public class EnvResolver {

	Map<String,String> variables;
	
	public final static EnvResolver INSTANCE = new EnvResolver();
	
	private EnvResolver() {
		variables = new HashMap<String,String>();
		Map<String,String> env = System.getenv();
		Iterator<String> iter = env.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			String value = env.get(key);
			variables.put(key.toLowerCase(), value);
		}
		java.util.Properties systemProps = System.getProperties();
		Iterator<Object> iter2 = systemProps.keySet().iterator();
		while(iter2.hasNext()) {
			Object key = iter2.next();
			String value = systemProps.getProperty(String.valueOf(key));
			variables.put(String.valueOf(key).toLowerCase(), value);
		}
	}
	
	public String get(String key) {
		if(!variables.containsKey(key.toLowerCase())) throw new RuntimeException("Missing variable in environment or System props: "+key);
		return variables.get(key.toLowerCase());
	}
}
