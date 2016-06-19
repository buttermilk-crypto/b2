package asia.redact.bracket.properties.impl;

import java.util.Comparator;
import java.util.TreeMap;

import asia.redact.bracket.properties.values.ValueModel;

public class SortedPropertiesImpl extends PropertiesImpl {

	private static final long serialVersionUID = 1L;
	private Comparator<String> comparator;

	public SortedPropertiesImpl() {
		super();
	}

	public SortedPropertiesImpl(Comparator<String> comparator) {
		init();
		this.comparator = comparator;
	}

	@Override
	protected void init() {
		if(comparator == null) {
			map = new TreeMap<String,ValueModel>();
		}else{
			map = new TreeMap<String,ValueModel>(comparator);
		}
	}


}
