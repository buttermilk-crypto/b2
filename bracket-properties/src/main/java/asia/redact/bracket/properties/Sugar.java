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
	
	Sec sec() {
		return new ObfuscationAdapter(props);
	}
	
	Sec sec(char[]password) {
		return new ObfuscationAdapter(props,password);
	}
	
	Types types() {
		return new TypesAdapter(props);
	}
	
}
