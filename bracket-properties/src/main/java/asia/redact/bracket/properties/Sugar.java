/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
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
	
	Quote quote() {
		return new QuoteAdapter(props);
	}
	
	Sec sec() {
		return new SecAdapter(props);
	}
	
	Sec sec(char[]password) {
		return new SecAdapter(props,password);
	}
	
	Types types() {
		return new TypesAdapter(props);
	}
	
}
