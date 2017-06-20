package com.luckyend.integrest.filters;

import com.luckyend.integrest.auth.BasicAuthPolicy;
import com.luckyend.integrest.auth.OAuth2Policy;
import com.luckyend.integrest.auth.SecurityPolicy;

import io.restassured.RestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthFilter implements Filter {

	private SecurityPolicy securityPolicy; 
	
	public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
			FilterContext ctx) {
		switch (securityPolicy.getType()) {
		case BASIC:
			BasicAuthPolicy bp = (BasicAuthPolicy) securityPolicy;
			RestAssured.authentication = RestAssured.basic(bp.getUser(), bp.getPassword());
			break;
		case OAUTH2:
			OAuth2Policy op = (OAuth2Policy) securityPolicy;
			requestSpec.header("Authorization", String.format("Bearer %s", op.getToken().getAccessToken()));
		default:
			RestAssured.authentication = RestAssured.DEFAULT_AUTH;
			break;
		}
		
		
		
		return ctx.next(requestSpec, responseSpec);
	}
}
