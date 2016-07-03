package asia.redact.bracket.codegen;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.StringEscapeUtils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.ValueModel;

public class CompiledPropsGenerator {

	Properties input;

	public CompiledPropsGenerator(Properties input) {
		this.input = input;
	}

	public void generate(String packageName, String simpleName) {

		ClassName propsClass = ClassName.get("asia.redact.bracket.properties","Properties");

		Builder b = MethodSpec.methodBuilder("init")
				.addModifiers(Modifier.PUBLIC)
				.returns(propsClass);

		b.addStatement("super.init()");

		Map<String, ValueModel> map = input.asMap();
		int count = 0;
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			ValueModel v = map.get(key);
			Comment comments = v.getComments();
			char sep = v.getSeparator();
			List<String> values = v.getValues();
			String svar = "svar" + count;
			String sepStr = "'" + sep + "'";
			b.addStatement("final String [] $L = " + array(values), svar);
			b.addStatement("map.put($S, new BasicValueModel($S, $L, $L)", key,
					escape(comments.comments), sepStr, svar);

			count++;
		}

		b.addStatement("return this");
		MethodSpec initMethod = b.build();

		ClassName implClass = ClassName.get(
				"asia.redact.bracket.properties.impl", "PropertiesImpl");

		TypeSpec tspec = TypeSpec.classBuilder(simpleName)
				.superclass(implClass)
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
				.addMethod(initMethod)
				.build();

		JavaFile javaFile = JavaFile.builder(packageName, tspec)

		.build();

		try {
			javaFile.writeTo(System.out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String array(List<String> values) {
		StringBuffer buf = new StringBuffer();
		buf.append("{");

		for (int i = 0; i < values.size(); i++) {
			buf.append('\"');
			buf.append(escape(values.get(i)));
			buf.append('\"');
			buf.append(',');
		}
		if (values.size() > 0)
			buf.deleteCharAt(buf.length() - 1);

		buf.append("}");
		return buf.toString();
	}

	private String escape(String input) {
		return StringEscapeUtils.escapeJava(input);
	}
}
