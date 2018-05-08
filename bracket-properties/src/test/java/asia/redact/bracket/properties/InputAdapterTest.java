package asia.redact.bracket.properties;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import asia.redact.bracket.properties.io.InputAdapter;

import junit.framework.Assert;

public class InputAdapterTest {
	

	@Test
	public void test0() {
		
		// read UTF-8 
		InputAdapter ia = new InputAdapter();
		String path = "src/test/resources/icu4j/Arabic.out.properties";
		ia.readFile(new File(path), StandardCharsets.UTF_8);
		
		Properties props = ia.props;
		
		// read unicode-escaped version - should convert it to native
		ia = new InputAdapter();
		path = "src/test/resources/icu4j/Arabic.out.ascii.properties";
		ia.readFile(new File(path), StandardCharsets.ISO_8859_1);
		
		Properties nProps = ia.props;
		
		Assert.assertEquals(props.get("line0"), nProps.get("line0"));
		
		
	}
}
