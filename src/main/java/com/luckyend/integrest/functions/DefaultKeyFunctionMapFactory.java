package com.luckyend.integrest.functions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.luckyend.integrest.model.ParamTypeEnum;
import com.luckyend.integrest.model.TypedKey;

import lombok.NoArgsConstructor;

/**
 * Custom function map factory for keys
 * 
 * @author Ariel Carrera
 *
 */
@NoArgsConstructor
public class DefaultKeyFunctionMapFactory implements KeyFunctionMapFactory {
	
	private static KeyFunctionMapFactory INSTANCE = null;
	
	public static KeyFunctionMapFactory getDefaultInstance() {
        if (INSTANCE == null){
        	synchronized(DefaultKeyFunctionMapFactory.class){
                if(INSTANCE == null) // check again within synchronized block to guard for race condition
                    INSTANCE = new DefaultKeyFunctionMapFactory();
            }
        }
        return INSTANCE;
    }
	
	protected Map<String, Function<String[], TypedKey<ParamTypeEnum, String>>> keyFunctionMap;
	
	@Override
	public Map<String, Function<String[], TypedKey<ParamTypeEnum, String>>> getFunctionMap(){
		if (keyFunctionMap == null) {
			keyFunctionMap = new HashMap<>();
			keyFunctionMap.put("HEADER", this::header);
			keyFunctionMap.put("PATH_PARAM", this::pathParam);
			keyFunctionMap.put("QUERY_PARAM", this::queryParam);
		}
		
		return keyFunctionMap;
	}
	
	
	public TypedKey<ParamTypeEnum, String> header(String[] param){
		return createParam(ParamTypeEnum.HEADER, param);
	}

	public TypedKey<ParamTypeEnum, String> pathParam(String[] param){
		return createParam(ParamTypeEnum.PATH_PARAM, param);
	}
	
	public TypedKey<ParamTypeEnum, String> queryParam(String[] param){
		return createParam(ParamTypeEnum.QUERY_PARAM, param);
	}
	
	protected TypedKey<ParamTypeEnum, String> createParam(ParamTypeEnum t, String[] param) {
		String s = null;
		if (param != null && param.length > 0) {
			s = param[0];
		}
		return new TypedKey<>(t, s);
	}
	


}
