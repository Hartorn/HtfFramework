package org.hartorn.htf.exception;

/**
 * Exception thrown at runtime, used for not having compilation time errors.
 *
 * @author Hartorn
 *
 */
public class NotYetImplementedException extends RuntimeException {
    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 8644097547957850241L;

    /**
     * Default constructor.
     */
    public NotYetImplementedException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be initialized by a call to
     * {@link #Throwable.initCause(java.lang.Throwable)}.
     *
     * @param message
     *            the detail message
     */
    public NotYetImplementedException(final String message) {
        super(message);
    }
}
