package asia.redact.bracket.codegen;

import java.io.Serializable;
import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.impl.PropertiesImpl;

public class ModelMapDerivedProps extends PropertiesImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	public ModelMapDerivedProps(boolean concurrent) {
		super(concurrent);
	}

	public Properties init() {
		super.init();
		
	//	this.map.put(key, new BasicValueModel(comment, sep, values));
		
		return this;
	}
}
