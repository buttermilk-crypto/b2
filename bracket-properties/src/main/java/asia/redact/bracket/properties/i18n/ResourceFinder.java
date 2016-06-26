package asia.redact.bracket.properties.i18n;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.PropertiesParser;
import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.line.LineScanner;

/**
 * <p>
 * Given an absolute path to a folder containing a set of properties files (in resource bundle form) 
 * load the correct one based on the provided locale. 
 * </p>
 * 
 * <p>"path" will be tested to see if a folder on the external file system with this path exists. If
 * it does, we will assume the resources are external. If "path" does not exist externally, we will
 * look for it in the classpath.</p> 
 * 
 * <p>Files with ".utf8" as the file extension are understood as UTF-8 encoded files. Do not use a BOM marker.</p>
 * 
 *  <p>Files with".properties" extension are understood as traditional resource files containing unicode escapes.
 *  These escapes are not processed during the parse - you can convert the encoding using the encoding API.</p>
 * 
 * @author Dave
 */
public class ResourceFinder {

	final Properties props;
	LocalePathBuilder pathBuilder;
	boolean isExternal;
	Charset charset;
	
	/**
	 * Constructor for classpath resource
	 * 
	 * @param path
	 * @param locale
	 * @param fileExt
	 */
	public ResourceFinder(String path, Locale locale, String fileExt) {
		super();
		this.props = new PropertiesImpl(false).init();
		init(path,locale,fileExt);
	}
	
	/**
	 * Constructor for classpath resource where the properties are injected into the provided props instance
	 * 
	 * @param path
	 * @param locale
	 * @param fileExt
	 */
	public ResourceFinder(Properties props, String path, Locale locale, String fileExt) {
		super();
		this.props = props;
		init(path,locale,fileExt);
	}
	
	private void init(String path, Locale locale, String fileExt) {
		pathBuilder = new LocalePathBuilder(path,locale,fileExt);
		File file = new File(path);
		if(file.exists() && file.isDirectory())isExternal = true;
		if(fileExt.contains("utf8"))charset=StandardCharsets.UTF_8;
		else charset = StandardCharsets.ISO_8859_1;
	}

	public Properties locate() {
		
		if(isExternal) return locateExternal();
	
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		
		List<String> list = pathBuilder.getSearchStrings();
		
			for(String item: list){
			 URL url = cl.getResource(item);
			 if(url != null) { // url will be null if resource not found
			   try(
					InputStream in = url.openStream();
					InputStreamReader reader = new InputStreamReader(in, charset);
					BufferedReader breader = new BufferedReader(reader);
					LineScanner scanner = new LineScanner(breader);
				){
					PropertiesParser parser = new PropertiesParser(scanner);
					props.merge(parser.parse().getProperties());
					break;
				}catch(IOException x){
					throw new RuntimeException(x);
				}
			 }
			}
	     
		return props;
	}
	
	private Properties locateExternal() {
		
		List<String> list = pathBuilder.getSearchStrings();
		
		for(String item: list){
			File file = new File(item);
			if(file.exists()) {
			    try(
					FileInputStream in = new FileInputStream(file);
					InputStreamReader reader = new InputStreamReader(in, charset);
					BufferedReader breader = new BufferedReader(reader);
					LineScanner scanner = new LineScanner(breader);
				){
					PropertiesParser parser = new PropertiesParser(scanner);
					props.merge(parser.parse().getProperties());
					break;
				}catch(IOException x){
					throw new RuntimeException(x);
				}
			}
		}
     
	return props;
	}
	
	
}


