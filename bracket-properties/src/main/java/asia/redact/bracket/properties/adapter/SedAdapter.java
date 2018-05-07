package asia.redact.bracket.properties.adapter;

import java.util.List;

import asia.redact.bracket.properties.Properties;

public class SedAdapter implements Sed {
	
    Properties props;
	
	public SedAdapter(Properties props){
		this.props = props;
	}

	@Override
	public void replace(String key, String pattern, String replacement) {
		String rep = props.get(key).replaceAll(pattern, replacement);
		props.put(key, rep);

	}

	@Override
	public void replaceAll(String keyBase, String pattern, String replacement) {
		Dot dot = Dot.instance(props);
		List<String> list = dot.getListKeys(keyBase);
		for(String key: list) {
			replace(key,pattern,replacement);
		}
	}

	@Override
	public void replaceAll(String pattern, String replacement) {
		
		Properties _props = Properties.instance();
		
		props.forEach((k,v) -> {
			String rep = v.getValue().replaceAll(pattern, replacement);
			_props.put(k, rep);
		});
		
		_props.forEach((k,v)-> {
			props.put(k,v);
		});
		
	}

}
