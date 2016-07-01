# b2 (Bracket-Properties 2.0)

Bracket Properties is a library to work with java .properties files. It has many features missing from the 
core java implementation such as retention of order and UTF-8 support. If you can think of something you
wish java properties files could do better, chances are bracket-properties already has it. 

This version is a complete rewrite and the API has changed quite a bit from 1.x.

## Get It

For maven, use:

	<dependency>
	    <groupId>asia.redact.bracket.properties</groupId>
	    <artifactId>bracket-properties</artifactId>
	    <version>2.0.0</version>
	</dependency>

For ant, download the bundle from [my web site] (https://www.cryptoregistry.com/downloads/bracket-properties/2.0.0/bracket-properties-2.0.0.zip).

There is also the source at [Github](https://github.com/buttermilk-crypto/b2/tree/master/bracket-properties).

##Instantiation


	// Get properties from various I/O input sources
	Reader reader = ...
	InputAdapter ia = new InputAdapter();
	Properties props = ia.read(reader);
	
The above is syntactic sugar for
	
		try (
		 LineScanner scanner = new LineScanner(reader);
		){
			Properties p = new PropertiesParser(scanner).parse().getProperties();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	
You can also instantiate properties instances directly:
	

	Properties props; // interface asia.redact.bracket.properties.Properties
		
	props = new PropertiesImpl(false).init();
	props.put("key", "value");
	
Setting the boolean argument to true for the PropertiesImpl constructor will cause the underlying map 
to be made concurrent (i.e., to have thread-safe operations).  

Some previous instantiation methods have been moved into the InputAdapter:
		
	// from legacy java.util.Properties class
	java.util.Properties legacy = new java.util.Properties();
	...
	Properties props = new InputAdapter().readLegacyProperties(legacy);

	// from a Map	
	Map<String,String> map = new HashMap<>();
	...
	Properties props = new InputAdapter().readMap(map);
	
	
## Order Retention

All Bracket Properties implementations have insertion order retention as the default, even in round tips to 
files. This was one of the main reasons for implementing a new Properties package. There is also a 
SortedPropertiesImpl which takes a Comparator<String> instance as input, for the case when you want
to order the keys. This comes at the cost of a TreeMap. 

Serialization is covered below. 
	
	
## Underlying Data Model

The data model is a map with ValueModel keys, which you can access through the asMap() method:

	Map<String,ValueModel> map = props.asMap();
	
There is also a flattened map available which collapses any continuations:

	Map<String,String> map = props.asFlattenedMap();
	
ValueModel retains all the structure found in properties files:
	
	ValueModel model = props.getValueModel(key);
	char sep = model.getSeparator(); // can be = or :
	String concatenated = model.getValue(); 
	List<String> values = model.getValues(); // list of continuations
	Comment comments = model.getComments(); // any comments are encapsulated here 
	
There is also a KeyValueModel interface which is accessible via the asList() method:

	Properties props = ...;
	List<KeyValueModel> list = props.asList();
	list.foreach(model-> {
		String key = model.getKey();
		String value = model.getValue();
	});
	

## Multiline Support

	Properties props = ...;
	props.put("key1", "first line ", "\ncontinuation1 ", "\ncontinuation2 ");
	System.out.println("key1: " + props.get("key1"));
	
Output is

	key1: first line 
	continuation1 
	continuation2

## Comment Support

Comments are retained and can be re-serialized as expected in most cases. The internal data structure basically sees comments
as something above, rather than below, a key-value pair. There is also a way to programmatically set properties with
a comment block. 

	String key2 = "key2";
	props.put(key2, new Comment("# You rock"), "my value");
	

## Accessors Support
 
 Basic accessor support is via the Properties interface. The sugar() interface now has all the additional
 methods:
 
    public String get(String key);
	public String get(String key, String defaultVal);
	public List<String> getValues(String key); // for multi-line get the values
	public Comment getComments(String key); // full comment including # or !
	public char getSeparator(String key);
	public ValueModel getValueModel(String key);
	
	public void put(String key, String ... values);
	public void put(String key, Comment comment, String ... values);
	public void put(String key, char separator, Comment comment, String ... values);
	public void put(KeyValueModel model);
	public void put(String key, ValueModel model);
 
	// more using the Types sugar.
	Properties props = ...;
	Types t = props.sugar().types();
	t.stringValue("test.s1"); // return a string
	t.intValue("test.int1"); // return a primitive int
	t.booleanValue("test.bool1"); // return a boolean, e.g., test.bool1=enabled
	t.longValue("test.long1"); // return a primitive long
	Date d = t.dateValue("test.date1"); // return a date. Value is a long 
	List<String> list1 = t.listValue("test.list1"); // return a list
	

## Serialization Done Correctly

	OutputAdapter out = new OutputAdapter(props); 
	Writer w = new StringWriter(); 
	out.writeTo(w); 
  
or

	MyOutputFormat format = new MyOutputFormat(); 
	OutputAdapter out = new OutputAdapter(props); 
	Writer w = new StringWriter(); 
	out.writeTo(w, format); 
 
For the common case of java.util.Properties compatibility in US-ASCII encoding with embedded Unicode escapes, 
AsciiOutputFormat is provided:

	AsciiOutputFormat format = new AsciiOutputFormat(); 
	OutputAdapter out = new OutputAdapter(props); 
	File file = new File("my.properties");
	out.writeAsciiTo(file,format); 

or just use

    out.writeAsciiTo(file);
    
there is also the simple

	OutputAdapter.toString(props);

 
## Easy Config Externalization 

The Ref API is for externalization and merging 

	// some externalized properties in user.home 
	String home = System.getProperty("user.home"); 
	String adminExtProps = home+"/admin.properties";
	
	// some defaults in a template loaded from an embedded classpath
	String tProps = "/template.properties";
	
	// these will load in order, merge, and override as expected
	Properties result = new LoadList()
				.addReference(new PropertiesReference(ReferenceType.CLASSLOADED,tProps))
				.addReference(new PropertiesReference(ReferenceType.EXTERNAL,adminExtProps))
				.load()
				.getProps();


## Obfuscation

Version 2.0.0 provides obfuscation and password protection via the sugar() interface:

    Properties props = ...;
    props.add("key", "value");
    
    char [] password = {...};
    Sec sec = props.sugar().sec(password);
    sec.obfuscate("key"); // the value of "key" is obfuscated.
	 
	 System.out.println("key = "+props.get("key"));
	 
Outputs something like:

	key=lHvK4ifXj7CFYsb4=
	
You can also use the unsecure but simple sugar with no password provided:

    Sec sec = props.sugar().sec(); // no password provided
    sec.obfuscate("key"); // the value of "key" is obfuscated.
	
This will use the default key, which offers no security but does effectively obfuscate the value 
if preventing casual visual inspection is the only objective.

 
## XML Support

In earlier versions Bracket provided a custom XML API. In version 2.0 this is replaced with an implementation
which is compatible with the legacy java.util.Properties class. 

    Reader reader = ...;
    InputAdapter ia = new InputAdapter();
    ia.readXML(reader);
    Properties props = ia.props;
   
    Writer writer = ...;
    new OutputAdapter(props)
    .writeTo(writer, new XMLOutputFormat(null));
   
Or just

	String xml = OutputAdapter.toXML(props);


## JSON Support

Version 2.0 introduces a JSON API:

    Reader reader = ...;
    InputAdapter ia = new InputAdapter();
    ia.readJSON(reader);
    Properties props = ia.props;

	 String json = OutputAdapter.toJSON(props);
	 
## Working with lists

Properties have a use pattern where data is in a list form using an index. This was partly to combat the inherent
limitations of the java.util.Properties class, but is a nice idiom in itself:

    key.0=value
    key.1=another
    key.2=again

This idiom is supported by the dot sugar:

    Properties props = ...; // with the above
    Dot dot = props.sugar().dot();
    
    List<String> keys = dot.getListKeys("key"); // finds the matching keys
    List<String> values = dot.valueList("key"); // builds a list of the values
    String s = dot.dotList("key");
    
The String s looks like this:

	value.another.again
	
## Access to the environment variables and system properties

The Env sugar provides a templating capacity and brings the environment variables into the scope of any
properties file:

	Properties props = ...;
	props.put("dir", "My home dir is: ${user.dir}");
	Env env = props.sugar().env();
	String dir = env.resolve("dir");
	
The value of the String "dir" will look something like this after the call to resolve():

    My home dir is: C:\Users\dave
    
## Quotes

There is a sugar for adding quotation marks to values:

    Properties props = ...;
	 props.put("line", "This is a sentence.");
	 Quote quote = props.sugar().quote();
	 String line = quote.curly("line");
	 
The value of the String "line" will look like this after the call to curly():

	"This is a sentence." (not This is a sentence.)
	



 



