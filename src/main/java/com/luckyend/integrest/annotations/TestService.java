package com.luckyend.integrest.annotations;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ METHOD })
public @interface TestService {
	String uri();
	String basePath();
	String path();
}