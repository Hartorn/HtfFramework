package org.bitbucket.hartorn.framework.annotations;

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
public @interface MethodUrl {
    /**
     * Give the method url part.
     *
     * @return the url part.
     */
    String adress();
}
