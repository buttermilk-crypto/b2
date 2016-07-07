/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.codegen;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.StringEscapeUtils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.values.BasicValueModel;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.Entry;
import asia.redact.bracket.properties.values.ValueModel;

public class CompiledPropsGenerator {

	final Properties input;

	public CompiledPropsGenerator(Properties input) {
		this.input = input;
	}
	
	public String generatePojoPropertiesImpl(String packageName, String simpleName) {

		ClassName propsClass = ClassName.get("asia.redact.bracket.properties","Properties");
		
		// our abstract base class
		ClassName implClass = ClassName.get("asia.redact.bracket.properties.impl", "PojoPropertiesImpl");
		
		// serial UID
		FieldSpec fieldSpec = FieldSpec.builder(long.class, "serialVersionUID")
				.addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
				.initializer("$L", 1L)
				.build();
		
		// constructor
		MethodSpec cstr = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addStatement("super()")
				.build();

		// here is our input data
		Map<String, ValueModel> map = input.asMap();
		
		// the class
		TypeSpec.Builder cBuilder = TypeSpec.classBuilder(simpleName);
		cBuilder.superclass(implClass);
		cBuilder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
		cBuilder.addField(fieldSpec);
		
		// add fields, one per key
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			ValueModel v = map.get(key);
			Comment comments = v.getComments();
			//char sep = v.getSeparator();
			List<String> values = v.getValues();
			String fieldName = normalizeFieldName(key);
			
			FieldSpec fSpec = FieldSpec.builder(Entry.class, fieldName)
					.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
					.initializer("new $T($S, new $T($S), $L)",  
							Entry.class,
							key,
							Comment.class, 
							escape(comments.comments), 
							escapeValues(values))
					.build();
			cBuilder.addField(fSpec);
		}
		
		cBuilder.addMethod(cstr);
		
		// init method
		MethodSpec.Builder imBuilder = MethodSpec.methodBuilder("init")
				.addModifiers(Modifier.PUBLIC)
				.returns(propsClass);
		
		// initialize entries array 
		imBuilder.addStatement("entries = new $T[$L]", Entry.class, map.size());
		
		// set entries
		iter = map.keySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			String key = iter.next();
			String fieldName = this.normalizeFieldName(key);
			imBuilder.addStatement("entries[$L] = $L", i, fieldName);
			i++;
		}
		
		imBuilder.addStatement("return this");
		MethodSpec initMethod = imBuilder.build();
		
		// add init method to class builder
		cBuilder.addMethod(initMethod);
		
		// build class
		TypeSpec tspec = cBuilder.build();
		JavaFile javaFile = JavaFile.builder(packageName, tspec).build();
		
		// write it to a string
		StringWriter writer = new StringWriter();
		try {
			javaFile.writeTo(writer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return writer.toString();
	}

	public String generatePropertiesImpl(String packageName, String simpleName) {

		ClassName propsClass = ClassName.get("asia.redact.bracket.properties","Properties");
		ClassName implClass = ClassName.get("asia.redact.bracket.properties.impl", "PropertiesImpl");
		
		FieldSpec fieldSpec = FieldSpec.builder(long.class, "serialVersionUID")
				.addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
				.initializer("$L", 1L)
				.build();
		
		MethodSpec cstr = MethodSpec.constructorBuilder()
				.addModifiers(Modifier.PUBLIC)
				.addStatement("super(true)")
				.build();

		// init method
		
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
			String stmt = "final String [] "+svar+" = " + array(values);
		//	System.err.println(stmt);
			
			b.addStatement(stmt);
			b.addStatement("map.put($S, new $T(new $T($S), $L, $L))", 
					key, 
					BasicValueModel.class, 
					Comment.class, 
					escape(comments.comments), 
					sepStr, 
					svar);

			count++;
		}

		b.addStatement("return this");
		MethodSpec initMethod = b.build();

		TypeSpec tspec = TypeSpec.classBuilder(simpleName)
				.superclass(implClass)
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL)
				.addField(fieldSpec)
				.addMethod(cstr)
				.addMethod(initMethod)
				.build();

		JavaFile javaFile = JavaFile.builder(packageName, tspec).build();
		StringWriter writer = new StringWriter();
		try {
			javaFile.writeTo(writer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return writer.toString();
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
		String s = StringEscapeUtils.escapeJava(input);
		// handles the case of ${} which seems to barf ...
		//s = s.replace("%", "%%");
		s = s.replace("$", "$$");
		return s;
	}
	
	private String escapeValues(List<String> values){
		StringBuffer buf = new StringBuffer();
		for(String item: values){
			buf.append('"');
			buf.append(item);
			buf.append('"');
			buf.append(",");
		}
		buf.deleteCharAt(buf.length()-1);
		return buf.toString();
	}
	
	/**
	 * Make a key into a valid java fieldName. 
	 * 
	 * @param key
	 * @return
	 */
	private String normalizeFieldName(String key){
		StringBuffer buf = new StringBuffer();
		
		for(int i = 0;i<key.length();i++){
			char ch = key.charAt(i);
			if(i == 0) {
				if(Character.isJavaIdentifierStart(ch)) { //TODO - unicode?
					buf.append(ch);
				}else{
					buf.append('_');
				}
			}else{
				if(Character.isJavaIdentifierPart(ch)) { //TODO - unicode?
					buf.append(ch);
				}else{
					// do nothing, skip
				}
			}
		}
		
		return buf.toString();
	}
}
