package net.micwin.yajl.core.exceptions;

/**
 * Thrown when execution passes code that was not yet implemented. This is a
 * brutal alternative to those eclipse-alike todo tasks when generating code.
 * Use this only if the not yet implemented code is important for execution ;) .
 * 
 * @author MicWin
 * 
 */
public class NotYetImplementedError extends Error {

	/**
	 * creates a new Error.
	 * 
	 * @param what
	 *            : the part which has not been implemented yet, like
	 *            "switch case META". This becomes part of a comprehensive
	 *            message.
	 */
	public NotYetImplementedError(String what) {
		super("not yet implemented : " + what);
	}

}
