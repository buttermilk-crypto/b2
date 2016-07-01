package asia.redact.bracket.properties.adapter;

import asia.redact.bracket.properties.Properties;

public class QuoteAdapter implements Quote {

	final Properties props;
	
	public QuoteAdapter(Properties props) {
		this.props = props;
	}

	@Override
	public String dq(String key) {
		StringBuffer buf = new StringBuffer();
		buf.append("\"");
		buf.append(props.get(key));
		buf.append("\"");
		return buf.toString();
	}

	@Override
	public String sq(String key) {
		StringBuffer buf = new StringBuffer();
		buf.append("'");
		buf.append(props.get(key));
		buf.append("'");
		return buf.toString();
	}

	@Override
	public String curly(String key) {
		StringBuffer buf = new StringBuffer();
		buf.append('\u201C');
		buf.append(props.get(key));
		buf.append("\u201D");
		return buf.toString();
	}

	@Override
	public String scurly(String key) {
		StringBuffer buf = new StringBuffer();
		buf.append('\u2018');
		buf.append(props.get(key));
		buf.append("\u2019");
		return buf.toString();
	}

}
