
package com.luckyend.integrest.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class TestData {

	private String method;
	private Map<String,Object> headers = new HashMap<>();
	private Map<String,Object> pathParams = new HashMap<>();
	private Map<String,Object> queryParams = new HashMap<>();
	private Object data;
	private DataTypeEnum type = DataTypeEnum.JSON;
	private String description;

	public void putPathParam(String key, Object value){
		pathParams.put(key,value);
	}
	
	public void putQueryParam(String key, Object value){
		queryParams.put(key,value);
	}
	
	public void putHeader(String key, Object value){
		headers.put(key,value);
	}
}
