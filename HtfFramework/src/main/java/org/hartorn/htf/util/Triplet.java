package org.hartorn.htf.util;

/**
 * Class representing a pair, with a element, middle and right element.
 *
 * @author Hartorn
 *
 * @param <L>
 *            Left element type
 * @param <M>
 *            Middle element type
 * @param <R>
 *            Right element type
 */
public final class Triplet<L, M, R> {

    private static final String LEFT = "org.hartorn.htf.util.Tripler[LEFT :";
    private static final String RIGHT = " RIGHT:";
    private static final String MIDDLE = " MIDDLE:";
    private static final char ENDCHAR = ']';
    private final L left;
    private final M middle;
    private final R right;

    private Triplet(final L leftArg, final M middleArg, final R rightArg) {
        this.left = leftArg;
        this.middle = middleArg;
        this.right = rightArg;
    }

    /**
     * Static method factory to create a pair.
     *
     * @param <R>
     *            the right element type
     * @param <M>
     *            the middle element type
     * @param <L>
     *            the left element type
     * @param leftArg
     *            the left element
     * @param middleArg
     *            the middle element
     * @param rightArg
     *            the right element
     * @return the pair created with both elements.
     */
    public static <L, M, R> Triplet<L, M, R> of(final L leftArg, final M middleArg, final R rightArg) {
        return new Triplet<L, M, R>(leftArg, middleArg, rightArg);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Triplet<?, ?, ?> other = (Triplet<?, ?, ?>) obj;
        if (this.left == null) {
            if (other.left != null) {
                return false;
            }
        } else if (!this.left.equals(other.left)) {
            return false;
        }
        if (this.middle == null) {
            if (other.middle != null) {
                return false;
            }
        } else if (!this.middle.equals(other.middle)) {
            return false;
        }
        if (this.right == null) {
            if (other.right != null) {
                return false;
            }
        } else if (!this.right.equals(other.right)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.left == null) ? 0 : this.left.hashCode());
        result = (prime * result) + ((this.middle == null) ? 0 : this.middle.hashCode());
        result = (prime * result) + ((this.right == null) ? 0 : this.right.hashCode());
        return result;
    }

    /**
     * Return the left element of the pair.
     *
     * @return the left element of the pair.
     */
    public L left() {
        return this.left;
    }

    /**
     * Return the middle element of the pair.
     *
     * @return the middle element of the pair.
     */
    public M middle() {
        return this.middle;
    }

    /**
     * Return the right element of the pair.
     *
     * @return the right element of the pair.
     */
    public R right() {
        return this.right;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder message = new StringBuilder(Triplet.LEFT);
        message.append(StringUtil.stringify(this.left()));
        message.append(Triplet.MIDDLE);
        message.append(StringUtil.stringify(this.middle()));
        message.append(Triplet.RIGHT);
        message.append(StringUtil.stringify(this.right()));
        message.append(Triplet.ENDCHAR);
        return message.toString();
    }

}
