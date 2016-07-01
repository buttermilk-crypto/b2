package asia.redact.bracket.properties.io;

import java.io.IOException;
import java.io.Reader;
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

	public InputAdapter(Properties props) {
		super();
		this.props = props;
	}
	
	public void readMap(Map<String,?> map){
		map.keySet().forEach(key->{
			props.put(key, String.valueOf(map.get(key)));
		});
	}
	
	public void readLegacyProperties(java.util.Properties legacy){
		Set<Object> set = legacy.keySet();
		for(Object key: set) {
			String val = legacy.getProperty(String.valueOf(key));
			props.put(String.valueOf(key), val);
		}
	}
	
	/**
	 * Read in a standard properties formatted file.
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
	
	public void readXML(Reader reader){
		   SAXParserFactory factory = SAXParserFactory.newInstance();
	        SAXParser parser;
			try {
				parser = factory.newSAXParser();
				InputSource source = new InputSource(reader);
			    parser.parse(source, new BracketPropertiesSAXHandler(props));
			} catch (Exception x){
				throw new RuntimeException("Parsing properties failed: "+x.getMessage());
			}
	}
	
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
	
	String key, value;
	
	public BracketPropertiesSAXHandler(Properties props) {
		this.props = props;
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
