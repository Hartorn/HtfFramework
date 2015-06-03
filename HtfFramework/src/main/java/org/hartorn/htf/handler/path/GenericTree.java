package org.hartorn.htf.handler.path;

import org.hartorn.htf.exception.ImplementationException;
import org.hartorn.htf.util.Pair;
import org.hartorn.htf.util.StringUtil;

/**
 * Generic tree implementation.
 *
 *
 * @author Hartorn
 * @param <D>
 *            type of data
 */
public final class GenericTree<D extends Addable<D>> implements HtfTree<D> {
    private final char separator;
    private final HtfNode<D> root;

    /**
     * Constructor.
     *
     * @param pathSeparator
     *            the separator for path resolving.
     */
    public GenericTree(final char pathSeparator) {
        this.separator = pathSeparator;
        this.root = new GenericNode<D>(StringUtil.EMPTY);
    }

    @Override
    public char getPathSeparator() {
        return this.separator;
    }

    @Override
    public void makeUnmodifiable() {
        this.root.makeUnmodifiable();
    }

    @Override
    public void registerData(final String nodePath, final D data) throws ImplementationException {
        final String[] pathParts = StringUtil.stripAndToLower(nodePath, this.getPathSeparator()).split(String.valueOf(this.getPathSeparator()));
        HtfNode<D> node = this.root;
        for (final String pathPart : pathParts) {
            node = node.addChild(pathPart);
        }
        node.addData(data);
    }

    @Override
    public D resolvePath(final String nodePath) {
        final String[] pathParts = StringUtil.stripAndToLower(nodePath, this.getPathSeparator()).split(String.valueOf(this.getPathSeparator()));
        HtfNode<D> node = this.root;
        for (final String pathPart : pathParts) {
            node = node.getChild(pathPart);
            if (node == null) {
                break;
            }
        }
        return node != null ? node.getData() : null;
    }

    @Override
    public Pair<D, String[]> tryToResolve(final String nodePath) {
        final String[] pathParts = StringUtil.stripAndToLower(nodePath, this.getPathSeparator()).split(String.valueOf(this.getPathSeparator()));
        HtfNode<D> node = this.root;
        HtfNode<D> oldNode = null;
        int index = 0;
        // Searching node
        for (; index < pathParts.length; index++) {
            oldNode = node;
            node = node.getChild(pathParts[index]);
            if (node == null) {
                break;
            }
        }

        final String[] leftOvers;

        if (node == null) {
            // If node not found, keep leftovers of url, and last node
            node = oldNode;
            leftOvers = new String[pathParts.length - index];
            for (int i = 0; i < (pathParts.length - index); i++) {
                leftOvers[i] = pathParts[i + index];
            }
        } else {
            leftOvers = new String[0];
        }
        return Pair.of(node.getData(), leftOvers);
    }

}
