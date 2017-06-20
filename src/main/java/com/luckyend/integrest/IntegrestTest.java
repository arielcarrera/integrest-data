
package com.luckyend.integrest;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.easetech.easytest.converter.ConverterManager;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.luckyend.integrest.annotations.BasicAuth;
import com.luckyend.integrest.annotations.Converter;
import com.luckyend.integrest.annotations.OAuth2;
import com.luckyend.integrest.auth.BasicAuthPolicy;
import com.luckyend.integrest.auth.NoAuthPolicy;
import com.luckyend.integrest.auth.OAuth2Policy;
import com.luckyend.integrest.auth.SecurityPolicy;
import com.luckyend.integrest.converters.JsonFlatMapConverter;
import com.luckyend.integrest.filters.AuthFilter;
import com.luckyend.integrest.model.TestData;
import com.luckyend.integrest.runners.IntegrestTestRunner;

import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(IntegrestTestRunner.class)
public abstract class IntegrestTest {
	
	@Getter
    protected SecurityPolicy securityPolicy;
	
    @Getter
    protected boolean authenticationEnabled;
    
    @Getter
    protected boolean logDataEnabled = false;

    @Getter
    protected boolean reuseHttpClient = true;
    
	public String getBaseUri() {
		return "http://localhost:8080";
	}

	public String getBasePath() {
		return "";
	};
	
    public IntegrestTest() {
		super();
    	OAuth2 oauth2Annotation = this.getClass().getAnnotation(OAuth2.class);
    	if (oauth2Annotation != null) {
    		securityPolicy = new OAuth2Policy(oauth2Annotation.uri(), oauth2Annotation.basePath(), oauth2Annotation.path(), oauth2Annotation.clientId(),
    				oauth2Annotation.user(), oauth2Annotation.password());
    		authenticationEnabled = true;
    	} else {
    		BasicAuth basicAnnotation = this.getClass().getAnnotation(BasicAuth.class);
        	if (basicAnnotation != null) {
        		securityPolicy = new BasicAuthPolicy(basicAnnotation.user(), basicAnnotation.password());
        		authenticationEnabled = true;
        	} else {
    	    	securityPolicy = new NoAuthPolicy();
    	    	authenticationEnabled = false;
        	}
    	}
    	
    	Converter converterAnnotation = this.getClass().getAnnotation(Converter.class);
    	if (converterAnnotation != null) {
    		 ConverterManager.registerConverter(JsonFlatMapConverter.class);
    	}
	}
	
    @Before
    public void setUp() {
    	if (logDataEnabled)	log.info("Setting up test case");
	}
	
	@After
	public void after() throws Exception {
		if (logDataEnabled)	log.info("Running: tearDown");
	}

	protected RequestSpecification buildDefaultRequestSpec(TestData testData){
    	return given().filter(new AuthFilter(this.getSecurityPolicy())).accept(APPLICATION_JSON).contentType(APPLICATION_JSON)//.header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
    		.body(testData.getData());
    }
	
	protected String getSampleToString(String filePath) {
		return getSampleToString(this.getClass().getResourceAsStream(filePath));
	}
	
	protected String getSampleToString(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}


    
}
