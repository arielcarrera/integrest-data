package com.luckyend.integrest.auth;

import lombok.Getter;
import lombok.Setter;

public class BasicAuthPolicy implements SecurityPolicy {
	
	@Getter
	private final SecurityPolicyEnum type = SecurityPolicyEnum.BASIC;
	
	@Getter @Setter
	private String user;
	@Getter @Setter
	private String password;
	
	public BasicAuthPolicy(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}
	
}
