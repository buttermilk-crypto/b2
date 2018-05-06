/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.BitSet;
import java.util.List;

import asia.redact.bracket.properties.Properties;

/**
 * Syntactic sugar for various type conversions, i.e., retrieve the value and do type conversion in one operation
 * 
 * @author dave
 *
 */
public interface Types {
	
	public static Types instance(Properties props) {
		return new TypesAdapter(props);
	}
	
	public String stringValue(String key);
	public char[] passwordValue(String key);
	public int intValue(String key);
	public boolean booleanValue(String key); // Works for values "true/false", "enabled/disabled", "yes/no", "1/0"
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
	
	public void put(String key, String val);
	public void put(String key, char[] val);
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
	public void put(String key, List<String> values);
	public void put(String key, char delimiter, List<String> values);

}
