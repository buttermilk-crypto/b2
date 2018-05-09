package asia.redact.bracket.codegen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.Test;

import com.props.project.CArabic;
import com.props.project.CArabicPojo;

import asia.redact.bracket.codegen.CompiledPropsGenerator;
import asia.redact.bracket.properties.io.InputAdapter;
import junit.framework.Assert;

public class CompileGeneratorTest {

	@Test
	public void test0() {
		
		InputAdapter in = new InputAdapter();
		try (
				FileInputStream fin = new FileInputStream(new File("src/test/resources/Arabic.out.properties"));
				InputStreamReader reader = new InputStreamReader(fin, StandardCharsets.UTF_8);
		) {
			in.read(reader);
			CompiledPropsGenerator gen = new CompiledPropsGenerator(in.props);
			String cPropsClass = gen.generatePropertiesImpl("com.props.project", "CArabic");
			String cPropsPojoClass = gen.generatePojoPropertiesImpl("com.props.project", "CArabicPojo");
			
			Files.write(new File("src/test/java/com/props/project/CArabic.java").toPath(), cPropsClass.getBytes("UTF-8"));
			Files.write(new File("src/test/java/com/props/project/CArabicPojo.java").toPath(), cPropsPojoClass.getBytes("UTF-8"));
			
		}catch(IOException x){}
	}
	
	@Test
	public void test1() {
		CArabic cl  = new CArabic();
	//	System.err.println(cl.get("line0"));
		
		CArabicPojo pojo = new CArabicPojo();
	//	System.err.println(pojo.line0.getValue());
		
		Assert.assertEquals(cl.get("line0"), pojo.line0.getValue());
	}

}
