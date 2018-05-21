# b2 (Bracket-Properties 2.3.0)

Bracket Properties is a library to work with Java(tm) .properties files. It has many features missing from the 
core implementation such as retention of order and direct UTF-8 support without escapes. If you can think of 
something you wish properties files could do better, chances are bracket-properties already has it. 

This version is a complete rewrite and the API has changed quite a bit from 1.x. Requires Java 8+.

## Get It

For maven, use:

	<dependency>
	    <groupId>asia.redact.bracket.properties</groupId>
	    <artifactId>bracket-properties</artifactId>
	    <version>2.3.0</version>
	</dependency>
	
Open Source repo:

    https://github.com/buttermilk-crypto/b2


## Instantiation

Bracket Properties implement the Properties interface and have Impl classes, as one would expect
from any java package. There is also an I/O package (again, as you would expect). The most important 
classes in the I/O package are the InputAdapter and the OutputAdapter. Use InputAdapter to load 
properties from different inputs. 

	// Get properties from various I/O input sources
	Properties props = new InputAdapter().read(reader).props;
	Properties props = new InputAdapter().readFile(file, StandardCharsets.UTF_8).props;
   
InputAdapter has many virtues, for example it can pull in multiple properties files:
   
    new InputAdapter()
       .readFile(file0, StandardCharsets.UTF_8)
       .readFile(file1, StandardCharsets.UTF_8)
       .readFile(file2, StandardCharsets.US_ASCII);
       
UTF-8 support is managed by the charset which is passed in. If the properties file is
in the legacy format with Unicode escape sequences, then using US-ASCII or ISO-8859-1 in
conjunction with readFile() will correctly parse those. If UTF-8 is specified, the
properties file is assumed to have UTF-8 characters (escapes will not be processed 
during the parsing phase). More on this below. 
	
Under the hood, InputAdapter calls the PropertiesParser class. It uses a line-based scanner.
	
You can also instantiate Properties instances directly using the factory methods:
	
	Properties props; // interface asia.redact.bracket.properties.Properties
		
	props = Properties.instance();
	props.put("key", "value");
	
There are several different types of back-ends available besides the default LinkedHashMap impl:

     props = Properties.concurrentInstance(); // thread-safe backed collection
    props = Properties.sortedInstance();     // for non-insert order
    

Some previous instantiation methods have been moved into the InputAdapter class:
		
	// from legacy java.util.Properties class
	java.util.Properties legacy = new java.util.Properties();
	...
	Properties props = new InputAdapter().readLegacyProperties(legacy);

	// from a Map	
	Map<String,String> map = new HashMap<>();
	...
	Properties props = new InputAdapter().readMap(map);
	
InputAdapter instances are not intended to be reused. They keep an internal Properties instance 
which is marked as final. They also have an instance variable which remembers where the file
was and its charset as a convenience for later updates to that file:

    InputAdapter in = new InputAdapter;
    in.readFile(path, charset); // sets path and charset. and reads the file contents into in.props
    Properties props = in.props;

    ... // do some updates to that properties instance

    // now update the file to keep it in sync with your changes
    OutputAdapter out = new OutputAdapter(props);
    out.writeFile(in.getCurrentFile(), in.getCurrentCharset());

	
## Order Retention

All Bracket Properties class implementations have insertion order retention as the default, even in round tips to 
files. This was one of the main reasons for implementing a new Properties package. The exception to this rule is
SortedPropertiesImpl which takes a Comparator<String>, for the case when you want to order the keys. 
This comes at the cost of a TreeMap. 

Properties file serialization is covered below. 
	
	
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

Comments are retained and can be re-serialized as expected in most cases. The internal data structure basically sees comments as something above, rather than below, a key-value pair. There is also a way to programmatically set properties with a comment block. 

	String key2 = "key2";
	props.put(key2, new Comment("# You rock"), "my value");
	

## Accessors Support
 
 Basic accessor support is via the Properties interface, more is available through the Types adapter
 
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
 
	// more using the Types adapter
	Properties props = ...;
	Types t = Types.instance(props);
	t.stringValue("test.s1"); // return a string
	t.intValue("test.int1"); // return a primitive int
	t.booleanValue("test.bool1"); // return a boolean, e.g., test.bool1=enabled
	t.longValue("test.long1"); // return a primitive long
	Date d = t.dateValue("test.date1"); // return a date. Value is a long 
	List<String> list1 = t.listValue("test.list1"); // return a list
	
See below for a localization scheme.


## File Serialization Done Correctly

	OutputAdapter out = new OutputAdapter(props); 
	Writer w = new StringWriter(); 
	out.writeTo(w); 
  
or output format can be customized to whatever required extent:

	MyOutputFormat format = new MyOutputFormat(); // implement OutputFormat
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

which uses PlainOutputFormat. 

OutputAdapter is intended to be a one-use class. It retains an instance variable of the Properties
it was instantiated with so you could do something like:

	OutputAdapter out = new OutputAdapter(props); 
	out.write(file0, StandardCharset.US_ASCII); // outputs unicode escapes if required
	out.write(file1, StandardCharset.UTF-8); // outputs just UTF-8 encoded characters
	

## UTF-8 Support

One of the design decisions with java.util.Properties was to insist on the idea a .properties
file was an ASCII-encoded file, even through Java has pretty good UTF-8 native support. In
order to do internationalization, it was necessary to run any UTF-8 characters in properties
files through a command-line utility called ascii2native.exe, which escapes them. The escapes
are then decoded when java.util.Properties loads the file. 

Bracket Properties provides support to read .properties files using either the unicode escape
approach (for backwards-compatibility) or by directly encoding .properties files with UTF-8 characters, for example:

UTF-8 encoded:

    https://raw.githubusercontent.com/buttermilk-crypto/b2/master/bracket-properties/src/test/resources/icu4j/Arabic.out.properties

ASCII encoded with escapes:

    https://raw.githubusercontent.com/buttermilk-crypto/b2/master/bracket-properties/src/test/resources/icu4j/Arabic.out.ascii.properties

Both of these files have identical data, and the same Bracket API will read either one, although it is necessary to have an idea about the charset encoded in the file for this to work:

	   // read UTF-8 encoded file
		InputAdapter ia = new InputAdapter();
		String path = "src/test/resources/icu4j/Arabic.out.properties";
		ia.readFile(new File(path), StandardCharsets.UTF_8); // tell it the charset
		
		Properties props = ia.props;
		
		// read ASCII unicode-escaped version 
		ia = new InputAdapter();
		path = "src/test/resources/icu4j/Arabic.out.ascii.properties";
		ia.readFile(new File(path), StandardCharsets.ISO_8859_1); // tell it the charset
		
		Properties nProps = ia.props;
		
		// assert they are equal data
		Assert.assertEquals(props.get("line0"), nProps.get("line0"));

Note that the UTF-8 encoded file is 4086 bytes; the ASCII file with escapes is more than 12,000 bytes -
a three-fold increase in size. 

Bracket allows for new ideas on internationalization to be explored. For example, one could
organize their i18n properties in this manner:

    // i18n.properties

    ja.email=Eメール
    ja.userid=ユーザーID
    ja.password=パスワード
    ja.login=ログイン
    ko.email=이메일
    ko.userid=사용자 ID
    ko.password=암호
    ko.login=로그인
    zh.email=电子邮件
    zh.userid=用户帐号
    zh.password=密码
    zh.login=登录

    // read properties
    InputAdapter in = new InputAdapter();
    in.readFile("i18n.properties", StandardCharsets.UTF-8);
    
    // language-specific accessor methods
    I18N japanese = I18N.instance(in.props, Locale.JAPANESE);
    I18N korean = I18N.instance(in.props, Locale.KOREAN);
    I18N chinese = I18N.instance(in.props, Locale.CHINESE);
    
    chinese.get("email"); // returns 电子邮件
    

 
## Easy Configuration Externalization 

The Ref API is for externalization and merging. It provides a fully featured properties over-ride
system for application configs. You can develop with a classpath-based config and override
it in deployment with an external config file.

	// some externalized properties in user.home 
	String home = System.getProperty("user.home"); 
	String appExtProps = home+"/app.properties";
	
	// some defaults in a template loaded from an embedded classpath
	String tProps = "/template.properties";
	
	// these will load in order, merge, and override as expected
	Properties result = new LoadList()
				.addReference(new PropertiesReference(ReferenceType.CLASSLOADED, tProps))
				.addReference(new PropertiesReference(ReferenceType.EXTERNAL, appExtProps))
				.load()
				.getProps();


## Obfuscation/Encryption

Version 2.0.0+ provides obfuscation and password protection via the Sec adapter:

    Properties props = ...;
    props.add("key", "value");
    
    char [] password = {...};
    Sec sec = Sec.instance(props);
    sec.sec(password);
    sec.obfuscate("key"); // the value of "key" is obfuscated.
	 
	 System.out.println("key = "+props.get("key"));
	 
Outputs something like:

	key=lHvK4ifXj7CFYsb4=
	
You can also use the simple obfuscation with no password provided:

    Sec sec = Sec.instance(props); // no password provided
    sec.obfuscate("key"); // the value of "key" found in props is obfuscated.
    
later...

    sec.deobfuscate("key"); // the value of key is now deobfuscated and ready to use
    props.get("key");
	
This will use the default key for obfuscation, which offers no security but does effectively obfuscate the value 
if preventing casual visual inspection is the only objective. You can also provide a key but managing
the key is outside the scope of Bracket. 

 
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


## JSON Conversion Support

Version 2.0 introduces a simple to use JSON API taken from the Eclipse Project:

    Reader reader = ...;
    InputAdapter ia = new InputAdapter();
    ia.readJSON(reader);
    Properties props = ia.props;

	 String json = OutputAdapter.toJSON(props);
	 
## Working with lists

Properties have a use pattern where data is in list form using an incrementing index. This was partly to combat the inherent limitations of the java.util.Properties class, but is a nice idiom in itself:

    key.0=value
    key.1=another
    key.2=again

This idiom is supported by the Dot adapter in several ways:

    Properties props = ...; // with the above
    Dot dot = Dot.instance(props);
    
    List<String> keys = dot.getListKeys("key"); // finds the matching keys
    List<String> values = dot.valueList("key"); // builds a list of the values
    String s = dot.dotList("key");
    
The String s looks like this:

	value.another.again
	
## Access to the Environment Variables and System Properties

The Env adapter provides a simple dynamic templating capacity and brings the environment variables 
and System Properties into the scope of any properties file:

	Properties props = ...;
	props.put("dir", "My home dir is: ${user.dir}");
	Env env = Env.instance(props);
	String dir = env.resolve("dir");
	
The value of "dir" will look something like this after the call to resolve():

    My home dir is: C:\Users\dave
    
## Quotes

For adding quotation marks to values:

    Properties props = ...;
	 props.put("line", "This is a sentence.");
	 Quote quote = Quote.instance(props);
	 String line = quote.curly("line");
	 
The value of the String "line" will look like this after the call to curly():

	"This is a sentence." (not This is a sentence.)
	




