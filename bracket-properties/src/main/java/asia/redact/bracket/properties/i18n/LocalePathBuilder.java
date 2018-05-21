/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * Support for locating legacy ResourceBundle files
 * 
 * @author Dave
 *
 */
public class LocalePathBuilder {
	
	final Locale locale;
	final String path;
	final String ext;
	
	/**
	 * For example, ("/ibmresbundle/app", Locale.JAPAN, ".properties")
	 * @param path
	 * @param locale
	 * @param fileExtension
	 */
	public LocalePathBuilder(String path, Locale locale, String fileExtension) {
		super();
		this.locale = locale;
		this.path = path;
		this.ext = fileExtension;
	}
	
	public List<String> getSearchStrings() {
		ArrayList<String> list = new ArrayList<String>();
		
		//baseName_en_US_WINDOWS
		if(hasLanguageCountryVariant()) {
			StringBuilder b = new StringBuilder();
			b.append(path)
			.append("_")
			.append(locale.getLanguage())
			.append("_")
			.append(locale.getCountry())
			.append("_")
			.append(locale.getVariant())
			.append(ext);
			list.add(b.toString());
		}
		
		//baseName_en_US
		if(hasLanguageCountry()) {
			StringBuilder b = new StringBuilder();
			b.append(path)
			.append("_")
			.append(locale.getLanguage())
			.append("_")
			.append(locale.getCountry())
			.append(ext);
			list.add(b.toString());
		}
		
		//baseName_en
			if(hasLanguage()) {
				StringBuilder b = new StringBuilder();
				b.append(path)
				.append("_")
				.append(locale.getLanguage())
				.append(ext);
				list.add(b.toString());
			}
		
		//baseName
		{
			StringBuilder b = new StringBuilder();
			b.append(path)
			.append(ext);
			list.add(b.toString());
		}
		
		return list;
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

}
