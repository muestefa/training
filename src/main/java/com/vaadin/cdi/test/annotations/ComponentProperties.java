package com.vaadin.cdi.test.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ FIELD, METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentProperties {
	boolean margin();

	String additionalStyles() default "";
}
