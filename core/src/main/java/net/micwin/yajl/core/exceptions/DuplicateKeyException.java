package net.micwin.yajl.core.exceptions;

/**
 * This exception is a specialization of a DuplicateNameException saying that
 * its not only a double name, but a key, which is an identifier.
 * 
 * @author micwin
 * 
 */
public class DuplicateKeyException extends DuplicateNameException {

    /**
         * 
         */
    private static final long serialVersionUID = 8802683356943606794L;

    public DuplicateKeyException(String nameInQuestion) {
	super(nameInQuestion);
    }

    public String getKeyInQuestion() {
	return getNameInQuestion();
    }

}
