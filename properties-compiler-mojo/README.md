# b2 (Bracket-Properties 2.0 - Compiled Properties Subproject)

Bracket Properties is a library to work with java(tm) .properties files. It has many features missing from the 
core java(tm) implementation such as retention of order and UTF-8 support. If you can think of something you
wish java(tm) properties files could do better, chances are bracket-properties already has it. 

This subproject is focused on the idea of pre-compiled configurations. It contains a maven plugin which can do
source code generation to build pre-compiled classes containing properties data. No need to load the config from a file!

The mojo output has one runtime dependency, which is bracket properties 2.0 or better. It also requires maven 3 and Java 8.

## Get It

Use this in your pom.xml file and fix up as required:

	<build>
		<plugins>

			<plugin>
				<groupId>asia.redact.bracket.properties</groupId>
				<artifactId>pcompiler-maven-plugin</artifactId>
				<version>1.0.0</version>

             <!-- defaults, change these to match your desired targets -->
				<configuration>
					<targetPackage>com.example</targetPackage>
					<targetClassname>CProperties</targetClassname>
					<inputProperties>${basedir}/src/main/resources/input.properties</inputProperties>
					
					<!-- two possibilities here, choose one -->
					<baseClass>PojoPropertiesImpl</baseClass>
					<!-- <baseClass>PropertiesImpl</baseClass> -->
				</configuration>

				<executions>
					<execution>
						<id>pcompiler</id>
						<goals>
							<goal>pcompiler</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			
			<!-- ... -->
			
		</plugins>
	</build>

## What it does

The Mojo will generate java(tm) source code for one class with a package and class name of your choice. The input is a properties file. Put this input properties file in your src/main/resources folder. Set the configuration as needed, it is fairly self-explanatory.

When the mojo runs, it will create a new source directory in target and that code will become part of your project's jar file when you compile and build your artifacts.

You can examine the source code which was generated in target/src-generated. If you select PropertiesImpl as the baseClass parameter it will look something like this:

     package com.example;

     import asia.redact.bracket.properties.Properties;
     import asia.redact.bracket.properties.impl.PropertiesImpl;
     import asia.redact.bracket.properties.values.BasicValueModel;
     import asia.redact.bracket.properties.values.Comment;

     public final class CProperties extends PropertiesImpl {
      
      public static final long serialVersionUID = 1;

      public CProperties() {
        super(true);
      }

    public Properties init() {
      super.init();
      
      final String [] svar0 = {"value0"};
      map.put("key0", new BasicValueModel(new Comment(""), '=', svar0));
      final String [] svar1 = {"value1"};
      map.put("key1", new BasicValueModel(new Comment(""), '=', svar1));
      final String [] svar2 = {"value2"};
      map.put("key2", new BasicValueModel(new Comment(""), '=', svar2));
      
      return this;
     }
    }
    
For the PojoPropertiesImpl option, the output will look like this:

    package com.example;

    import asia.redact.bracket.properties.impl.PojoPropertiesImpl;
    import asia.redact.bracket.properties.values.Comment;
    import asia.redact.bracket.properties.values.Entry;

    public final class CPropertiesPojo extends PojoPropertiesImpl {
     public static final long serialVersionUID = 1;

     public final Entry key0 = new Entry("key.0", new Comment(""), "value0");
     public final Entry key1 = new Entry("key.1", new Comment(""), "value1");
     public final Entry key2 = new Entry("key.2", new Comment(""), "value2");
     public final Entry key3 = new Entry("key.3", new Comment("#  this is a comment\\r\\n#  with more than one line"), "a value ","which continues...");

    public CPropertiesPojo() {
      super();
    }

    public Properties init() {
     entries = new Entry[4];
     entries[0] = key0;
     entries[1] = key1;
     entries[2] = key2;
     entries[3] = key3;
     return this;
    }
    }
 
 
## Uses

To use the class you will instantiate it directly. Assuming your class name is CProperties and you are extending PropertiesImpl, you would do this:

    Properties props = new CProperties().init();
    
for the PojoPropertiesImpl option, the constructor is similar:

    Properties props = new CPropertiesPojo().init();
    
Or you can use it like this:

     CPropertiesPojo pojo = new CPropertiesPojo().init();
     String value = pojo.key0.getValue();
     Assert.assertEquals(pojo.get("key.0"), value);

All of the Properties interface methods are available, as are the sugars. 

One of the main uses of pre-compiled properties is to not have the issue of late binding on configurations which need to be read from files. This helps prevent any question about inconsistent configurations.

You might say that you can just include any strings or values you need as static variables, but pre-compiled properties have all the advantages of the Properties Interface and associated classes found in bracket-properties for formatting and so on.

 








 
