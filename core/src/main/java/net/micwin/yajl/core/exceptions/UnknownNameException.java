package net.micwin.yajl.core.exceptions;

/**
 * This exception is thrown when a named element is required and a used name or
 * id is not present.
 * 
 * @author micwin@micwin.net
 * 
 */

public class UnknownNameException extends Exception {

    private static final long serialVersionUID = 3646763015107125684L;
    private final String _nameInQuestion;

    public UnknownNameException(String nameInQuestion) {
	super(nameInQuestion);
	this._nameInQuestion = nameInQuestion;
    }

    public String getNameInQuestion() {
	return _nameInQuestion;
    }

}
