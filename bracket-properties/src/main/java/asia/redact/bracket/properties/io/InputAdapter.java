package asia.redact.bracket.properties.io;

import java.io.IOException;
import java.io.Reader;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.PropertiesParser;
import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.line.LineScanner;

public class InputAdapter {

	Properties props;
	
	public InputAdapter() {
		props = new PropertiesImpl(false).init();
	}

	public InputAdapter(Properties props) {
		super();
		this.props = props;
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
	
	public void readXML(Reader in){
		
	}
	
	public void readJSON(Reader in){
		
	}

}
