package com.luckyend.integrest.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.luckyend.flatmapper.FlatMapper;
import com.luckyend.flatmapper.json.JsonFlatMapper;
import com.luckyend.integrest.functions.DefaultValueFunctionMapFactory;
import com.luckyend.integrest.model.MultipartForm;
import com.luckyend.integrest.model.TestData;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MultipartConverter extends AbstractIntegrestConverter {

	//Default converter for children
	private static FlatMapper<String> jsonFlatMapper = new JsonFlatMapper(DefaultValueFunctionMapFactory.getInstance());

	@Override
	public TestData convert(Map<String, Object> convertFrom) {
        return super.convert(convertFrom);
    }

	@Override
	protected MultipartForm convertData(Map<String,Object> from){
    	MultipartForm data = new MultipartForm();
    	
    	//obtengo un mapa con los parametros simples del form multipart
		Map<String, Object> mapaParamSimples = from.entrySet()
    			.stream()
    			.filter(p -> !p.getKey().contains("."))
    			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    			
    	//obtengo un mapa con los parametros complejos del form multipar (aquellos que hay que convertir a json)
		Map<String, List<Entry<String, Object>>> mapaParamComplejos = from.entrySet()
    			.stream()
    			.filter(p -> p.getKey().contains("."))
    			.collect(Collectors.groupingBy(this::getNombreEnFormulario));
		
		for (Entry<String, List<Entry<String, Object>>> entradaCompleja : mapaParamComplejos.entrySet()) {
			String keyAtributoComplejo = entradaCompleja.getKey();
			List<Entry<String, Object>> listaAtributos = entradaCompleja.getValue();

			HashMap<String, Object> atributosEntradaCompleja = new HashMap<>();
			for (Entry<String, Object> entry : listaAtributos) {
				atributosEntradaCompleja.put(entry.getKey().substring(entry.getKey().indexOf(".") + 1),entry.getValue());
			}
			mapaParamSimples.put(keyAtributoComplejo, jsonFlatMapper.convertFromFlatMap(atributosEntradaCompleja, false));
		}
		
        return data;
	}
	
	/**
	 * Retorna la clave / nombre del atributo en el form data
	 * @param key
	 * @return
	 */
	private String getNombreEnFormulario(Entry<String, Object> entry){
		String key = entry.getKey();
		int index = key.indexOf('.');
		if (index > 0){
			return key.substring(0, index);
		}
		return key;
	}
}
