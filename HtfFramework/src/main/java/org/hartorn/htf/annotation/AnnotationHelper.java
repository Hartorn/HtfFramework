package org.hartorn.htf.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.atteo.classindex.ClassFilter;
import org.atteo.classindex.ClassIndex;
import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.util.Pair;

/**
 * AnnotationHelper is used to get classes, using a specific Annotation, or the annotations of a specific class.
 *
 * @author Hartorn
 *
 */
public enum AnnotationHelper {
    ;
    private AnnotationHelper() {
        // helper, private constructor
    }

    /**
     * Check if the class has the given annotation, and throws an exception if the annotation is not present.
     *
     * @param classToCheck
     *            the class to verify
     * @param annotation
     *            the annotation class
     * @throws ImplementationException
     *             The exception thrown if the annotation is not present
     */
    public static void checkAnnotation(final Class<?> classToCheck, final Class<? extends Annotation> annotation) throws ImplementationException {
        if (!AnnotationHelper.hasAnnotation(classToCheck, annotation)) {
            throw new ImplementationException(classToCheck.getCanonicalName() + " does not have annotation " + annotation.getCanonicalName());

        }
    }

    /**
     * Check if the annotation array contains one annotation of the given class.
     *
     * @param annotations
     *            the array of annotations
     * @param annotationClass
     *            the class of searched annotation
     * @return true if it contains an annotation of this class, else false
     */
    public static boolean containsAnnotation(final Annotation[] annotations, final Class<? extends Annotation> annotationClass) {
        for (final Annotation annotation : annotations) {
            if (annotationClass.equals(annotation.annotationType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the set of all the indexed class with the annotation.
     *
     * @param annotationClass
     *            the annotation
     * @return a set of all the annotated classes
     */
    public static Set<Class<?>> getAnnotatedClasses(final Class<? extends Annotation> annotationClass) {
        final Iterable<Class<?>> annotatedClasses = ClassIndex.getAnnotated(annotationClass);

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        for (final Class<?> annotatedClass : annotatedClasses) {
            classes.add(annotatedClass);
        }
        return Collections.unmodifiableSet(classes);
    }

    /**
     * Get the annotated methods for the given class.
     *
     * @param <A>
     *            type of the annotation
     * @param classToInspect
     *            the class you want the methods from
     * @param methodAnnotation
     *            the method annotation researched
     * @return the unmodifiable list of the annotated methods
     */
    public static <A extends Annotation> List<Pair<Method, A>> getAnnotatedMethodsAndAnnotations(final Class<?> classToInspect,
            final Class<A> methodAnnotation) {
        final List<Pair<Method, A>> annotatedMethods = new ArrayList<Pair<Method, A>>();
        for (final Method method : classToInspect.getDeclaredMethods()) {
            if (method.isAnnotationPresent(methodAnnotation)) {
                annotatedMethods.add(Pair.of(method, method.getAnnotation(methodAnnotation)));
            }
        }
        return Collections.unmodifiableList(annotatedMethods);
    }

    /**
     * Return the number of first dimension array containing an annotation of the given class.
     *
     * @param paramAnnotations
     *            a matrix of annotation
     * @param annotationClass
     *            an annotation class to look for
     * @return the number of first dimension array containing an annotation of the given class
     */
    public static int getNumberOfAnnotatedParameters(final Annotation[][] paramAnnotations, final Class<? extends Annotation> annotationClass) {
        int nbAnnotations = 0;
        // Looking for each parameter
        for (final Annotation[] annotationArray : paramAnnotations) {
            // Looking through annotations of the parameter
            if (AnnotationHelper.containsAnnotation(annotationArray, annotationClass)) {
                nbAnnotations++;
            }
        }
        return nbAnnotations;
    }

    /**
     * Return the number of parameters with the given annotation.
     * 
     * @param toCheck
     *            the method to check
     * @param annotationClass
     *            the annotation to look for
     * @return the number of parameters with the given annotation
     */
    public static int getNumberOfAnnotatedParameters(final Method toCheck, final Class<? extends Annotation> annotationClass) {
        return AnnotationHelper.getNumberOfAnnotatedParameters(toCheck.getParameterAnnotations(), annotationClass);
    }

    /**
     * Get the set of the top level, public indexed class with the annotation.
     *
     * @param annotationClass
     *            the annotation
     * @return a set of the annotated classes
     */
    public static Set<Class<?>> getTopLevelPublicAnnotatedClasses(final Class<? extends Annotation> annotationClass) {

        Iterable<Class<?>> annotatedClasses = ClassIndex.getAnnotated(annotationClass);
        annotatedClasses = ClassFilter.only().topLevel().withModifiers(Modifier.PUBLIC).from(annotatedClasses);

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        for (final Class<?> annotatedClass : annotatedClasses) {
            classes.add(annotatedClass);
        }
        return Collections.unmodifiableSet(classes);
    }

    /**
     * Check if the class has the given annotation.
     *
     * @param classToCheck
     *            the class to check
     * @param annotation
     *            the annotation class researched
     * @return true if the class has the annotation, else false
     */
    public static boolean hasAnnotation(final Class<?> classToCheck, final Class<? extends Annotation> annotation) {
        return (classToCheck != null) && classToCheck.isAnnotationPresent(annotation);
    }

}
