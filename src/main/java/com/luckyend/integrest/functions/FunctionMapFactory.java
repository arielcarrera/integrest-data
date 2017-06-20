package com.luckyend.integrest.functions;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import com.luckyend.flatmapper.functions.DefaultFunctionMapFactory;

import lombok.NoArgsConstructor;

/**
 * Custom function map factory
 * 
 * @author Ariel Carrera
 *
 */
@NoArgsConstructor
public class FunctionMapFactory extends DefaultFunctionMapFactory {
	
	private static FunctionMapFactory INSTANCE = null;
	
	private static String[] defaultNameDictionary = {"James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan", "Juan", "Ariel", "Marcos", "Andres", "Julian", "Manuel", "Jose", "Julieta", "Natalia", "Maria", "Victoria", "Andrea", "Laura"};
	private static String[] defaultLastNameDictionary = {"Perez", "Rodriguez", "Garcia", "Martinez", "Lopez", "Sanchez", "Ramirez", "Torres", "Diaz", "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson"};
	
	public static FunctionMapFactory getInstance() {
        if (INSTANCE == null){
        	synchronized(FunctionMapFactory.class){
                if(INSTANCE == null) // check again within synchronized block to guard for race condition
                    INSTANCE = new FunctionMapFactory();
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
	
	public File findFileFromClasspath(String[] param){
		return new File("test");
	}
}
