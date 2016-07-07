/*
 *  This file is part of Bracket Properties
 *  Copyright 2011-2016 David R. Smith, All Rights Reserved
 *
 */
package asia.redact.bracket.properties.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.values.ValueModel;

public class DotAdapter implements Dot {
	
	Properties props;
	
	public DotAdapter(Properties props){
		this.props = props;
	}
	
	protected Pattern dotIntegerPattern = Pattern.compile("\\.(\\d+)");

	/* (non-Javadoc)
	 * @see asia.redact.bracket.properties.adapter.Dot#getListKeys(java.lang.String)
	 */
	@Override
	public List<String> getListKeys(String keyBase) {
		
		List<String> list = new ArrayList<String>();
		
		Set<String> keys = props.asMap().keySet();
		// collect the keys which match
		for(String k : keys){
			if(k.startsWith(keyBase)){
				// verify key is actually of the correct form, with a dot and a terminal integer
				String remainder = k.substring(keyBase.length(),k.length());
				Matcher matcher = dotIntegerPattern.matcher(remainder);
				if(matcher.matches()){
					list.add(k);
				}else{
					continue;
				}
			}
		}
		//TODO use a comparator
		Collections.sort(list);
		return list;
	}

	/* (non-Javadoc)
	 * @see asia.redact.bracket.properties.adapter.Dot#valueList(java.lang.String)
	 */
	@Override
	public List<String> valueList(String keyBase) {
		List<String> list = new ArrayList<String>();
		Map<Integer,String> numberedMap = new TreeMap<Integer,String>();
		Map<String,ValueModel> map = props.asMap();
		Set<String> keys = map.keySet();
		// collect the keys which match
		for(String k : keys){
			if(k.startsWith(keyBase)){
				// verify key is actually of the correct form, with a dot and a terminal integer
				String remainder = k.substring(keyBase.length(),k.length());
				Matcher matcher = dotIntegerPattern.matcher(remainder);
				if(matcher.matches()){
					Integer keyInt = Integer.parseInt(matcher.group(1));
					numberedMap.put(keyInt,map.get(k).getValue());
				}else{
					continue;
				}
			}
		}
		
		if(numberedMap.size() == 0) return list;
		//collect the values in order of the numbers
		Set<Integer> numberKeySet = numberedMap.keySet();
		for(Integer i: numberKeySet){
			list.add(numberedMap.get(i));
		}
		return list;
	}
	
	@Override
	public String generateClasspath(String keyBase) {
		List<String> list = valueList(keyBase);
		StringBuffer classpath = new StringBuffer();
		int i = 0;
		for(String s: list){
			classpath.append(s);
			if(i<list.size()-1)classpath.append(File.pathSeparator);
			i++;
		}
		
		return classpath.toString();
	}

	@Override
	public String delimitedList(String keyBase, String delim) {
		List<String> list = valueList(keyBase);
		StringBuffer b = new StringBuffer();
		int i = 0;
		for(String s: list){
			b.append(s);
			if(i<list.size()-1)b.append(delim);
			i++;
		}
		
		return b.toString();
	}

	@Override
	public String dotList(String keyBase) {
		return delimitedList(keyBase, ".");
	}
	
}
