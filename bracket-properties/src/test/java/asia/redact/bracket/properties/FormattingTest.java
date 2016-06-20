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
		String[] files = { 
				"/crlf/test.properties",
				"/crlf/test.mac.properties", 
				"/crlf/test.unix.properties" 
		};

		for (String f : files) {
			try (InputStream in = this.getClass().getResourceAsStream(f);
					InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
					LineScanner scanner = new LineScanner(reader);
			) {
				
				Properties props = new PropertiesParser(scanner).parse().getProperties();
				System.err.println(OutputAdapter.toXML(props));

			} catch (IOException x) {
				x.printStackTrace();
			}
		}
	}

}
