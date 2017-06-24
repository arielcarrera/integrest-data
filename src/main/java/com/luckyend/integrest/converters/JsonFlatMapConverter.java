package com.luckyend.integrest.converters;

import java.util.Map;

import com.luckyend.flatmapper.FlatMapper;
import com.luckyend.flatmapper.json.JsonFlatMapper;
import com.luckyend.integrest.functions.DefaultValueFunctionMapFactory;
import com.luckyend.integrest.model.TestData;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JsonFlatMapConverter extends AbstractIntegrestConverter {

	private static FlatMapper<String> jsonFlatMapper = new JsonFlatMapper(DefaultValueFunctionMapFactory.getInstance());

	@Override
	public TestData convert(Map<String, Object> convertFrom) {
        return super.convert(convertFrom);
    }

	protected String convertData(Map<String,Object> from){
		return jsonFlatMapper.convertFromFlatMap(from, false);
	}

}
