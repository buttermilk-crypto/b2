package asia.redact.bracket.properties;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.BitSet;
import java.util.List;

public interface Sugar {
	
	public int intValue(String key);
	public boolean booleanValue(String key); // Works for "true/false", "enabled/disabled", "yes/no", "1/0"
	public long longValue(String key);
	public float floatValue(String key);
	public short shortValue(String key);
	public byte byteValue(String key);
	public java.util.Date dateValue(String key); //value is a long
	public java.util.Date dateValue(String key, String format) throws ParseException;
	public List<String> listValue(String key); // default delimiter is a space
	public List<String> listValue(String key, String delimiter);
	public BitSet bitsetValue(String key);
	public BigInteger bigValue(String key);
	public BigDecimal bigDecimalValue(String key);
	
	public void put(String key, int val);
	public void put(String key, byte val);
	public void put(String key, short val);
	public void put(String key, float val);
	public void put(String key, long val);
	public void put(String key, double val);
	public void put(String key, boolean val);
	public void put(String key, char val);
	public void put(String key, BigInteger bi);
	public void put(String key, BigDecimal bd);

}
