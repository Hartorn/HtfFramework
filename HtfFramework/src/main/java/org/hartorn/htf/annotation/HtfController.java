package org.hartorn.htf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.atteo.classindex.IndexAnnotated;

/**
 * Annotation marking the class as a controller.
 *
 * @author Hartorn
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@IndexAnnotated
public @interface HtfController {
    /**
     * Give the controller address url part.
     */
    String address() default "";
}
