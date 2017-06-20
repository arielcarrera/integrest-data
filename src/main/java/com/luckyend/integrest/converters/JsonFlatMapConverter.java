package com.luckyend.integrest.converters;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.easetech.easytest.converter.AbstractConverter;

import com.luckyend.flatmapper.json.JsonFlatMapper;
import com.luckyend.integrest.functions.FunctionMapFactory;
import com.luckyend.integrest.model.TestData;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @NoArgsConstructor
public class JsonFlatMapConverter extends AbstractConverter<TestData> {

	private static JsonFlatMapper jsonFlatMapper = new JsonFlatMapper(FunctionMapFactory.getInstance());

	public TestData convert(Map<String, Object> convertFrom) {
		Map<String, Object> from = new HashMap<>(convertFrom);
		from.remove("Duration(ms)");
		String desc = (String) from.get("#descripcion");
		from.remove("#descripcion");
		
		from.values().removeIf(Objects::isNull);
		if (from.size() == 0) return null;
		
    	TestData testJson = new TestData();
    	testJson.setData(jsonFlatMapper.convertFlatMapToJson(from, false));
    	testJson.setDescription(desc);
		
        return testJson;
    }

}
