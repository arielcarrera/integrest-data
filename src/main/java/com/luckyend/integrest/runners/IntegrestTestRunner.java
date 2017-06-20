
package com.luckyend.integrest.runners;

import org.easetech.easytest.annotation.Converters;
import org.easetech.easytest.converter.ConverterManager;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.luckyend.integrest.IntegrestTest;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Ariel Carrera
 *
 */
@Slf4j
public class IntegrestTestRunner extends DataDrivenTestRunner {

    public IntegrestTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }
    

    /**
     * Returns a new fixture for running a test. Default implementation executes the test class's no-argument
     * constructor (validation should have ensured one exists).
     */
    protected Object createTest() throws Exception {
        Object testInstance = getTestClass().getOnlyConstructor().newInstance();
        loadTestConfigurations(testInstance);
        loadResourceProperties(testInstance);
        
        if (testInstance instanceof IntegrestTest){
        	IntegrestTest it = (IntegrestTest) testInstance;
	        loadIntegrestConfig(it);
	        registerIntegrestConverters(testInstance.getClass().getAnnotation(com.luckyend.integrest.annotations.Converters.class));
	        registerIntegrestConverter(testInstance.getClass().getAnnotation(com.luckyend.integrest.annotations.Converter.class));
//	        if (it.isAuthenticationEnabled()) loadIntegrestSecurityPolicy(it);
//	        else RestAssured.authentication = RestAssured.DEFAULT_AUTH;
        } else {
			log.warn("It's not an IntegrestTest class!");
		}
        
        instrumentClass(getTestClass().getJavaClass(), testInstance);
        
        registerConverter(getTestClass().getJavaClass().getAnnotation(Converters.class));
        return testInstance;

    }
    
	private void registerIntegrestConverters(com.luckyend.integrest.annotations.Converters converters) {
		if (converters != null) {
            Class<? extends com.luckyend.integrest.annotations.Converter>[] convertersArray = converters.value();
            if (convertersArray != null && convertersArray.length > 0) {
                for (Class<? extends com.luckyend.integrest.annotations.Converter> c : convertersArray) {
                    ConverterManager.registerConverter(c);
                }
            }
        }
	}
	
	private void registerIntegrestConverter(com.luckyend.integrest.annotations.Converter converter) {
		if (converter != null) {
    		ConverterManager.registerConverter(converter.value());
        } 
	}
	
	protected void loadIntegrestConfig(IntegrestTest testInstance) {
		//TODO verificar si conviene aqui al crear el test o antes para toda la clase configurar esto
		HttpClientConfig clientConfig;
		if (testInstance.isReuseHttpClient()) {
			clientConfig = HttpClientConfig.httpClientConfig().reuseHttpClientInstance();
		} else {
			clientConfig = HttpClientConfig.httpClientConfig().dontReuseHttpClientInstance();
		}
		RestAssured.config = RestAssured.config().httpClient(clientConfig);
		/****/
		
		RestAssured.baseURI= testInstance.getBaseUri();
		RestAssured.basePath = testInstance.getBasePath();
	}

    /**
     * Returns a {@link Statement} that invokes {@code method} on {@code test}
     */
    protected Statement methodInvoker(FrameworkMethod method, Object testInstance) {
        registerIntegrestConverters(method.getAnnotation(com.luckyend.integrest.annotations.Converters.class));
        registerIntegrestConverter(method.getAnnotation(com.luckyend.integrest.annotations.Converter.class));
        
        return super.methodInvoker(method, testInstance);
    }


}
