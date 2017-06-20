package com.luckyend.integrest.utils;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.luckyend.integrest.exceptions.ConversionException;
import com.luckyend.integrest.model.OAuthTokenResponse;

public class TokenUtil {
	
    public static OAuthTokenResponse getTokenResponse(String response) {
        if (response == null)
            return null;
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        	.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
        	.setSerializationInclusion(Include.NON_NULL);
        try {
			return mapper.readValue(response, OAuthTokenResponse.class);
		} catch (IOException e) {
			throw new ConversionException("Invalid OAuth token response", e);
		}
    }
}