package net.micwin.tools4j.exceptions;

/**
 * This exception is thrown when a key is required but not present.
 * 
 * @author micwin@micwin.net
 * 
 */

public class UnknownKeyException extends UnknownNameException {

    private static final long serialVersionUID = -8709852335577533433L;

    public UnknownKeyException(String keyInQuestion) {
	super(keyInQuestion);
    }

    public String getKeyInQuestion() {
	return getNameInQuestion();
    }

    @Override
    public String getMessage() {
	return "unknown key '" + getKeyInQuestion() + "'";
    }
}
