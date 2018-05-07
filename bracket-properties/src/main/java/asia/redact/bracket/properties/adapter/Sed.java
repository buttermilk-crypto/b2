package asia.redact.bracket.properties.adapter;

import asia.redact.bracket.properties.Properties;

/**
 * Search and replace values
 * 
 * @author dave
 *
 */
public interface Sed {

	public void replace(String key, String pattern, String replacement);
	public void replaceAll(String keyBase, String pattern, String replacement);
	public void replaceAll(String pattern, String replacement);
	
	public static Sed instance(Properties props) {
		return new SedAdapter(props);
	}
}
