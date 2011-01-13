package net.micwin.yajl.core.validators;

import java.util.Collection;

/**
 * An exception that indicates that Validation has failed.
 * 
 * @author MicWin
 * 
 */
public class ValidationException extends Exception {

	public ValidationException(Collection<String> problems) {
		super(problems.toString());
	}
}
