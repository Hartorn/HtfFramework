package org.hartorn.htf.handler.path;

import java.lang.reflect.Method;

import org.hartorn.htf.annotation.HtfMethod.HttpVerbs;
import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.util.Pair;

/**
 * This class represents a UrlTree, that you can scan with an url to identify the node, and the methods callable.
 *
 * @author Hartorn
 *
 */
public interface UrlTree extends UrlNode {
    /**
     * Build a new empty UrlTree.
     *
     * @return a new UrlTree
     */
    static UrlTree newUrlTree() {
        return PathNode.newTree();
    }

    /**
     * Register the new url to resolve, with the given method, with the verbs.
     *
     * @param fullUrl
     *            the url
     * @param method
     *            the method
     * @param controller
     *            the controller
     * @param verbs
     *            the verbs
     * @throws ImplementationException
     *             Exception thrown if tree is unmodifiable
     */
    void registerNode(final String fullUrl, final Class<?> controller, final Method method, final HttpVerbs[] verbs) throws ImplementationException;

    /**
     * Resolve the given url, with the associated verb.
     *
     * @param fullUrl
     *            the translated path of the request
     * @param verb
     *            the http verb associated with the request url
     * @return The pair of controller, and method to call
     */
    Pair<Class<?>, Method> resolveUrl(final String fullUrl, final HttpVerbs verb);

}
