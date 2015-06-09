package org.hartorn.htf.util;

/**
 * Class representing a pair, with a left element, and a right element.
 *
 * @author Hartorn
 *
 * @param <L>
 *            Left element type
 * @param <R>
 *            Right element type
 */
public final class Pair<L, R> {

    private static final String LEFT = "org.hartorn.htf.util.Pair[LEFT :";
    private static final String RIGHT = " RIGHT:";
    private static final char ENDCHAR = ']';
    private final L left;
    private final R right;

    private <LArg, RArg> Pair(final L leftArg, final R rightArg) {
        this.left = leftArg;
        this.right = rightArg;
    }

    /**
     * Static method factory to create a pair.
     *
     * @param <R>
     *            the right element type
     * @param <L>
     *            the left element type
     * @param leftArg
     *            the left element
     * @param rightArg
     *            the right element
     * @return the pair created with both elements.
     */
    public static <L, R> Pair<L, R> of(final L leftArg, final R rightArg) {
        return new Pair<L, R>(leftArg, rightArg);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (this.left() == null) {
            if (other.left() != null) {
                return false;
            }
        } else if (!this.left().equals(other.left())) {
            return false;
        }
        if (this.right() == null) {
            if (other.right() != null) {
                return false;
            }
        } else if (!this.right().equals(other.right())) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.left == null) ? 0 : this.left.hashCode());
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
        final StringBuilder message = new StringBuilder(Pair.LEFT);
        message.append(StringUtil.stringify(this.left()));
        message.append(Pair.RIGHT);
        message.append(StringUtil.stringify(this.right()));
        message.append(Pair.ENDCHAR);
        return message.toString();
    }

}
