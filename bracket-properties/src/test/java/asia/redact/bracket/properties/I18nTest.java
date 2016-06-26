package asia.redact.bracket.properties;

import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

import asia.redact.bracket.properties.i18n.ResourceFinder;
import asia.redact.bracket.properties.i18n.LocalePathBuilder;

public class I18nTest {

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
	}
	
	
}

