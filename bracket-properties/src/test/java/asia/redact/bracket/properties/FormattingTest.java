/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import asia.redact.bracket.properties.io.InputAdapter;
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
				props.put("test.specialXMLChar.0", ">");
				props.put("test.specialXMLChar.1", "<");
				props.put("test.specialXMLChar.2", "&");
				props.put("test.specialXMLChar.3", "'");
				props.put("test.specialXMLChar.4", "\"");
				System.err.println(OutputAdapter.toJSON(props));

			} catch (IOException x) {
				x.printStackTrace();
			}
		}
	}
	
	@Test
	public void test2() {
		InputStream in = this.getClass().getResourceAsStream("/xml/test-legacyDTD.xml");
		Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
		InputAdapter input = new InputAdapter();
		input.readXML(reader, true);
		Assert.assertTrue(input.props.size()==22);
	}
	
	@Test
	public void test3() {
		try (
				InputStream in = this.getClass().getResourceAsStream("/json/testread.json");
				Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
			){
			InputAdapter input = new InputAdapter();
			input.readJSON(reader);
			Assert.assertTrue(input.props.size()==22);
		}catch(IOException x){}
	}

}
