package org.hartorn.htf.handler.path;

import org.hartorn.htf.exception.ImplementationException;

/**
 * Interface representing a generic node, with a string key and a data object.
 *
 * @author Hartorn
 *
 * @param <D>
 *            the object data type
 */
public interface HtfNode<D> {

    /**
     * Add a node with the given key, or return the existing child, if a child with the given key already exists.
     *
     * @param key
     *            the key value
     * @return the new node, or the existing one.
     */
    HtfNode<D> addChild(final String key);

    /**
     * Set the node data to the given data.
     *
     * @param data
     *            the D data to set
     */
    void addData(final D data) throws ImplementationException;

    /**
     * Method returning the child corresponding to this key, or null if not exist.
     *
     * @param key
     *            the key value
     *
     * @return the node, of null
     */
    HtfNode<D> getChild(final String key);

    /**
     * Return the node data.
     *
     * @return the node data (can be null)
     */
    D getData();

    /**
     * Return the node Key (or Id).
     *
     * @return the node key
     */
    String getKey();

    /**
     * Make the node unmodifiable, locking node insertions, and making children unmodifiable.
     *
     */
    void makeUnmodifiable();

}
