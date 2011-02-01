/**
 * 
 */
package net.micwin.tools4j.exceptions;

/**
 * A technical exception. Since technical problems most the time result in fatal
 * situations, this is realized as a RuntimeEcception.
 * 
 * @author micwin@micwin.net
 * 
 */
public class TechException extends RuntimeException {

    /**
         * 
         */
    private static final long serialVersionUID = -8162673220528039526L;

    /**
         * @param message
         */
    public TechException(String message) {
	super(message);
    }

    /**
         * @param cause
         */
    public TechException(Throwable cause) {
	super(cause);
    }

    /**
         * @param message
         * @param cause
         */
    public TechException(String message, Throwable cause) {
	super(message, cause);
    }

}
