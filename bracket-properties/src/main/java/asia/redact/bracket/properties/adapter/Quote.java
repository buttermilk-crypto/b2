package asia.redact.bracket.properties.adapter;

public interface Quote {

	public String dq(String key); // same as get() but with quotation marks (\u0022)
	public String sq(String key); //same as get() but with apostrophes. (\u0027)
	public String curly(String key); // same as get() but with curly quotes (\u201C/\u201D)
	public String scurly(String key); // same as get() but with single curly quotes (\u2018/\u2019)
	
}
