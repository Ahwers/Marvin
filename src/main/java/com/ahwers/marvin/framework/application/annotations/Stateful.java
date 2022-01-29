package com.ahwers.marvin.framework.application.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.ahwers.marvin.framework.application.state.ApplicationState;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Stateful {

    Class<? extends ApplicationState> value();

}
