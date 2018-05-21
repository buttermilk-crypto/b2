package asia.redact.bracket.properties.adapter;

import java.util.Locale;

import asia.redact.bracket.properties.Properties;

/**
 * Alternate scheme for I18N. 
 * 
 * @author dave
 *
 */
public interface I18N {

	public static I18N instance(Properties props, Locale locale) {
		return new I18NAdapter(props, locale);
	}

	public String get(String key);

	public String get(String key, String defaultVal);

	public void put(String key, String... values);

	public boolean containsKey(String key);
}
