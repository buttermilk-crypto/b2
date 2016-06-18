/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.mgmt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Used to construct localized properties file names from a base name and a
 * Locale. The path is assumed to be on the external file system.
 * 
 * @see LocaleStringBuilder
 * @author Dave
 *
 */
public class ExternalizedLocaleStringBuilder {

	Locale locale;
	String baseName;

	public ExternalizedLocaleStringBuilder(String baseName, Locale locale) {
		super();
		this.locale = locale;

		if (baseName.endsWith(".properties")) {
			this.baseName = baseName.substring(0, baseName.length() - 11);
		} else {
			this.baseName = baseName;
		}
	}

	public List<String> getSearchStrings() {
		ArrayList<String> list = new ArrayList<String>();

		// baseName
		{
			StringBuilder b = new StringBuilder();
			b.append(baseName).append(".properties");
			list.add(b.toString());
		}

		// baseName_en
		if (hasLanguage()) {
			StringBuilder b = new StringBuilder();
			b.append(baseName).append("_").append(locale.getLanguage())
					.append(".properties");
			list.add(b.toString());
		}

		// baseName_en_US
		if (hasLanguageCountry()) {
			StringBuilder b = new StringBuilder();
			b.append(baseName).append("_").append(locale.getLanguage())
					.append("_").append(locale.getCountry())
					.append(".properties");
			list.add(b.toString());
		}

		// baseName_en_US_WINDOWS
		if (hasLanguageCountryVariant()) {
			StringBuilder b = new StringBuilder();
			b.append(baseName).append("_").append(locale.getLanguage())
					.append("_").append(locale.getCountry()).append("_")
					.append(locale.getVariant()).append(".properties");
			list.add(b.toString());
		}

		return list;
	}

	public List<String> getValidPaths() {
		List<String> paths = getSearchStrings();
		List<String> valid = new ArrayList<String>();
		for (String path : paths) {
			File file = new File(path);
			if (file.isFile()) {
				valid.add(path);
			}
		}
		return valid;
	}

	private boolean hasLanguageCountryVariant() {
		if (locale.getLanguage().equals(""))
			return false;
		if (locale.getCountry().equals(""))
			return false;
		if (locale.getVariant().equals(""))
			return false;
		return true;
	}

	private boolean hasLanguageCountry() {
		if (locale.getLanguage().equals(""))
			return false;
		if (locale.getCountry().equals(""))
			return false;
		return true;
	}

	private boolean hasLanguage() {
		if (locale.getLanguage().equals(""))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((baseName == null) ? 0 : baseName.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExternalizedLocaleStringBuilder other = (ExternalizedLocaleStringBuilder) obj;
		if (baseName == null) {
			if (other.baseName != null)
				return false;
		} else if (!baseName.equals(other.baseName))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		return true;
	}

}
