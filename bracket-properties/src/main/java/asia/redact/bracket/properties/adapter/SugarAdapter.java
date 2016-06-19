package asia.redact.bracket.properties.adapter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;

import asia.redact.bracket.properties.Properties;

/**
 * Syntactic sugar - convenience methods
 * 
 * @author Dave
 *
 */
public class SugarAdapter  implements Sugar {

	private Properties props;
	
	public SugarAdapter(Properties props) {
		this.props = props;
	}

	@Override
	public int intValue(String key) {
		return Integer.parseInt(props.get(key));
	}

	/**
	 * Works for "true/false", "enabled/disabled", "yes/no", "ok", "on/off" "1/0"
	 */
	public boolean booleanValue(String key) {
		String val = props.get(key);
		
		if(Boolean.parseBoolean(val)) return true;
		else{
			// special tests
			if(val == null || val.equals("")) return false;
			if(val.toLowerCase().trim().equals("enabled")) return true;
			if(val.toLowerCase().trim().equals("ok")) return true;
			if(val.toLowerCase().trim().equals("on")) return true;
			if(val.toLowerCase().trim().equals("1")) return true;
		}
		
		return false;
	}

	@Override
	public long longValue(String key) {
		return Long.parseLong(props.get(key));
	}

	/**
	 * The value is assumed to be stored as a long integer
	 * 
	 */
	@Override
	public Date dateValue(String key) {
			String val = props.get(key);
			if(val.trim().length() != 13)
				throw new RuntimeException("Value does not look like a long that could be used as a date");
			return new java.util.Date(longValue(key));
	}

	@Override
	public float floatValue(String key) {
		return Float.parseFloat(props.get(key));
	}

	@Override
	public short shortValue(String key) {
		return Short.parseShort(props.get(key));
	}

	@Override
	public byte byteValue(String key) {
		return Byte.parseByte(props.get(key));
	}

	public Date dateValue(String key, String format) throws ParseException {
			SimpleDateFormat f = new SimpleDateFormat(format);
			return f.parse(props.get(key));
	}

	@Override
	public List<String> listValue(String key){
		List<String> l = new ArrayList<String>();
		String val = props.get(key);
		String [] items = val.split(" ");
		for(String s : items){
			l.add(s);
		}
		return l;
	}

	@Override
	public List<String> listValue(String key, String delimiter){
		List<String> l = new ArrayList<String>();
		String val = props.get(key);
		String [] items = val.split(delimiter);
		for(String s : items){
			l.add(s);
		}
		return l;
	}

	@Override
	public BitSet bitsetValue(String key) {
		String value = props.get(key).trim();
		BitSet set = new BitSet(value.length());
		for(int i=0;i<value.length();i++){
			set.set(i,value.charAt(i)=='1' ? true : false);
		}
		return set;
	}

	@Override
	public BigInteger bigValue(String key) {
		return new BigInteger(props.get(key).trim());
	}

	@Override
	public BigDecimal bigDecimalValue(String key) {
		return new BigDecimal(props.get(key).trim());
	}

	public void put(String key, int val){
		props.put(key,String.valueOf(val));
	}
	
	public void put(String key, BigInteger bi){
		props.put(key,String.valueOf(bi));
	}
	
	public void put(String key, BigDecimal bd){
		props.put(key,String.valueOf(bd));
	}
	
	public void put(String key, float val){
		props.put(key,String.valueOf(val));
	}
	
	public void put(String key, long val){
		props.put(key,String.valueOf(val));
	}
	
	public void put(String key, double val){
		props.put(key,String.valueOf(val));
	}
	
	public void put(String key, boolean val){
		props.put(key,String.valueOf(val));
	}
	
	public void put(String key, char val){
		props.put(key,String.valueOf(val));
	}

	@Override
	public void put(String key, byte val) {
		props.put(key,String.valueOf(val));
	}

	@Override
	public void put(String key, short val) {
		props.put(key,String.valueOf(val));
	}
}
