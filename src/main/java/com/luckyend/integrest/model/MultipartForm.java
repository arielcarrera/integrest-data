
package com.luckyend.integrest.model;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MultipartForm {

	private HashMap<String, Object> data = new HashMap<>();

	public void put(String key, File file) {
		data.put(key, file);
	}

	public void put(String key, String value) {
		data.put(key, value);
	}

	public Object get(String key) {
		return data.get(key);
	}

	public Set<String> keySet() {
		return data.keySet();
	}

	public Set<Map.Entry<String, Object>> entrySet() {
		return data.entrySet();
	}

	public Collection<Object> values() {
		return data.values();
	}

	public int size() {
		return data.size();
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public boolean containsKey(String key) {
		return data.containsKey(key);
	}

	public boolean containsValue(String value) {
		return data.containsValue(value);
	}
	
	public boolean containsValue(File value) {
		return data.containsValue(value);
	}

	public Object remove(String key) {
		return data.remove(key);
	}

	public boolean replace(String key, String oldValue, String newValue) {
		return data.replace(key, oldValue, newValue);
	}
	
	public boolean replace(String key, File oldValue, File newValue) {
		return data.replace(key, oldValue, newValue);
	}
	
	public boolean replace(String key, String oldValue, File newValue) {
		return data.replace(key, oldValue, newValue);
	}
	
	public boolean replace(String key, File oldValue, String newValue) {
		return data.replace(key, oldValue, newValue);
	}

	public Object putIfAbsent(String key, File value) {
		return data.putIfAbsent(key, value);
	}
	
	public Object putIfAbsent(String key, String value) {
		return data.putIfAbsent(key, value);
	}
	
    public void putAllStrings(Map<String, String> strings) {
    	data.putAll(strings);
    }
    
    public void putAllFiles(Map<String, File> files) {
    	data.putAll(files);
    }
}
