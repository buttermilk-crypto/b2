package asia.redact.bracket.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;

import org.junit.Assert;
import org.junit.Test;

import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.impl.SortedPropertiesImpl;
import asia.redact.bracket.properties.line.LineScanner;

public class ParserTest {

	/**
	 * Very simple properties, validate line ends work
	 */
	@Test
	public void test0() {

		String[] files = { 
				"/crlf/compare.properties",
				"/crlf/compare.mac.properties", 
				"/crlf/compare.unix.properties" 
		};

		for (String f : files) {
			try (InputStream in = this.getClass().getResourceAsStream(f);
					InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
					LineScanner scanner = new LineScanner(reader);
			) {
				
				Properties props = new PropertiesParser(scanner).parse().getProperties();
				Assert.assertTrue(props.size() == 3);

			} catch (IOException x) {
				x.printStackTrace();
			}
		}
	}
	
	/**
	 * More interesting properties
	 */
	@Test
	public void test1() {

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
				Assert.assertTrue(props instanceof PropertiesImpl);
				Assert.assertTrue(props.size() == 3);
				Assert.assertTrue(props.getValues("item1").size() == 3);
				Assert.assertTrue(props.containsKey("item3"));
				Assert.assertTrue(props.getComments("item3").size() == 2);
				Assert.assertTrue(props.containsKey("item0"));
			

			} catch (IOException x) {
				x.printStackTrace();
			}
		}
		
		for (String f : files) {
			try (InputStream in = this.getClass().getResourceAsStream(f);
					InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
					LineScanner scanner = new LineScanner(reader);
			) {
				
				Properties props = new PropertiesParser(scanner).parse(
						new Comparator<String>() {
							@Override
							public int compare(String o1, String o2) {
								return o1.compareTo(o2);
							}
						}
				).getProperties();
				
				Assert.assertTrue(props instanceof SortedPropertiesImpl);
				Assert.assertTrue(props.size() == 3);
				Assert.assertTrue(props.getValues("item1").size() == 3);
				Assert.assertTrue(props.containsKey("item3"));
				Assert.assertTrue(props.getComments("item3").size() == 2);
				Assert.assertTrue(props.containsKey("item0"));
			

			} catch (IOException x) {
				x.printStackTrace();
			}
		}
		
	}
	

}
