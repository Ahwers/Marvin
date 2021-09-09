package com.ahwers.marvin.framework.application;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Repeatable(CommandMatches.class)
@Retention(RUNTIME)
@Target(METHOD)
public @interface CommandMatch {
	
	String value();

}
