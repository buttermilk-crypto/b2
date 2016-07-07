package asia.redact.bracket.properties.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.PropertiesParser;
import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.json.Json;
import asia.redact.bracket.properties.json.JsonObject;
import asia.redact.bracket.properties.json.JsonValue;
import asia.redact.bracket.properties.line.LineScanner;

public class InputAdapter {

	public final Properties props;
	
	public InputAdapter() {
		props = new PropertiesImpl(false).init();
	}
	
	public InputAdapter(boolean concurrent) {
		props = new PropertiesImpl(concurrent).init();
	}

	public InputAdapter(Properties props) {
		super();
		this.props = props;
	}
	
	public void readFile(File path, Charset charset){
		try (
			FileInputStream in = new FileInputStream(path);
			InputStreamReader reader = new InputStreamReader(in, charset);
			BufferedReader breader = new BufferedReader(reader);
			LineScanner scanner = new LineScanner(breader);
		){
			Properties p = new PropertiesParser(scanner).parse().getProperties();
			props.merge(p);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Load from a Java Map<String,?>. String.valueOf() is applied to the value of get(key). 
	 * 
	 * @param map
	 */
	public void readMap(Map<String,?> map){
		map.keySet().forEach(key->{
			props.put(key, String.valueOf(map.get(key)));
		});
	}
	
	/**
	 * Read in from legacy java.util.Properties
	 * 
	 * @param legacy
	 */
	public void readLegacyProperties(java.util.Properties legacy){
		Set<Object> set = legacy.keySet();
		for(Object key: set) {
			String val = legacy.getProperty(String.valueOf(key));
			props.put(String.valueOf(key), val);
		}
	}
	
	/**
	 * Read in from a given URL. Throws a RuntimeException on IOExceptions.
	 * 
	 * @param url
	 */
	public void readURL(URL url) {
		try (
				InputStream in = url.openStream();
				InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
				BufferedReader breader = new BufferedReader(reader);
				LineScanner scanner = new LineScanner(breader);
		){
			Properties p = new PropertiesParser(scanner).parse().getProperties();
			props.merge(p);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Read in a standard (i.e., legacy) properties formatted file.
	 *  
	 * @param reader
	 */
	public void read(Reader reader){
		try (
		 LineScanner scanner = new LineScanner(reader);
		){
			Properties p = new PropertiesParser(scanner).parse().getProperties();
			props.merge(p);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Read in from a stream encoded using the (legacy) Sun DTD.
	 * 
	 * @param reader
	 */
	public void readXML(Reader reader){
		   SAXParserFactory factory = SAXParserFactory.newInstance();
		   factory.setValidating(false);
	        SAXParser parser;
			try {
				parser = factory.newSAXParser();
				InputSource source = new InputSource(reader);
			    parser.parse(source, new BracketPropertiesSAXHandler(props));
			} catch (Exception x){
				x.printStackTrace();
				throw new RuntimeException("Parsing properties failed: "+x.getMessage());
			}
	}
	
	public void readXML(Reader reader, boolean validate){
		   SAXParserFactory factory = SAXParserFactory.newInstance();
		   factory.setValidating(validate);
	        SAXParser parser;
			try {
				parser = factory.newSAXParser();
				InputSource source = new InputSource(reader);
			    parser.parse(source, new BracketPropertiesSAXHandler(props));
			} catch (Exception x){
				x.printStackTrace();
				throw new RuntimeException("Parsing properties failed: "+x.getMessage());
			}
	}
	
	/**
	 * Read in from a JSON-encoded stream.
	 *  
	 * @param in
	 */
	public void readJSON(Reader in){
		JsonValue root;
		try {
			root = Json.parse(in);
			JsonObject obj = root.asObject();
			obj.forEach(item-> {
				props.put(item.getName(), item.getValue().asString());
			});
		} catch (Exception e) {
			throw new RuntimeException("Parsing properties failed: "+e.getMessage());
		}
		
	}

}


class BracketPropertiesSAXHandler extends DefaultHandler {
	
	protected Properties props;
	
	protected String key, value;
	
	public BracketPropertiesSAXHandler(Properties props) {
		this.props = props;
	}
	
	/**
	 * Supply the DTD so the code does not go out and look for it on the URL in the file.
	 */
	public InputSource resolveEntity(String systemId, String publicId) throws SAXException, IOException {
		InputStream in = this.getClass().getResourceAsStream("/xml/props.dtd");
		InputStreamReader reader = new InputStreamReader(in,StandardCharsets.UTF_8);
		return new InputSource(reader);
	}
	
	public void characters(char[] buffer, int start, int length) {
        value = new String(buffer, start, length);
	}

	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		int count = attributes.getLength();
		if(qName.equals("entry") && count > 0){
			key = attributes.getValue("key");
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(key != null)
		props.put(key,value);
	}
	
}
