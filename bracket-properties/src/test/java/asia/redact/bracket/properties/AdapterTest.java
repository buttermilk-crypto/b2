package asia.redact.bracket.properties;

import java.util.List;

import org.junit.Test;

import asia.redact.bracket.properties.adapter.Alias;
import asia.redact.bracket.properties.adapter.Dot;
import asia.redact.bracket.properties.adapter.Sed;
import junit.framework.Assert;

public class AdapterTest {

	  @Test
	  public void test0() {
		  Properties props = Properties.instance();
		  props.put("key.first", "a value");
		  Alias a = Alias.instance(props);
		  a.putKeyRef("key.second", "key.first");
		  Assert.assertTrue(a.getKeyRef("key.second").equals("a value"));
		  
		  Dot dot = Dot.instance(props);
		  
		  props.put("wrapper.java.classpath.1", "../lib/wrapper.jar");
		  props.put("wrapper.java.classpath.2", "../lib/myapp.jar");
		  props.put("wrapper.java.classpath.3", "../lib/mysql.jar");
		 
		  List<String> list = dot.valueList("wrapper.java.classpath");
		  Assert.assertTrue(list.size()==3);
		  
		  String delim = dot.delimitedList("wrapper.java.classpath", ":");
		  Assert.assertEquals("../lib/wrapper.jar:../lib/myapp.jar:../lib/mysql.jar", delim);
					
		  Sed s = Sed.instance(props);
		  s.replaceAll("wrapper.java.classpath", "lib", "usr/lib");
		  
	  }
}
