package org.hartorn.htf.exception;

/**
 * Exception due to bad user operation (domain validation, access rights, ...).
 *
 * @author Hartorn
 *
 */
public class UserException extends Exception {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -229616096441523479L;

    /**
     * Default constructor.
     */
    public UserException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized, and may subsequently be initialized by a call to
     * {@link #Throwable.initCause(java.lang.Throwable)}.
     *
     * @param message
     *            the detail message
     */
    public UserException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause. Note that the detail message associated with cause is not automatically
     * incorporated in this exception's detail message.
     *
     *
     * @param message
     *            the detail message
     * @param cause
     *            the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public UserException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified detail message, cause, suppression enabled or disabled, and writable stack trace enabled or
     * disabled.
     *
     * @param message
     *            the detail message
     * @param cause
     *            the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression
     *            whether or not suppression is enabled or disabled
     * @param writableStackTrace
     *            whether or not the stack trace should be writable
     */
    public UserException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains
     * the class and detail message of cause). This constructor is useful for exceptions that are little more than wrappers for other throwables (for
     * example, {@link #PrivilegedActionException}).
     *
     * @param cause
     *            the cause (which is saved for later retrieval by the {@link #Throwable.getCause()} method). (A null value is permitted, and
     *            indicates that the cause is nonexistent or unknown.)
     */
    public UserException(final Throwable cause) {
        super(cause);
    }
}
