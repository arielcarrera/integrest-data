package com.luckyend.integrest.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoAuthPolicy implements SecurityPolicy {
	
	@Getter
	private final SecurityPolicyEnum type = SecurityPolicyEnum.NONE;
	
}
