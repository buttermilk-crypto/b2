package asia.redact.bracket.properties.adapter;

import asia.redact.bracket.properties.Properties;

/**
 * Implement a reference scheme (aliasing) for keys within a properties file set
 * 
 * @author Dave
 *
 */
public class AliasAdapter implements Alias {

	Properties props;
	
	public AliasAdapter(Properties props) {
		this.props = props;
	}
	
	public String getKeyRef(String key){
		String v = props.get(key);
		if(v.startsWith(REF_TOKEN)){
			return getKeyRef(v.substring(2));
		}else return v;
	}
	
	public void putKeyRef(String newRefKey, String existingKey){
		if(!props.containsKey(existingKey)) {
			throw new RuntimeException("Should be existing key: "+existingKey);
		}
		props.put(newRefKey, REF_TOKEN+existingKey);
	}

}
