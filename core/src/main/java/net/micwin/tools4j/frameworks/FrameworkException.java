/*
 * (C) 2003 M.Winkler All Rights reserved
 * 
 * 
 * $Log: FrameworkException.java,v $
 * Revision 1.3  2007/08/30 07:31:38  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:26  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:28  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:23  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.6.2.1  2006/12/29 12:05:14  recipient00
 * introducing generics
 *
 * Revision 1.6  2004/04/18 22:29:05  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.5  2004/04/16 12:27:53  recipient00
 * once more adjusted formattings :( and extended javadocs
 *
 * 
 * Revision 1.4 2004/04/15 19:02:21 recipient00 Messed around with malformed
 * comments ...
 * 
 * 
 * 
 * Revision 1.3 2004/01/11 19:59:21 recipient00 Translated into mere english
 * 
 * Revision 1.2 2004/01/10 20:21:39 recipient00 Added todo tags for translation
 * and legal notes
 * 
 * Revision 1.1 2004/01/07 22:34:20 recipient00 initial check in
 * 
 * Revision 1.1 2003/12/16 09:26:22 micwin initial check in
 *  
 */

package net.micwin.tools4j.frameworks;

import java.io.PrintWriter;

/**
 * Thrown when something is gone wrong with the framework manager. If this
 * exception bases upon another exception (lets say a ConfigExcdeption or else),
 * this one can be stored as <code>reason</code>.
 * 
 * TODO add legal notes
 * 
 * @author micwin@gmx.org
 * @since 10.12.2003 08:18:23
 */
public class FrameworkException extends Exception {

    /**
         * 
         */
    private static final long serialVersionUID = -7748800136476679275L;

    // --------------------------------------------------------------
    // -- instance field
    // --------------------------------------------------------------
    String _name;

    Throwable _reason;

    /**
         * Disabled constructor.
         */
    FrameworkException() {
	super();
    }

    FrameworkException(String s) {
	super(s);
    }

    /**
         * Creates a new Exception based upon an underlying one.
         * 
         * @param name
         * @param reason
         */
    public FrameworkException(String name, Throwable reason) {
	_name = name;
	_reason = reason;
    }

    /**
         * Returns the name of the framework that made problems. This is the
         * name used by the FrameworkManager, not the class name or else of the
         * implementing framework class..
         * 
         * @return
         */
    public String getName() {
	return _name;
    }

    /**
         * Returns the exception that has caused this exception, if any. May be
         * <code>null</code>.
         * 
         * @return
         */
    public Throwable getReason() {
	return _reason;
    }

    /**
         * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
         */
    public void printStackTrace(PrintWriter s) {
	super.printStackTrace(s);
	if (_reason != null) {
	    s.println("\n--- underlying exception : -------------------\n");
	    _reason.printStackTrace(s);
	}
    }

    /**
         * @see java.lang.Object#toString()
         */
    public String toString() {
	return "FrameworkException fw='" + _name + "' Exception=" + _reason;
    }
}