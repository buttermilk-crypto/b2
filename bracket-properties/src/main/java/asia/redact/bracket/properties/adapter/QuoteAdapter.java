package asia.redact.bracket.properties.adapter;

import asia.redact.bracket.properties.Properties;

public class QuoteAdapter implements Quote {

	final Properties props;
	
	public QuoteAdapter(Properties props) {
		this.props = props;
	}

	@Override
	public void dq(String key) {
		StringBuffer buf = new StringBuffer();
		buf.append("\"");
		buf.append(props.get(key));
		buf.append("\"");
		props.put(key,buf.toString());
	}

	@Override
	public void sq(String key) {
		StringBuffer buf = new StringBuffer();
		buf.append("'");
		buf.append(props.get(key));
		buf.append("'");
		props.put(key,buf.toString());
	}

	@Override
	public void curly(String key) {
		StringBuffer buf = new StringBuffer();
		buf.append('\u201C');
		buf.append(props.get(key));
		buf.append("\u201D");
		props.put(key,buf.toString());
	}

	@Override
	public void scurly(String key) {
		StringBuffer buf = new StringBuffer();
		buf.append('\u2018');
		buf.append(props.get(key));
		buf.append("\u2019");
		props.put(key,buf.toString());
	}

	@Override
	public void dequote(String key) {
		if(!props.containsKey(key)) return;
		String val = props.get("key");
		boolean startsWith = false;
		boolean endsWith = false;
		if(isQuote(val.charAt(0))) {
			startsWith = true;
		}
		if(isQuote(val.charAt(val.length()-1))) {
			endsWith = true;
		}
		StringBuffer buf = new StringBuffer(val);
		if(startsWith) buf.deleteCharAt(0);
		if(endsWith) buf.deleteCharAt(buf.length()-1);
		props.put(key,buf.toString());
	}

	@Override
	public void quoteAll(char start, char end) {
		props.forEach((k,v)-> {
			StringBuffer buf = new StringBuffer();
			buf.append(start);
			buf.append(v);
			buf.append(end);
			props.put(k,buf.toString());
		});
	}

	@Override
	public void dequoteAll() {
		props.forEach((k,v)-> {
			StringBuffer buf = new StringBuffer();
			buf.append(v);
			buf.deleteCharAt(buf.length()-1);
			buf.deleteCharAt(0);
			props.put(k,buf.toString());
		});
		
	}
	
	private boolean isQuote(char ch){
		switch(ch){
			case '\'': return true;
			case '"': return true;
			case '\u201C': return true;
			case '\u201D': return true;
			case '\u2018': return true;
			case '\u2019': return true;
			default: return false;
		}
	}

}
