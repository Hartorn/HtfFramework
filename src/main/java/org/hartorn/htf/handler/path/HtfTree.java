package org.hartorn.htf.handler.path;

import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.util.Pair;

/**
 * Represents a generic tree, with data to resolve with a path.
 *
 * @author Hartorn
 *
 * @param <D>
 *            type of the data
 */
public interface HtfTree<D extends Addable<D>> {

    /**
     * The character separing the differents elements of the path.
     *
     * @return the separator element
     */
    char getPathSeparator();

    /**
     * Make the tree unmodifiable, locking node insertions, and making children unmodifiable.
     *
     */
    void makeUnmodifiable();

    /**
     * Register the data at the given path.
     *
     * @param nodePath
     *            the path (starting with path separator character)
     * @param data
     *            the data
     * @throws ImplementationException
     *             Technical Exception
     */
    void registerData(final String nodePath, final D data) throws ImplementationException;

    /**
     * Resolve the node path, returning the data registered there, or null if not found.
     *
     * @param nodePath
     *            the path to resolve
     * @return the data at the node
     */
    D resolvePath(final String nodePath);

    /**
     * Try to resolve the path (starting with path separator character).
     *
     * @param nodePath
     *            the path to resolve
     * @return a pair of the data of the last node visited (if any), and the leftovers of path elements
     */
    Pair<D, String[]> tryToResolve(final String nodePath);

}
