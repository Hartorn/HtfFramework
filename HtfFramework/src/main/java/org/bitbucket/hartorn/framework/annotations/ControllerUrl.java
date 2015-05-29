package org.bitbucket.hartorn.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BaseUrl : annotation giving the path for the controller.
 *
 * @author Hartorn
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ControllerUrl {
    /**
     * Give the controller address url part.
     *
     * @return the url part.
     */
    String adress();
}
