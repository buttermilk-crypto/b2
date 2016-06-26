package asia.redact.bracket.properties;

import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

import asia.redact.bracket.properties.i18n.ResourceFinder;
import asia.redact.bracket.properties.i18n.LocalePathBuilder;
import asia.redact.bracket.properties.io.AsciiToNativeFilter;

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
		
		// as classpath, ascii
		ResourceFinder rf = new ResourceFinder("ibmresbundle/app", Locale.JAPANESE, ".properties");
		Properties props = rf.locate();
		Assert.assertNotNull(props);
		System.err.println(props.get("email").length());
		
		// as classpath, utf8 encoded
		rf = new ResourceFinder("ibmresbundle/app", Locale.JAPANESE, ".utf8");
		props = rf.locate();
		Assert.assertNotNull(props);
		AsciiToNativeFilter filter = new AsciiToNativeFilter("\u96fb\u5b50\u30e1\u30fc\u30eb");
		Assert.assertTrue(props.get("email").equals(filter.read()));
		
		// as external file in the project
		//rf = new ResourceFinder("src/test/java/ibmresbundle/app", Locale.JAPANESE, ".utf8");
	//	props = rf.locate();
		//Assert.assertNotNull(props);
		
	}
}

