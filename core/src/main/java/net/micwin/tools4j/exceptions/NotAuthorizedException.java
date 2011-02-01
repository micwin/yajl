/**
 * 
 */
package net.micwin.tools4j.exceptions;

/**
 * An exception that is thrown in situations where a login fails, a session has
 * been timed out and such. Feel free to extend.
 * 
 * @author micwin@micwin.net
 * 
 */
public class NotAuthorizedException extends Exception {

    /**
         * 
         */
    private static final long serialVersionUID = -1462063979009188689L;

    /**
         * @param message
         */
    public NotAuthorizedException(String message) {
	super(message);
    }

    /**
         * @param cause
         */
    public NotAuthorizedException(Throwable cause) {
	super(cause);
    }

    /**
         * @param message
         * @param cause
         */
    public NotAuthorizedException(String message, Throwable cause) {
	super(message, cause);
    }
}
