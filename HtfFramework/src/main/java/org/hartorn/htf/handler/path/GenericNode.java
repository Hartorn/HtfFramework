package org.hartorn.htf.handler.path;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hartorn.htf.exception.ImplementationException;

/**
 * Generic node implementation.
 *
 * @author Hartorn
 *
 * @param <D>
 *            Type of data
 */
public final class GenericNode<D extends Addable<D>> implements HtfNode<D> {
    private final String keyAttr;
    private D dataAttr;
    private Map<String, HtfNode<D>> children;

    /**
     * Constructor.
     *
     * @param key
     *            the key
     */
    public GenericNode(final String key) {
        this.keyAttr = key;
        this.dataAttr = null;
        this.children = null;
    }

    @Override
    public HtfNode<D> addChild(final String key) {
        if (this.children == null) {
            this.children = new ConcurrentHashMap<String, HtfNode<D>>();
        }
        HtfNode<D> child = this.getChild(key);
        if (child == null) {
            child = new GenericNode<D>(key);
        }

        this.children.put(key, child);
        return child;
    }

    @Override
    public void addData(final D data) throws ImplementationException {
        if (this.dataAttr == null) {
            this.dataAttr = data;
        } else {
            this.dataAttr.addData(data);
        }
    }

    @Override
    public HtfNode<D> getChild(final String key) {
        if (this.children == null) {
            return null;
        }
        return this.children.get(key);
    }

    @Override
    public D getData() {
        return this.dataAttr;
    }

    @Override
    public String getKey() {
        return this.keyAttr;
    }

    @Override
    public void makeUnmodifiable() {
        this.children = Collections.unmodifiableMap(this.children);

        for (final HtfNode<D> node : this.children.values()) {
            node.makeUnmodifiable();
        }
    }
}
