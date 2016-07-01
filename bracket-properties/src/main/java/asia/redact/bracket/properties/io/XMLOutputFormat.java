package asia.redact.bracket.properties.io;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import asia.redact.bracket.properties.values.Comment;

/**
 * Re-implementation of the legacy DTD to at least provide ordering and add CDATA 
 * blocks when required. Sun only provided one comment block which is at the beginning. 
 * We can't do much with this DTD and retain compatibility.
 * 
 * @author Dave
 *
 */
public class XMLOutputFormat implements OutputFormat {
	
	private static final String top = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
	private static final String doctype = "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">";
	
	private final static Pattern specialChars = Pattern.compile(">|<|'|\"|&");

	private final String comment;
	
	public XMLOutputFormat(String comment) {
		super();
		this.comment = comment;
	}

	@Override
	public String formatContentType() {
		StringBuilder b = new StringBuilder();
		b.append(top);
		b.append("\n");
		b.append(doctype);
		b.append("\n");
		return b.toString();
	}

	@Override
	public String formatHeader() {
		if(comment == null)
			return "<properties>\n<comment/>\n";
		else {
			StringBuffer b = new StringBuffer();
			b.append("<properties>\n");
			b.append("<comment>\n");
			if(hasXMLSpecialChar(comment)){
				b.append("<![CDATA[");
				b.append(comment);
				b.append("]]>");
			}else{
				b.append(comment);
			}
			b.append("<comment/>\n");
			return b.toString();
		}
			
	}

	@Override
	public String format(String key, char separator, List<String> values, Comment comments) {
		StringBuilder b = new StringBuilder();
		b.append("<entry key=\"");
		b.append(key); 
		b.append("\">");
		values.forEach(item->{
			if(hasXMLSpecialChar(item)){
				b.append("<![CDATA[");
				b.append(item);
				b.append("]]>");
			}else{
				b.append(item);
			}
		});
		b.append("</entry>");
		b.append("\n");
		return b.toString();
	}

	@Override
	public String formatFooter() {
		return "</properties>\n";
	}
	
	private boolean hasXMLSpecialChar(String item) {
		Matcher m = specialChars.matcher(item);
		return m.find();
	}

}
