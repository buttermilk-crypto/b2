package asia.redact.bracket.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import asia.redact.bracket.codegen.CompiledPropsGenerator;
import asia.redact.bracket.properties.line.LineScanner;

public class CompileGeneratorTest {

	@Test
	public void test0() {
		
		String f = "/crlf/compare.properties";
		
		Properties props = null;
		
		try (InputStream in = this.getClass().getResourceAsStream(f);
				InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
				LineScanner scanner = new LineScanner(reader);
		) {
			
			props = new PropertiesParser(scanner).parse().getProperties();
			Assert.assertTrue(props.size() == 3);

		} catch (IOException x) {
			x.printStackTrace();
		}
		
		CompiledPropsGenerator gen = new CompiledPropsGenerator(props);
		gen.generate("com.props.project", "CProperties");
	}

}
