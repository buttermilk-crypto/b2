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
	
	public Alias alias() {
		return new AliasAdapter(props);
	}
	
	public Dot dot() {
		return new DotAdapter(props);
	}
	
	public Env env() {
		return new EnvAdapter(props);
	}
	
	public Quote quote() {
		return new QuoteAdapter(props);
	}
	
	public Sec sec() {
		return new SecAdapter(props);
	}
	
	public Sec sec(char[]password) {
		return new SecAdapter(props,password);
	}
	
	public Types types() {
		return new TypesAdapter(props);
	}
	
}
