package asia.redact.bracket.properties;

import asia.redact.bracket.properties.adapter.*;

public class Sugar {

	final Properties props;
	
	public Sugar(Properties props) {
		this.props = props;
	}
	
	Alias alias() {
		return new AliasAdapter(props);
	}
	
	Dot dot() {
		return new DotAdapter(props);
	}
	
	Env env() {
		return new EnvAdapter(props);
	}
	
	Obfuscation sec() {
		return new ObfuscationAdapter(props);
	}
	
	Types types() {
		return new TypesAdapter(props);
	}
	
}
