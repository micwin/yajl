package net.micwin.tools4j.exceptions;

public class NotYetImplementedException extends RuntimeException {

    public NotYetImplementedException() {
    }

    public NotYetImplementedException(Class clazz, String method, String section) {
	super(composeMessage(clazz, method, section));
    }

    private static String composeMessage(Class clazz, String method,
	    String section) {
	String msg = "" + clazz.getName();
	if (method != null) {
	    msg += "." + method;
	}

	if (section != null) {
	    msg += " (section '" + section + "')";
	}
	msg += " has not been implemented yet.";
	return msg;
    }
}
