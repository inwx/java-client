package com.inwx.model;

import java.io.Serializable;

import java.util.*;

public class MapElement extends Element implements Serializable {
	Map<String, Element> map;

	public MapElement() {
	}

	public MapElement(Map<String, Object> map) {
		this.map = new HashMap<String, Element>();

		for (String key : map.keySet()) {
			Object o = map.get(key);

			if (o instanceof HashMap) {
				this.map.put(key, new MapElement((Map) o));
			} else if (o instanceof String) {
				this.map.put(key, new StringElement((String) o));
			} else if (o instanceof Integer) {
				this.map.put(key, new IntegerElement((Integer) o));
			} else if (o.getClass().isArray()) {
				this.map.put(key, new ArrayElement((Object[]) o));
			} else if (o instanceof Double) {
				this.map.put(key, new DoubleElement((Double) o));
			}
		}
	}

	public Element get(String key) {
		return map.get(key);
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Map<String, Element> getMap() {
		return map;
	}

	public String toString() {
		String result = "{\n";

		for (String key : keySet()) {
			result += "    " + key + " : " + get(key).toString() + " \n";
		}

		result += "}\n";

		return result;
	}

	public List<Element> getChildren() {
		List<Element> result = new ArrayList<Element>();

		for (Element elem : map.values()) {
			result.addAll(elem.getChildren());
			result.add(elem);
		}

		return result;
	}
}
