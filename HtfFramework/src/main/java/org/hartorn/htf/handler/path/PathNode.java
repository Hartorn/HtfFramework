package org.hartorn.htf.handler.path;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hartorn.htf.annotation.HtfMethod.HttpVerbs;
import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.util.Pair;

final class PathNode implements UrlTree {
    private static final String SLASH = "/";
    private static final Logger LOG = LogManager.getLogger();

    private final String urlPart;
    private Map<HttpVerbs, Pair<Class<?>, Method>> callableMethods;
    private Map<String, PathNode> children;
    private final boolean isModifiable = true;

    private PathNode() {
        this.urlPart = null;
        this.callableMethods = null;
        this.children = null;
    }

    private PathNode(final String urlPartArg) {
        this.urlPart = urlPartArg;
        this.callableMethods = null;
        this.children = null;
    }

    /**
     * Build a new empty UrlTree.
     *
     * @return a new UrlTree
     */
    static UrlTree newTree() {
        return new PathNode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hartorn.htf.handler.path.UrlNode#addChild(java.lang.String)
     */
    @Override
    public UrlNode addChild(final String urlPartArg) throws ImplementationException {
        final PathNode newNode = new PathNode(urlPartArg);
        this.addChild(newNode);
        return newNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hartorn.htf.handler.path.UrlNode#addMethod(java.lang.reflect.Method, org.hartorn.htf.annotation.HttpVerb.HttpVerbs[])
     */
    @Override
    public void addMethod(final Pair<Class<?>, Method> callableMethodArg, final HttpVerbs... httpVerbs) throws ImplementationException {
        this.checkModifiable();
        if ((httpVerbs != null) && (httpVerbs.length > 0)) {
            for (final HttpVerbs verb : httpVerbs) {
                this.insertPair(verb, callableMethodArg);
            }
        } else {
            this.insertPair(null, callableMethodArg);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hartorn.htf.handler.path.UrlNode#getChild(java.lang.String)
     */
    @Override
    public UrlNode getChild(final String urlPartArg) {
        if (this.children == null) {
            return null;
        }
        return this.children.get(urlPartArg);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.hartorn.htf.handler.path.UrlNode#getControllerAndMethod(org.hartorn.htf.annotation.ControllerMethod.HttpVerbs)
     */
    @Override
    public Pair<Class<?>, Method> getControllerAndMethod(final HttpVerbs verb) {
        if (this.callableMethods == null) {
            return null;
        }
        Pair<Class<?>, Method> res = this.callableMethods.get(verb);
        if (res == null) {
            res = this.callableMethods.get(null);
        }
        return res;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.hartorn.htf.handler.path.UrlNode#getUrlPart()
     */
    @Override
    public String getUrlPart() {
        return this.urlPart;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.hartorn.htf.handler.path.UrlNode#makeUnmodifiable()
     */
    @Override
    public void makeUnmodifiable() {
        if (this.callableMethods != null) {
            this.callableMethods = Collections.unmodifiableMap(this.callableMethods);
        }
        if (this.children != null) {
            this.children = Collections.unmodifiableMap(this.children);
            for (final UrlNode child : this.children.values()) {
                child.makeUnmodifiable();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hartorn.htf.handler.path.UrlTree#registerNode(java.lang.String, java.lang.Class, java.lang.reflect.Method,
     * org.hartorn.htf.annotation.HttpVerb.HttpVerbs[])
     */
    @Override
    public void registerNode(final String fullUrl, final Class<?> controller, final Method method, final HttpVerbs[] verbs)
            throws ImplementationException {
        PathNode.LOG.debug("HTF - Registering EndPoint - URL:{} Controller:{} Method:{}", fullUrl, controller.getCanonicalName(), method.getName());

        final String[] pathParts = fullUrl.split(PathNode.SLASH);
        UrlNode parent = this;
        UrlNode child = null;
        for (final String nodeName : pathParts) {
            child = parent.getChild(nodeName);
            if (child == null) {
                child = parent.addChild(nodeName);
            }
            parent = child;
        }
        // At the end, parent == child. It is the last node, where the method should be registered.
        child.addMethod(Pair.of(controller, method), verbs);
        PathNode.LOG.debug("HTF - EndPoint registered - URL:{} Controller:{} Method:{}", fullUrl, controller.getCanonicalName(), method.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.hartorn.htf.handler.path.UrlTree#resolveUrl(java.lang.String, org.hartorn.htf.annotation.ControllerMethod.HttpVerbs)
     */
    @Override
    public Pair<Class<?>, Method> resolveUrl(final String fullUrl, final HttpVerbs verb) {
        final String[] pathParts = fullUrl.split(PathNode.SLASH);
        UrlNode node = this;
        for (final String pathPart : pathParts) {
            node = node.getChild(pathPart);
            if (node == null) {
                return null;
            }
        }
        final Pair<Class<?>, Method> res = node.getControllerAndMethod(verb);
        return res;
    }

    private void addChild(final PathNode nodeToAdd) throws ImplementationException {
        this.checkModifiable();
        // Lazy initialisation of the children
        if (this.children == null) {
            this.children = new HashMap<String, PathNode>();
        }
        this.children.put(nodeToAdd.getUrlPart(), nodeToAdd);
    }

    private void checkModifiable() throws ImplementationException {
        if (!this.isModifiable) {
            throw new ImplementationException("This node is not modifiable");
        }
    }

    private void insertPair(final HttpVerbs verb, final Pair<Class<?>, Method> ctrlAndMethod) {
        if (this.callableMethods == null) {
            this.callableMethods = new HashMap<HttpVerbs, Pair<Class<?>, Method>>();
        }
        this.callableMethods.put(verb, ctrlAndMethod);
    }
}
