/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

import asia.redact.bracket.properties.i18n.ResourceFinder;
import asia.redact.bracket.properties.i18n.LocalePathBuilder;
import asia.redact.bracket.properties.io.AsciiOutputFormat;
import asia.redact.bracket.properties.io.InputAdapter;
import asia.redact.bracket.properties.io.OutputAdapter;

public class I18nTest {
	
	
	@Test
	public void testUTF8Load() {
		File propsFile = new File("src/test/resources/ibmresbundle/app_ja.utf8");
		Assert.assertTrue(propsFile.exists());
		InputAdapter in = new InputAdapter();
		in.readFile(propsFile, StandardCharsets.UTF_8);
		System.err.println(OutputAdapter.toString(in.props));
		
		StringWriter writer = new StringWriter();
		try {
			new OutputAdapter(in.props).writeTo(writer, new AsciiOutputFormat());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(writer.toString());
		
		// clone to an ASCII escaped version
		Properties asciiEncoded = in.props.nativeToAscii();
		System.err.println(asciiEncoded.toString());
		
		// read an explicitly escaped legacy style properties file
		propsFile = new File("src/test/resources/ibmresbundle/app_ja.properties");
		Assert.assertTrue(propsFile.exists());
		in = new InputAdapter();
		in.readFile(propsFile, StandardCharsets.ISO_8859_1);
		System.err.println(in.props.toString());
		// clone to native (UTF-8)
		Properties nativeEncoded = in.props.asciiToNative();
		System.err.println(OutputAdapter.toString(nativeEncoded));
	}

	@Test
	public void test0() {
		LocalePathBuilder builder = new LocalePathBuilder("ibmresbundle/app", Locale.JAPANESE, ".properties");
		List<String> strings = builder.getSearchStrings();
		strings.forEach(item->{
			System.out.println(item);
		});
		
		Assert.assertEquals(2, strings.size());
		Assert.assertTrue(strings.get(0).length()>strings.get(1).length());
	}
	
	@Test
	public void test1() {
		
		// legacy encoding approach
		ResourceFinder rf = new ResourceFinder("ibmresbundle/app", Locale.JAPANESE, ".properties");
		Properties props = rf.locate();
		Assert.assertNotNull(props);
		String email = props.get("email"); // still in unicode escape format
		Assert.assertEquals("E\\u30e1\\u30fc\\u30eb", email);
		
		//to make them usable, encode Properties to UTF-8 with asciiToNative()
		Properties nativeImpl = props.asciiToNative(); // makes a clone
		Assert.assertNotNull(nativeImpl);
		email = nativeImpl.get("email"); // UTF-8 encoded cloned value
		Assert.assertEquals("Eメール", email);
				
			
		
	}
	
	@Test
	public void test2() {
			ResourceFinder rf = new ResourceFinder("ibmresbundle/app", Locale.JAPANESE, ".utf8");
			Properties props = rf.locate();
			Assert.assertNotNull(props);
			String email = props.get("email"); // UTF-8 encoded
			Assert.assertEquals("Eメール", email);
			
			rf = new ResourceFinder("ibmresbundle/app", Locale.KOREAN, ".utf8");
			props = rf.locate();
			email = props.get("email"); // UTF-8 encoded
			Assert.assertEquals("이메일", email);
	}
	
	
}

