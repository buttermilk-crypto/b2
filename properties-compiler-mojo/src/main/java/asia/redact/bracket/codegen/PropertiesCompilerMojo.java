/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.codegen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.io.InputAdapter;


@Mojo( name = "pcompiler", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class PropertiesCompilerMojo extends AbstractMojo {
	
	@Parameter( property = "pcompiler.inputProperties", defaultValue = "${basedir}/src/main/resources/input.properties" )
	private String inputProperties;
	
	@Parameter( property = "pcompiler.targetPackage", defaultValue = "com.example" )
	private String targetPackage;
	
	@Parameter( property = "pcompiler.targetClassname", defaultValue = "CompiledProperties" )
	private String targetClassname;
	
	// must be one of PropertiesImpl or PojoPropertiesImpl
	@Parameter( property = "pcompiler.baseClass", defaultValue = "PropertiesImpl" )
	private String baseClass;
	
	@Parameter(property = "pcompiler.project", defaultValue = "${project}")
	private MavenProject project;

    @Parameter(property="pcompiler.outputDir", defaultValue="${project.build.directory}/src-generated")
	private File outputDir;

	public PropertiesCompilerMojo() {}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		getLog().info( "baseClass:"+baseClass);
		getLog().info( "target class:"+targetPackage+"."+targetClassname);
		getLog().info( "project:"+project);
		getLog().info( "outputDir:"+outputDir);
		getLog().info( "inputProperties:"+inputProperties);
		
		File f = new File(inputProperties);
		if(!f.exists())
			try {
				throw new MojoExecutionException("input properties not found: "+f.getCanonicalPath());
			} catch (IOException e) {}
		InputAdapter in = new InputAdapter();
		try (
				FileInputStream inStream = new FileInputStream(f);
				InputStreamReader reader = new InputStreamReader(inStream, StandardCharsets.US_ASCII);
				BufferedReader breader = new BufferedReader(reader);
		) {
		in.read(breader);
			
		  switch(baseClass){
			case "PropertiesImpl": this.executePropertiesImpl(in.props); break;
			case "PojoPropertiesImpl": this.executePojoPropertiesImpl(in.props); break;
			default: {
				throw new MojoExecutionException("baseClass must be set to one of: PojoPropertiesImpl or PropertiesImpl");
			}
		 }
		  
		}catch(IOException x){
			getLog().error(x);
		}
		
		getLog().info("Adding " + outputDir.getAbsolutePath() + " to compile source root");
		project.addCompileSourceRoot(outputDir.getAbsolutePath());
		    
	}
	
	void executePropertiesImpl(Properties props) throws MojoExecutionException, MojoFailureException {
		CompiledPropsGenerator gen = new CompiledPropsGenerator(props);
		String claZZ = gen.generatePropertiesImpl(targetPackage, targetClassname);
		write(claZZ);
	}
	
	void executePojoPropertiesImpl(Properties props) throws MojoExecutionException, MojoFailureException {
		CompiledPropsGenerator gen = new CompiledPropsGenerator(props);
		String claZZ = gen.generatePojoPropertiesImpl(targetPackage, targetClassname);
		write(claZZ);
	}
	
	// supporting methods
	
	
	private void write(String claZZ) throws MojoExecutionException{
		File parent = pathToFolders();
		File outputFile = new File(parent, targetClassname+".java");
		try {
			Files.write(outputFile.toPath(), claZZ.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new MojoExecutionException("could not write output file");
		}
	}
	
	private File pathToFolders(){
		File parent = new File(outputDir, targetPackage.replaceAll("\\.", "/"));
		parent.mkdirs();
		return parent;
	}

	public String getTargetPackage() {
		return targetPackage;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

	public String getTargetClassname() {
		return targetClassname;
	}

	public void setTargetClassname(String targetClassname) {
		this.targetClassname = targetClassname;
	}

	public MavenProject getProject() {
		return project;
	}

	public void setProject(MavenProject project) {
		this.project = project;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public String getInputProperties() {
		return inputProperties;
	}

	public void setInputProperties(String inputProperties) {
		this.inputProperties = inputProperties;
	}

	public String getBaseClass() {
		return baseClass;
	}

	public void setBaseClass(String baseClass) {
		this.baseClass = baseClass;
	}
	
}
