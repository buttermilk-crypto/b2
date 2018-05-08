/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
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
						new Comparator<CharSequence>() {

							@Override
							public int compare(CharSequence o1, CharSequence o2) {
								
								return o1.toString().compareTo(o2.toString());
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
	
	@Test
	public void trimstest() {
		
		String trims = "/trims/trims.properties";
		
		try (InputStream in = this.getClass().getResourceAsStream(trims);
				InputStreamReader reader = new InputStreamReader(in, StandardCharsets.ISO_8859_1);
				LineScanner scanner = new LineScanner(reader);
		) {
			
			PropertiesParser pp = new PropertiesParser(scanner);
			pp.parse();
			Properties props = pp.getProperties();
			Assert.assertTrue(props.size() == 3);
			// by default keys are trimmed:
			Assert.assertTrue(props.containsKey("key0"));
			Assert.assertTrue(props.containsKey("key1"));
			Assert.assertTrue(props.containsKey("key2"));
			
			Assert.assertTrue(props.get("key0").equals("my value"));
			Assert.assertTrue(props.get("key1").equals("my value"));
			// this one is a continuation - tricky!
			System.err.println(props.get("key2")+"|"); // = my value and a second value
			Assert.assertTrue(props.get("key2").equals("my value and a second value"));

		} catch (IOException x) {
			x.printStackTrace();
		}	
	}

}
