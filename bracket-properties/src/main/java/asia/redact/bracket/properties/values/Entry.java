package asia.redact.bracket.properties.values;

import java.util.List;

public class Entry extends BasicValueModel implements KeyValueModel {

	private static final long serialVersionUID = 1L;
	
	String key;

	public Entry() {
		super();
	}

	public Entry(String key, String... value) {
		super(value);
		this.key = key;
	}

	public Entry(String key, char sep, String... value) {
		super(sep, value);
		this.key = key;
	}

	public Entry(String key, Comment comment, String... value) {
		super(comment, value);
		this.key = key;
	}

	public Entry(String key, Comment comment, char sep, String... value) {
		super(comment, sep, value);
		this.key = key;
	}

	public Entry(String key, List<String> comments, List<String> values) {
		super(comments, values);
		this.key = key;
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return key;
	}

}
