package asia.redact.bracket.codegen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import asia.redact.bracket.codegen.CompiledPropsGenerator;
import asia.redact.bracket.properties.io.InputAdapter;

public class CompileGeneratorTest {

	@Test
	public void test0() {
		
		InputAdapter in = new InputAdapter();
		try (
				FileInputStream fin = new FileInputStream(new File("src/test/resources/log4j.properties"));
				InputStreamReader reader = new InputStreamReader(fin, StandardCharsets.US_ASCII);
		) {
			in.read(reader);
			CompiledPropsGenerator gen = new CompiledPropsGenerator(in.props);
			gen.generatePropertiesImpl("com.props.project", "CProperties");
			gen.generatePojoPropertiesImpl("com.props.project", "CPropertiesPojo");
		}catch(IOException x){}
	}

}
