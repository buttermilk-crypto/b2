# b2 (Bracket-Properties 2.0 - Compiled Properties Subproject)

This subproject is focused on the idea of pre-compiled configurations. It builds a maven plugin which can do
source code generation for pre-compiled classes containing properties data. No need to load the config from a file!

The mojo output has one runtime dependency, which is bracket properties 2.0 or better. It also requires maven 3 and Java 8.

## What It Is

To motivate the idea of pre-compiled properties, think about the scenario in which you have a large, complex,
late-loading application (e.g., Spring) which is having difficulty getting a consistent set of configurations
loaded. This actually happened to me on a job. Compiled properties solves this problem by making the issue
of late-loading moot: it is impossible to screw things up because the configuration is compile-time rather
than run-time. 

Another use-case for pre-compiled properties is when you want to obfuscate or encrypt the configuration (or
parts of it) or you just want the configuration out of sight.  

## PropertiesImpl vs. PojoPropertiesImpl

The Mojo provides two options for superclass: PropertiesImpl and PojoPropertiesImpl. The PropertiesImpl subclass
will be backed by a LinkedHashMap (the default). But the PojoPropertiesImpl subclass will be a true pojo
with code like this:

    public final class CArabicPojo extends PojoPropertiesImpl {

      public static final long serialVersionUID = 1;

      public final Entry line0 = new Entry("line0", 
                    new Comment("# An optional comment"), 
                    "The value of the property (can be multi-line)"
                  );
      public final Entry line1 = new Entry("line1", new Comment("# An optional comment"), "A second value");
      
      public final Entry line2 = ...
  
    }

The name of the instance variable will be the key from the input properties. They key will be munged if required but of course
you should try to use keys which help your cause. 

Additionally the Pojo has a built-in array of references to the keys for easy iteration:

    entries = new Entry[7];
    entries[0] = line0;
    entries[1] = line1;
    entries[2] = line2;
    ...



## Get It

Use this in your pom.xml file and fix up as required:

	<build>
	
	   <dependencies>
	     	<dependency>
			   <groupId>asia.redact.bracket.properties</groupId>
			   <artifactId>bracket-properties</artifactId>
			   <version>2.3.0</version>
		   </dependency>
	   </dependencies>
	   
	   [...]
	
		<plugins>

			<plugin>
				<groupId>asia.redact.bracket.properties</groupId>
				<artifactId>pcompiler-maven-plugin</artifactId>
				<version>1.0.1</version>

             <!-- defaults as below, change these to match your desired targets -->
				<configuration>
					<targetPackage>com.example</targetPackage>
					<targetClassname>CompiledProperties</targetClassname>
					
					<!-- where your input properties file is -->
					<inputProperties>${basedir}/src/main/resources/input.properties</inputProperties>
					
					<!-- if this is set to ISO-8859-1, the input is assumed to be a 
					  legacy Properties file with unicode escapes, which we will parse out -->
					<charsetName>UTF-8</charsetName>
					
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
        init();
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

    Properties props = new CProperties();
    
for the PojoPropertiesImpl option, the constructor is similar:

    Properties props = new CPropertiesPojo();
    
As of version 1.0.1, the init() call is made within the constructor for you. Note that these classes are marked final. 
    
You can use it like this:

     CPropertiesPojo pojo = new CPropertiesPojo();
     
     String value = pojo.key0.getValue(); // instance variable
     String value1 = pojo.get("key0");    // Properties interface
     
     Assert.assertEquals(value, value1);

All of the Properties interface methods are available, as are the other classes in the Bracket packages. 



 








 
