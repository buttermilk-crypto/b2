package asia.redact.bracket.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import asia.redact.bracket.properties.io.OutputAdapter;
import asia.redact.bracket.properties.line.LineScanner;

public class FormattingTest {

	@Test
	public void test0(){
		String[] files = {"/sugar/sugar.properties"};

		for (String f : files) {
			try (InputStream in = this.getClass().getResourceAsStream(f);
					InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
					LineScanner scanner = new LineScanner(reader);
			) {
				
				Properties props = new PropertiesParser(scanner).parse().getProperties();
				props.put("test.specialXMLChar.0", ">");
				props.put("test.specialXMLChar.1", "<");
				props.put("test.specialXMLChar.2", "&");
				props.put("test.specialXMLChar.3", "'");
				props.put("test.specialXMLChar.4", "\"");
				System.err.println(OutputAdapter.toXML(props));

			} catch (IOException x) {
				x.printStackTrace();
			}
		}
	}
	
	@Test
	public void test1(){
		String[] files = {"/sugar/sugar.properties"};

		for (String f : files) {
			try (InputStream in = this.getClass().getResourceAsStream(f);
					InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
					LineScanner scanner = new LineScanner(reader);
			) {
				
				Properties props = new PropertiesParser(scanner).parse().getProperties();
				System.err.println(OutputAdapter.toJSON(props));

			} catch (IOException x) {
				x.printStackTrace();
			}
		}
	}

}
