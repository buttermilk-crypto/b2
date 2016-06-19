package asia.redact.bracket.properties.adapter;

import java.util.regex.Matcher;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.util.EnvResolver;

/**
 * Implement templating scheme for environment variables
 * 
 * @author Dave
 *
 */
public class EnvAdapter implements Env {
	
	Properties props;
	
	public EnvAdapter(Properties props) {
		this.props = props;
	}
	
	@Override
	public String resolve(String key) {
		
		String template = props.get(key);
		Matcher matcher = antStyleVarPattern.matcher(template);
		StringBuffer sb = new StringBuffer();
	    while(matcher.find()){
    		String val = matcher.group(1);
    		String repl = EnvResolver.INSTANCE.get(val);
    		matcher.appendReplacement(sb,Matcher.quoteReplacement(repl));
    		
	    }
	    matcher.appendTail(sb);
		
		return sb.toString();
	}

}
