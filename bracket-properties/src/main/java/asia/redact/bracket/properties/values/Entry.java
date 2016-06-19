package asia.redact.bracket.properties.values;

import java.util.List;

public class Entry implements KeyValueModel {

	private static final long serialVersionUID = 1L;
	
	private String key;
	private Comment comment;
	private List<String> values;
	private char separator;

	public Entry(String key, char separator, Comment comment, List<String> values) {
		super();
		this.key = key;
		this.comment = comment;
		this.values = values;
		this.separator = separator;
	}

	@Override
	public char getSeparator() {
		return separator;
	}

	@Override
	public Comment getComments() {
		return comment;
	}

	@Override
	public List<String> getValues() {
		return values;
	}

	@Override
	public String getValue() {
		StringBuffer buf = new StringBuffer();
		for(String v: values){
			buf.append(v);
		}
		return buf.toString();
	}

	@Override
	public String getKey() {
		return key;
	}

}
