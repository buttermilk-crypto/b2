package asia.redact.bracket.properties.impl;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import asia.redact.bracket.util.Obfuscate;
import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.Sugar;
import asia.redact.bracket.properties.values.BasicValueModel;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.KeyValueModel;
import asia.redact.bracket.properties.values.ValueModel;

public class PropertiesImpl extends AbstractMapDerivedPropertiesBase implements Properties {

	private static final long serialVersionUID = 1L;

	public PropertiesImpl() {
		super();
	}
	
	@Override
	void init() {
		map = new LinkedHashMap<String,ValueModel>();
	}

	@Override
	public String get(String key) {
		return null;
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
	public List<String> getComments(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getSeparator(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void forEach(BiConsumer<String, ValueModel> action) {
		map.forEach(action);
	}

	@Override
	public void put(String key, Comment comment, String... values) {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(String key, char separator, Comment comment,
			String... values) {
		// TODO Auto-generated method stub

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
	public boolean hasNonEmptyValue(String key) {
		return false;
	}

	@Override
	public void deleteKey(String key) {
		this.map.remove(key);
	}

	@Override
	public Properties merge(Properties props) {
		// TODO Auto-generated method stub
		return null;
	}

	public void obfuscate(String key){
		String val = this.get(key);
		if(key != null && !key.equals("")){
			String obfuscated = new Obfuscate().encrypt(val);
			this.put(key, obfuscated);
		}
	}
	
	public void deobfuscate(String key){
		String val = this.get(key);
		if(key != null && !key.equals("")){
			String deobfuscated = new Obfuscate().decrypt(val);
			this.put(key, deobfuscated);
		}
	}
	
	public char[] deobfuscateToChar(String key){
		String val = this.get(key);
		return new Obfuscate().decryptToChar(val,StandardCharsets.UTF_8);
	}
	
	@Override
	public void obfuscate(char[] password, String key) {
		String val = this.get(key);
		if(key != null && !key.equals("")){
			String obfuscated = new Obfuscate(password).encrypt(val);
			this.put(key, obfuscated);
		}
	}

	@Override
	public void deobfuscate(char[] password, String key) {
		String val = this.get(key);
		if(key != null && !key.equals("")){
			String deobfuscated = new Obfuscate(password).decrypt(val);
			this.put(key, deobfuscated);
		}
	}

	@Override
	public char[] deobfuscateToChar(char[] password, String key) {
		String val = this.get(key);
		return new Obfuscate(password).decryptToChar(val,StandardCharsets.UTF_8);
	}
	
	

	@Override
	public Properties slice(String keyBase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ValueModel> asMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> asFlattenedMap() {
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
		return map.get(key);
	}

	@Override
	public void put(String key, String ... values){
		lock.lock();
		try {
			if(!map.containsKey(key)){
				map.put(key, new BasicValueModel(values));
			}else{
				ValueModel val = map.get(key);
				val.getValues().clear();
				for(String s:values){
					val.getValues().add(s);
				}
			}
		}finally{
			lock.unlock();
		}
	}
	
	
	@Override
	public Sugar sugar() {
		if(sugar == null) sugar = new SugarAdapter(this);
		return sugar;
	}

}
