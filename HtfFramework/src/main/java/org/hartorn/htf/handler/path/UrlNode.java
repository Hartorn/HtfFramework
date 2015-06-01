package org.hartorn.htf.handler.path;

import java.lang.reflect.Method;

import org.hartorn.htf.annotation.HtfMethod.HttpVerbs;
import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.util.Pair;

/**
 * This class represent a node of a UrlTree. It can try to find the node, represented by the whole url, add children, ...
 *
 * @author Hartorn
 *
 */
public interface UrlNode {

    /**
     * Add a node as a child, with the given urlPart, and returns it.
     *
     * @param urlPartArg
     *            the urlPart for the node
     * @return the new node
     *
     * @throws ImplementationException
     *             Exception if the node is not modifiable
     */
    UrlNode addChild(final String urlPartArg) throws ImplementationException;

    /**
     * Add a callable method to this node.
     *
     * @param callableMethodsArg
     *            the callable method to add for the node
     * @param httpVerbs
     *            the http verbs accepted for this method (none for all)
     *
     * @throws ImplementationException
     *             Exception if the node is not modifiable
     */
    void addMethod(final Pair<Class<?>, Method> callableMethodsArg, final HttpVerbs... httpVerbs) throws ImplementationException;

    /**
     * Method returning the child corresponding to this urlPart, or null if not exist.
     *
     * @param urlPart
     *            the url part
     * @return the node, of null
     */
    UrlNode getChild(final String urlPart);

    /**
     * Return the controller and method for a node, with the given HttpVerb (can be null).
     *
     * @param verb
     *            the HttpVerb
     * @return a pair of a controller and a method, can be null
     */
    Pair<Class<?>, Method> getControllerAndMethod(final HttpVerbs verb);

    /**
     * Return the part of the adress concerned by this node.
     *
     * @return the urlPart of this node
     */
    String getUrlPart();

    /**
     * Make the node unmodifiable, by making unmodifiable the interns maps, by locking node insertions, and by making children unmodifiable.
     *
     */
    void makeUnmodifiable();

}
