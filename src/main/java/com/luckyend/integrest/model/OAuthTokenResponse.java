package com.luckyend.integrest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * OAuth token response wrapper
 * @author Ariel Carrera
 *
 */
@Data
public class OAuthTokenResponse {
	
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;
    
    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("refresh_expires_in")
    private long refreshExpiresIn;

    @JsonProperty("id_token")
    private String idToken;
    
    @JsonProperty("token_type")
    private String tokenType;

}