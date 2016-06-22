package asia.redact.bracket.properties.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.properties.values.BasicValueModel;
import asia.redact.bracket.properties.values.Comment;
import asia.redact.bracket.properties.values.Entry;
import asia.redact.bracket.properties.values.KeyValueModel;
import asia.redact.bracket.properties.values.ValueModel;

public class PropertiesImpl extends AbstractMapDerivedPropertiesBase implements
		Properties {

	private static final long serialVersionUID = 1L;

	public PropertiesImpl(boolean concurrent) {
		super(concurrent);
	}

	@Override
	public Properties init() {
		if (concurrent) {
			map = Collections
					.synchronizedMap(new LinkedHashMap<String, ValueModel>());
		} else {
			map = new LinkedHashMap<String, ValueModel>();
		}
		return this;
	}

	@Override
	public String get(String key) {
		ValueModel val = map.get(key);
		if (val == null)
			throw new RuntimeException(
					"Missing value "
							+ key
							+ ". Normally you would test for the existence of keys by using containsKey(key) prior to using get() if there is doubt");
		return val.getValue();
	}

	@Override
	public String get(String key, String defaultVal) {
		if (!map.containsKey(key))
			return defaultVal;
		else
			return get(key);
	}

	@Override
	public List<String> getValues(String key) {
		return map.get(key).getValues();
	}

	@Override
	public Comment getComments(String key) {
		return map.get(key).getComments();
	}

	@Override
	public char getSeparator(String key) {
		return map.get(key).getSeparator();
	}

	@Override
	public void forEach(BiConsumer<String, ValueModel> action) {
		map.forEach(action);
	}

	@Override
	public void put(String key, Comment comment, String... values) {

		if (!map.containsKey(key)) {
			map.put(key, new BasicValueModel(comment, values));
		} else {
			ValueModel val = map.get(key);
			if (val instanceof BasicValueModel) {
				((BasicValueModel) val).addComment(comment.comments);
			}
			val.getValues().clear();
			for (String s : values) {
				val.getValues().add(s);
			}
		}
	}

	@Override
	public void put(String key, char separator, Comment comment,
			String... values) {

		if (!map.containsKey(key)) {
			map.put(key, new BasicValueModel(comment, separator, values));
		} else {
			ValueModel val = map.get(key);
			if (val instanceof BasicValueModel) {
				((BasicValueModel) val).addComment(comment.comments);
				((BasicValueModel) val).setSeparator(separator);
			}
			val.getValues().clear();
			for (String s : values) {
				val.getValues().add(s);
			}
		}
	}

	@Override
	public void put(KeyValueModel model) {
		put(model.getKey(), model);
	}

	@Override
	public void put(String key, ValueModel model) {
		map.put(key, model);
	}

	@Override
	public boolean hasNonEmptyValue(String key) {
		String val = get(key);
		return val != null && (!val.trim().equals(""));
	}

	@Override
	public void deleteKey(String key) {
		this.map.remove(key);
	}

	@Override
	public Properties merge(Properties props) {

		Map<String, ValueModel> his = props.asMap();
		map.putAll(his);

		return this;
	}

	@Override
	public Properties slice(String root) {
		PropertiesImpl impl = new PropertiesImpl(false);
		for (String key : map.keySet()) {
			if (key.startsWith(root)) {
				ValueModel value = map.get(key);
				impl.put(key, value);
			}
		}
		return impl;
	}

	@Override
	public Map<String, ValueModel> asMap() {
		return map;
	}

	@Override
	public Map<String, String> asFlattenedMap() {

		LinkedHashMap<String, String> out = new LinkedHashMap<String, String>();
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = map.get(key).getValue();
			out.put(key, value);
		}
		return out;
	}

	@Override
	public List<KeyValueModel> asList() {
		ArrayList<KeyValueModel> list = new ArrayList<>();
		for (Map.Entry<String, ValueModel> e : map.entrySet()) {
			list.add(new asia.redact.bracket.properties.values.Entry(
					e.getKey(), e.getValue().getSeparator(), e.getValue()
							.getComments(), e.getValue().getValues()));
		}
		return list;
	}

	@Override
	public java.util.Properties asLegacy() {
		java.util.Properties legacy = new java.util.Properties();
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = get(key);
			legacy.put(key, value);
		}
		return legacy;
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toYAML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueModel getValueModel(String key) {
		ValueModel model = map.get(key);
		return new Entry(key, model.getSeparator(), model.getComments(), model.getValues());
	}

	@Override
	public void put(String key, String... values) {

		if (!map.containsKey(key)) {
			map.put(key, new BasicValueModel(values));
		} else {
			ValueModel val = map.get(key);
			val.getValues().clear();
			for (String s : values) {
				val.getValues().add(s);
			}
		}
	}
}
