package asia.redact.bracket.properties.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.Sugar;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.Entry;
import asia.redact.bracket.properties.values.KeyValueModel;
import asia.redact.bracket.properties.values.ValueModel;
import asia.redact.bracket.util.Obfuscate;

/**
 * true immutable properties
 * 
 * @author Dave
 *
 */
public class ImmutablePropertiesImpl extends PropertiesBaseImpl implements Properties {
	
	// data
	protected final Entry [] array;
	private static final long serialVersionUID = 1L;
	
	public ImmutablePropertiesImpl(ArrayList<Entry> list) {
		super();
		this.array = new Entry[list.size()];
		list.toArray(array);
	}
	
	public ImmutablePropertiesImpl(Entry [] list) {
		super();
		this.array = list;
	}

	// creates a permanently empty properties object
	public ImmutablePropertiesImpl() {
		super();
		array = new Entry[0];
	}

	@Override
	public Map<String, ValueModel> asMap() {
		Map<String,ValueModel> map = new LinkedHashMap<String,ValueModel>();
		for(Entry e: array){
			map.put(e.getKey(), e.cloneImmutable());
		}
		return map;
	}

	@Override
	public Map<String, String> asFlattenedMap() {
		Map<String,String> map = new LinkedHashMap<String,String>();
		for(Entry e: array){
			map.put(e.getKey(), e.getValue());
		}
		return map;
	}
	
	/**
	 * Given a key, locate our Entry in the array. This is thread-safe
	 * @param key
	 * @return
	 */
	public Entry find(String key) {
		lock.lock();
		try {
		for(Entry e: array){
			if(e.getKey().equals(key)) return e;
		}
		}finally{
			lock.unlock();
		}
		
		throw new RuntimeException("no such key: "+key);
	}

	@Override
	public String get(String key) {
		return find(key).getValue();
	}

	public List<String> getComments(String key) {
		lock.lock();
		try {
			if(!containsKey(key)) return null;
			return this.find(key).getComments();
		}finally{
			lock.unlock();
		}
	}	

	public char getSeparator(String key) {
		lock.lock();
		try {
			if(!containsKey(key)) return '\0';
			return find(key).getSeparator();
		}finally{
			lock.unlock();
		}
	}

	@Override
	public int size() {
		   return array.length;
	}

	@Override
	public boolean containsKey(String key) {
		for(Entry e: array){
			if(e.getKey().equals(key)) return true;
		}
		
		return false;
	}

	@Override
	public boolean hasNonEmptyValue(String key) {
		for(Entry e: array){
			if(e.getKey().equals(key)){
				if(!e.getValue().equals("")) return true;
			}
		}
		return false;
	}

	/**
	 * Given a base like key, collect keys like key.0, key.1, key.2 etc into a list. The keys need not 
	 * be in order in the file
	 */
	public List<String> getList(String keyBase) {
		List<String> l = new ArrayList<String>();
		Map<Integer,String> numberedMap = new TreeMap<Integer,String>();
		
		// collect the keys which match
		for(Entry e : array){
			String key = e.getKey();
			if(key.startsWith(keyBase)){
				// verify key is actually of the correct form, with a dot and a terminal integer
				String remainder = key.substring(keyBase.length(),key.length());
				Matcher matcher = dotIntegerPattern.matcher(remainder);
				if(matcher.matches()){
					Integer keyInt = Integer.parseInt(matcher.group(1));
					numberedMap.put(keyInt,find(key).getValue());
				}else{
					continue;
				}
			}
		}
		
		if(numberedMap.size() == 0) return l;
		//collect the values in order of the numbers
		Set<Integer> numberKeySet = numberedMap.keySet();
		for(Integer i: numberKeySet){
			l.add(numberedMap.get(i));
		}
		return l;
	}
	
	public List<String> getListKeys(String keyBase) {
		
		List<String> l = new ArrayList<String>();
		
		// collect the keys which match
		for(Entry e : array){
			String k = e.getKey();
			if(k.startsWith(keyBase)){
				// verify key is actually of the correct form, with a dot and a terminal integer
				String remainder = k.substring(keyBase.length(),k.length());
				Matcher matcher = dotIntegerPattern.matcher(remainder);
				if(matcher.matches()){
					l.add(k);
				}else{
					continue;
				}
			}
		}
		//TODO use a comparator
		Collections.sort(l);
		return l;
	}

	public List<String> getMapKeys(String keyBase) {
		
		List<String> l = new ArrayList<String>();
		
		for(Entry e : array){
			String k = e.getKey();
			if(k.startsWith(keyBase)){
				// verify key is actually of the correct form, with a dot, integer, dot, and k or v
				String remainder = k.substring(keyBase.length(),k.length());
				Matcher matcher = dotKeyValuePattern.matcher(remainder);
				if(matcher.matches()){
					l.add(k);
				}else{
					continue;
				}
			}
		}
		Collections.sort(l);
		return l;
	}

	public java.util.Properties convertToLegacyProperties() {
		lock.lock();
		try {
		java.util.Properties legacy = new java.util.Properties();
		for(Entry e: array){
			String key = e.getKey();
			String value = get(key);
			legacy.put(key, value);
		}
		return legacy;
		}finally{
			lock.unlock();
		}
	}
	
	public Entry atIndex(int index){
		return array[index];
	}

	@Override
	public void put(String key, String... values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void put(String key, Comment comment, String... values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void put(String key, char separator, Comment comment, String... values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Properties merge(Properties props) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void obfuscate(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deobfuscate(String key) {
		throw new UnsupportedOperationException();
	}
	
	public char[] deobfuscateToChar(String key){
		String val = this.get(key);
		return new Obfuscate().decryptToChar(val,StandardCharsets.UTF_8);
	}

	@Override
	public void deleteKey(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get(String key, String defaultVal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getValues(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void forEach(BiConsumer<String, ValueModel> action) {
		
		
	}

	@Override
	public void put(KeyValueModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(String key, ValueModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void obfuscate(char[] password, String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deobfuscate(char[] password, String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public char[] deobfuscateToChar(char[] password, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties slice(String keyBase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KeyValueModel> asList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.util.Properties asLegacy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toYAML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueModel getValueModel(String key) {
		return find(key);
	}
	
	@Override
	public Sugar sugar() {
		if(sugar == null) sugar = new SugarAdapter(this);
		return sugar;
	}

}
