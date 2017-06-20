package com.luckyend.integrest.annotations;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ TYPE })
public @interface OAuth2 {
	String uri();
	String basePath();
	String path();
	String clientId();
	String user();
	String password();
}