package org.hartorn.htf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MethodUrl : annotation giving the path for the controller.
 *
 * @author Hartorn
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HtfMethod {

    /**
     * Give the method url part.
     *
     */
    String address() default "";

    /**
     * Give authorized HTTP verbs.
     *
     */
    HttpVerbs[] httpVerbs() default {};

}
