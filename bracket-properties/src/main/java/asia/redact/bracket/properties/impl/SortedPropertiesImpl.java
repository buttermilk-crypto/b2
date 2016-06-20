package asia.redact.bracket.properties.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

import asia.redact.bracket.properties.Properties;

public class SortedPropertiesImpl extends PropertiesImpl {

	private static final long serialVersionUID = 1L;
	private Comparator<String> comparator;

	public SortedPropertiesImpl(boolean concurrent, Comparator<String> comparator) {
		super(concurrent);
		this.comparator = comparator;
	}

	@Override
	public Properties init() {
		if(comparator == null) {
			if(concurrent) {
				map = Collections.synchronizedMap(new TreeMap<>());
			}else{
				map = new TreeMap<>();
			}
		}else{
			if(concurrent) {
				map = Collections.synchronizedMap(new TreeMap<>(comparator));
			}else{
				map = new TreeMap<>(comparator);
			}
		}
		return this;
	}


}
