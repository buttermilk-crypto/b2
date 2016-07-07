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
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import asia.redact.bracket.properties.adapter.Dot;
import asia.redact.bracket.properties.adapter.Env;
import asia.redact.bracket.properties.adapter.Quote;
import asia.redact.bracket.properties.adapter.Sec;
import asia.redact.bracket.properties.adapter.Types;
import asia.redact.bracket.properties.line.LineScanner;

public class SugarTest {

	@Test
	public void test0(){
		
		Properties props = null;
		
		try (InputStream in = this.getClass().getResourceAsStream("/sugar/sugar.properties");
				InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
				LineScanner scanner = new LineScanner(reader);
		) {
			
			props = new PropertiesParser(scanner).parse().getProperties();

		} catch (IOException x) {
			x.printStackTrace();
		}
		
		Types t = props.sugar().types();
		t.stringValue("test.s1");
		t.passwordValue("test.password1");
		t.intValue("test.int1");
		t.booleanValue("test.bool1"); // test.bool1=enabled
		t.longValue("test.long1");
		Date d = t.dateValue("test.date1");
		List<String> list1 = t.listValue("test.list1");
		Assert.assertEquals(5, list1.size());
		
		Env env = props.sugar().env();
		String home = env.resolve("home");
		System.err.println(home);
		String java_home = env.resolve("home.jh");
		System.err.println(java_home);
		
		Sec ob = props.sugar().sec();
		ob.deobfuscate("password");
		String pass = props.get("password");
		System.err.println(pass);
		
		Dot dot = props.sugar().dot();
		String classpath = dot.generateClasspath("wrapper.java.classpath");
		System.err.println(classpath);
		
		Quote q = props.sugar().quote();
		q.curly("test.s1");
		System.err.println(props.get("test.s1"));
		
	}

}
