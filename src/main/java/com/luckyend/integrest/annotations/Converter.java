package com.luckyend.integrest.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.luckyend.integrest.converters.AbstractIntegrestConverter;

@Inherited
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ TYPE, METHOD })
public @interface Converter {
	
	Class<? extends AbstractIntegrestConverter> value();
	
}