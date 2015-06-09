package org.hartorn.htf.handler.path;

import org.hartorn.htf.exception.ImplementationException;

/**
 * Interface for the object of a HtfNode.
 *
 * @author Hartorn
 *
 * @param <D>
 *            The object type
 */
public interface Addable<D> {

    /**
     * Add the data to the object.
     *
     * @param data
     *            the data to add.
     * @throws ImplementationException
     *             Exception lors de l'ajout
     */
    void addData(D data) throws ImplementationException;

}
