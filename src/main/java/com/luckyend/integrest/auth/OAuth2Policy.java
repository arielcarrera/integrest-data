package com.luckyend.integrest.auth;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Date;

import org.apache.http.HttpStatus;

import com.luckyend.integrest.model.OAuthTokenResponse;
import com.luckyend.integrest.utils.TokenUtil;

import lombok.Getter;
import lombok.Setter;

public class OAuth2Policy implements SecurityPolicy {
	
	@Getter
	private final SecurityPolicyEnum type = SecurityPolicyEnum.OAUTH2;
	
	@Getter @Setter
	private String uri;
	@Getter @Setter
	private String basePath;
	@Getter @Setter
	private String path;
	@Getter @Setter
	private String clientId;
	@Getter @Setter
	private String user;
	@Getter @Setter
	private String password;
	
	@Getter
	protected OAuthTokenResponse tokenResponse;
	@Getter
	protected long accessTokenTime;
	
	public OAuth2Policy(String uri, String basePath, String path, String clientId, String user, String password) {
		super();
		this.uri = uri;
		this.basePath = basePath;
		this.path = path;
		this.clientId = clientId;
		this.user = user;
		this.password = password;
	}
	
	/**
     * Obtiene el token y lo asigna al test actual
     * @return token response
     */
	public OAuthTokenResponse getToken() {
		Date time = new Date();
		//si es la primera vez o expiro el token de acceso... negocio uno nuevo o renegocio uno
		if (tokenResponse == null) {
			obtenerToken(time);
		} else if ((accessTokenTime + tokenResponse.getExpiresIn()) < time.getTime()){
			//si aun no expiro el refresh token...
			if ((accessTokenTime + tokenResponse.getRefreshExpiresIn()) > time.getTime()){
				refrescarToken(time);
			} else {
				//si expiro obtengo directamente uno nuevo
				obtenerToken(time);
			}
		} //caso contrario el token actual es valido
		return tokenResponse;
	}

	protected void refrescarToken(Date time) {
		io.restassured.response.Response r;
		//obtengo un nuevo token con el refresh token
		r = given().baseUri(uri).basePath(basePath).noFilters()
						.accept(APPLICATION_JSON).formParam("grant_type","refresh_token").formParam("client_id", clientId)
						.formParam("refresh_token", tokenResponse.getRefreshToken())
						.expect().statusCode(HttpStatus.SC_OK).when().post(path);
		//actualizo el tiempo en que se pidio el ultimo token al actual
		accessTokenTime = time.getTime();
		//guardo la respuesta
		tokenResponse = TokenUtil.getTokenResponse(r.getBody().asString());
	}

	protected void obtenerToken(Date time) {
		io.restassured.response.Response r;
		//negocio un nuevo token
		 r = given().baseUri(uri).basePath(basePath).noFilters()
						.accept(APPLICATION_JSON).formParam("grant_type","password").formParam("client_id", clientId)
						.formParam("username", user).formParam("password", password)
						.expect().statusCode(HttpStatus.SC_OK).when().post(path);
		//actualizo el tiempo en que se pidio el ultimo token al actual
		accessTokenTime = time.getTime();
		//guardo la respuesta
		tokenResponse = TokenUtil.getTokenResponse(r.getBody().asString());
	}
	
}
