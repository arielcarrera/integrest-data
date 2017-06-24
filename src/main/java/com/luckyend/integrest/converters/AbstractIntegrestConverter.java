package com.luckyend.integrest.converters;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;

import org.easetech.easytest.converter.AbstractConverter;

import com.luckyend.integrest.functions.DefaultKeyFunctionMapFactory;
import com.luckyend.integrest.functions.KeyFunctionMapFactory;
import com.luckyend.integrest.model.ParamTypeEnum;
import com.luckyend.integrest.model.TestData;
import com.luckyend.integrest.model.TypedKey;
import com.luckyend.integrest.model.TypedKeyValue;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j @NoArgsConstructor
public abstract class AbstractIntegrestConverter extends AbstractConverter<TestData> {

	@Setter
	private KeyFunctionMapFactory keyFunctionMapFactory;
	
	@Override
	public Class<TestData> convertTo() {
        return (Class<TestData>) TestData.class;
    }
	
	public KeyFunctionMapFactory getKeyFunctionMapFactory(){
		if (keyFunctionMapFactory == null){
			keyFunctionMapFactory = new DefaultKeyFunctionMapFactory();
		}
		return keyFunctionMapFactory;
	}
	
	public TestData convert(Map<String, Object> convertFrom) {
		Map<String, Object> from = new HashMap<>(convertFrom);
		from.remove("Duration(ms)");
		String desc = (String) from.get("#descripcion");
		from.remove("#descripcion");
		String method = (String) from.get("#http_method");
		from.remove("#http_method");
		
		from.values().removeIf(Objects::isNull);
		
		if (from.size() == 0) return null;
		
		TestData testData = new TestData();
    	from.entrySet()
    		.stream()
    		.map(this::prepareKeyFunctions)
    		.filter(Objects::nonNull)
    		.forEach(d -> {
    			switch (d.getType()) {
					case HEADER: testData.putHeader(d.getKey(), d.getValue()); break;
					case PATH_PARAM: testData.putPathParam(d.getKey(), d.getValue()); break;
					case QUERY_PARAM: testData.putQueryParam(d.getKey(), d.getValue()); break;
					default:
						break;
					}	
    		});
    	//quito todas las funciones de las cabeceras
    	from.keySet().removeIf(p -> p.trim().startsWith("{{") && p.trim().endsWith("}}"));
    	//envio a convertir el data a json o lo que sea
    	testData.setData(convertData(from));
    	testData.setDescription(desc);
    	testData.setMethod(method);
    	
        return testData;
    }
	
	protected abstract Object convertData(Map<String,Object> from);
	

	private TypedKeyValue<ParamTypeEnum, String, Object> prepareKeyFunctions(Entry<String, Object> param) {
		String key = param.getKey();
		Object value = param.getValue();
		
		
		TypedKeyValue<ParamTypeEnum, String, Object> keyData = null;
		String expressionValue = key.substring(2, key.length() -2);
		if (!expressionValue.isEmpty()){
			//get the function to apply eg. {{HEADER(Content-type)}}
			int index = expressionValue.indexOf("(");
			if (index < 0){
				log.error("Unrecognized expression type, assigned as text");
//				pair.setKey(key);
			} else {
				String function = expressionValue.substring(0, index);
				if (function.isEmpty()){
					log.error("Unrecognized function type, assigned as text");
//					pair.setKey(key);
				} else {
					Function<String[], TypedKey<ParamTypeEnum, String>> f = getKeyFunctionMapFactory().getFunctionMap().get(function);
					if (f == null){
						log.error("Function {} not found, assigned as text", function);
//						pair.setKey(key);
					} else {
						int lastIndex = expressionValue.lastIndexOf(")");
						if (lastIndex > index){
							String params = expressionValue.substring(index+1, lastIndex);
							TypedKey<ParamTypeEnum, String> fResult = f.apply(params.split(","));
							if (fResult == null){
								log.warn("Function {} returned a null value", function);
							} else {
								keyData = new TypedKeyValue<ParamTypeEnum, String, Object>(fResult.getType(), fResult.getKey(), value);
							}
						} else {
							log.error("Failed to executed function {}, assigned as text", function);
//							pair.setKey(key);
						}
					}
				}
			}
		}
		return keyData;
	}
    
}
