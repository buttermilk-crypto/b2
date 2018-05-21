package asia.redact.bracket.properties.adapter;

import java.util.Locale;

import asia.redact.bracket.properties.Properties;

public class I18NAdapter implements I18N {

	Properties props;
	Locale locale;
	
	public I18NAdapter(Properties props) {
		super();
		this.props = props;
		this.locale = Locale.getDefault();
	}

	public I18NAdapter(Properties props, Locale locale) {
		this.props = props;
		this.locale = locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	private String prefix() {
		// en_US_WINDOWS.
		if(hasLanguageCountryVariant()) {
			StringBuilder b = new StringBuilder();
			b.append(locale.getLanguage())
			.append(".")
			.append(locale.getCountry())
			.append(".")
			.append(locale.getVariant())
			.append(".");
			return b.toString();
		// en_US.
		}else if(hasLanguageCountry()) {
			StringBuilder b = new StringBuilder();
			b.append(locale.getLanguage())
			.append(".")
			.append(locale.getCountry())
			.append(".");
			return b.toString();
		// en.
		}else if(hasLanguage()) {
			return locale.getLanguage()+".";
		}
		
		// locale seems bogus, return a sensible default
		return Locale.getDefault().getCountry()+".";
		
	}
	
	private boolean hasLanguageCountryVariant() {
		if(locale.getLanguage().equals("")) return false;
		if(locale.getCountry().equals("")) return false;
		if(locale.getVariant().equals("")) return false;
		return true;
	}
	private boolean hasLanguageCountry() {
		if(locale.getLanguage().equals("")) return false;
		if(locale.getCountry().equals("")) return false;
		return true;
	}
	
	private boolean hasLanguage() {
		if(locale.getLanguage().equals("")) return false;
		return true;
	}

	public String get(String key) {
		return props.get(prefix()+key);
	}

	public String get(String key, String defaultVal) {
		return props.get(prefix()+key, defaultVal);
	}

	public void put(String key, String... values) {
		props.put(prefix()+key, values);
	}

	public boolean containsKey(String key) {
		return props.containsKey(prefix()+key);
	}
	
	
	
}
