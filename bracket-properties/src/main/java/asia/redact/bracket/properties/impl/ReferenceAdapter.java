package asia.redact.bracket.properties.impl;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.Ref;

public class ReferenceAdapter implements Ref {

	Properties props;
	
	public ReferenceAdapter(Properties props) {
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
