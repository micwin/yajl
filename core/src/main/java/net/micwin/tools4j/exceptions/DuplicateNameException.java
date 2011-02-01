package net.micwin.tools4j.exceptions;

/**
 * This exception is thrown when a named element is added to a collection, map
 * or whatever, which name is already present.
 * 
 * @author micwin@micwin.net
 * 
 */
public class DuplicateNameException extends Exception {

    private final String _nameInQuestion;

    public DuplicateNameException(String nameInQuestion) {
	super(nameInQuestion);
	this._nameInQuestion = nameInQuestion;
    }

    public String getNameInQuestion() {
	return _nameInQuestion;
    }
}
