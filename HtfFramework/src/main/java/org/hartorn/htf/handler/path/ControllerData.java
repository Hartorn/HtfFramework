package org.hartorn.htf.handler.path;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.hartorn.htf.annotation.HtfMethod.HttpVerbs;
import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.util.Pair;

/**
 * Data to keep for resolving a controller : HttpVerbs, Method, controller class.
 *
 * @author Hartorn
 *
 */
public final class ControllerData implements Addable<ControllerData> {

    private final Map<HttpVerbs, Pair<Class<?>, Method>> callableMethodsAttr;

    /**
     * Controller.
     *
     * @param ctrlClass
     *            controller class
     * @param callableMethod
     *            method
     * @param verbs
     *            the verbs allowed for this controller and method
     */
    public ControllerData(final Class<?> ctrlClass, final Method callableMethod, final HttpVerbs[] verbs) {
        this.callableMethodsAttr = new HashMap<HttpVerbs, Pair<Class<?>, Method>>();
        if ((verbs == null) || (verbs.length == 0)) {
            this.callableMethodsAttr.put(null, Pair.of(ctrlClass, callableMethod));
        } else {
            for (final HttpVerbs verb : verbs) {
                this.callableMethodsAttr.put(verb, Pair.of(ctrlClass, callableMethod));
            }
        }
    }

    @Override
    public void addData(final ControllerData data) throws ImplementationException {
        this.callableMethodsAttr.putAll(data.getData());
    }

    /**
     * Return the pair of controller class and method for the given httpVerb.
     *
     * @param verb
     *            the httpVerb used
     * @return true
     */
    public Pair<Class<?>, Method> getControllerMethodPair(final HttpVerbs verb) {
        Pair<Class<?>, Method> result = this.callableMethodsAttr.get(verb);
        // If not found for given verb, check if allowed for all.
        if (result == null) {
            result = this.callableMethodsAttr.get(null);
        }
        return result;
    }

    /**
     * Method returning the data.
     *
     * @return the data
     */
    public Map<HttpVerbs, Pair<Class<?>, Method>> getData() {
        return this.callableMethodsAttr;
    }
}
