package asia.redact.bracket.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import asia.redact.bracket.properties.impl.PropertiesImpl;
import asia.redact.bracket.properties.io.OutputAdapter;
import asia.redact.bracket.properties.io.PlainOutputFormat;
import asia.redact.bracket.properties.ref.LoadList;
import asia.redact.bracket.properties.ref.PropertiesReference;
import asia.redact.bracket.properties.ref.ReferenceType;

public class RefTest {

	@Test
	public void test0() {
		try {
			// write out some temp properties as externals. 
			
			File tmpExternal = File.createTempFile("tempprop", "properties");
			Properties props = new PropertiesImpl(false).init();
			props.put("item.0", "my item");
			props.put("item.1", "my item2");
			props.put("item.2", "my item3");
			//override item
			props.put("item0", "default");
			FileOutputStream out = new FileOutputStream(tmpExternal);
			new OutputAdapter(props).writeTo(out, new PlainOutputFormat(), StandardCharsets.UTF_8);
			
			PropertiesReference externalRef = new PropertiesReference(tmpExternal, StandardCharsets.UTF_8);
			PropertiesReference classpathRef = new PropertiesReference(ReferenceType.CLASSLOADED, "/crlf/test.properties");
			Properties result = new LoadList()
				.addReference(externalRef)
				.addReference(classpathRef)
				.load()
				.getProps();
			Assert.assertEquals(6,result.size()); // override must have occurred
			Assert.assertEquals("This value", result.get("item0")); // override value
			
			tmpExternal.deleteOnExit();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
