package com.luckyend.integrest.functions;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import com.luckyend.flatmapper.functions.DefaultFunctionMapFactory;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom function map factory for values
 * 
 * @author Ariel Carrera
 *
 */
@Slf4j @NoArgsConstructor
public class DefaultValueFunctionMapFactory extends DefaultFunctionMapFactory {
	
	private static DefaultValueFunctionMapFactory INSTANCE = null;
	
	private static String[] defaultNameDictionary = {"James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan", "Juan", "Ariel", "Marcos", "Andres", "Julian", "Manuel", "Jose", "Julieta", "Natalia", "Maria", "Victoria", "Andrea", "Laura"};
	private static String[] defaultLastNameDictionary = {"Perez", "Rodriguez", "Garcia", "Martinez", "Lopez", "Sanchez", "Ramirez", "Torres", "Diaz", "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson"};
	
	public static DefaultValueFunctionMapFactory getInstance() {
        if (INSTANCE == null){
        	synchronized(DefaultValueFunctionMapFactory.class){
                if(INSTANCE == null) // check again within synchronized block to guard for race condition
                    INSTANCE = new DefaultValueFunctionMapFactory();
            }
        }
        return INSTANCE;
    }
	

	/* (non-Javadoc)
	 * @see com.luckyend.flatmapper.function.FunctionMapFactory#getFunctionMap()
	 */
	@Override
	public Map<String, Function<String[], Object>> getFunctionMap(){
		if (functionMap == null) {
			super.getFunctionMap();
			functionMap.put("RANDOM_NAME", this::randomName);
			functionMap.put("RANDOM_LASTNAME", this::randomLastName);
			functionMap.put("FILE", this::findFileFromClasspath);
		}
		
		return functionMap;
	}
	
	public String randomName(String[] param){
		String[] dictionary = defaultNameDictionary;
		//TODO soportar nombre del diccionario por parametro
		return dictionary[ThreadLocalRandom.current().nextInt(dictionary.length)];
	}
	
	public String randomLastName(String[] param){
		String[] dictionary = defaultLastNameDictionary;
		//TODO soportar nombre del diccionario por parametro
		return dictionary[ThreadLocalRandom.current().nextInt(dictionary.length)];
	}
	
	/**
	 * Obtiene una referencia a un File
	 * @param param
	 * @return
	 */
	public File findFileFromClasspath(String[] param){
		if (param != null && param.length > 0){
			String path = param[0];
			if (path.startsWith("classpath:")){
				URL u = this.getClass().getClassLoader().getResource(path.substring(10));
				if (u != null){
					try {
						return new File(u.toURI());
					} catch (URISyntaxException e) {
						log.error("Impossible to read file from location {}", path, e);
						return null;
					}
				} else {
					log.error("Impossible to get a file from location {}", path);
				}
			} else {
				return new File(path);
			}
		}
		return null;
	}
}
