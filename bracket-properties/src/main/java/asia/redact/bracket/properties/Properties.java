package asia.redact.bracket.properties;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import asia.redact.bracket.properties.adapter.Env;
import asia.redact.bracket.properties.adapter.Ref;
import asia.redact.bracket.properties.adapter.Sugar;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.KeyValueModel;
import asia.redact.bracket.properties.values.ValueModel;

public interface Properties {

	//basic accessors
	
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
	
	// Java 8
	public void forEach(BiConsumer<String,ValueModel> action);
	
	// tests
	public boolean containsKey(String key); // return true of the key exists
	public boolean hasNonEmptyValue(String key); // return true if the key exists with a non-empty value
	public int size();
	
	// actions
	public void clear();
	public void deleteKey(String key);
	public Properties merge(Properties props);
	
	public Properties slice(String keyBase);
	
	// conversions
	
	public Map<String,ValueModel> asMap();
	public Map<String,String> asFlattenedMap();
	public List<KeyValueModel> asList();
	public java.util.Properties asLegacy();
	
	// formatting
	public String toXML();
	public String toJSON();
	public String toYAML();
	public String toString();
	
}
