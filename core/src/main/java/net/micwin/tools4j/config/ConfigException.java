/*
 * $Log: ConfigException.java,v $
 * Revision 1.3  2007/08/30 07:31:25  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:18  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:23  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:27  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.6.2.1  2006/12/29 12:04:06  recipient00
 * Added serialVersionUIDs, removed "disabled constructors"
 *
 * Revision 1.6  2004/04/18 21:44:43  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.5  2004/04/16 12:29:24  recipient00
 * once more adjusted formattings :( and extended javadocs
 *
 * 
 * Revision 1.4 2004/04/04 10:58:53 recipient00 Added some more comments, and
 * some reformattings
 * 
 * Revision 1.3 2004/01/11 19:51:29 recipient00 Translated into mere english
 * 
 * Revision 1.2 2004/01/10 20:21:39 recipient00 Added todo tags for translation
 * and legal notes
 * 
 * Revision 1.1 2004/01/07 22:34:20 recipient00 initial check in
 * 
 * Revision 1.2 2003/12/30 21:13:18 micwin pseudo-Kollisionen :(
 * 
 * Revision 1.1 2003/12/16 09:30:56 micwin In Erwartung detaillierter Exceptions
 * eine Basis-Exception f�r die Package
 */

package net.micwin.tools4j.config;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Base exception of the config framework. Can hold another exception as
 * <code>reason</code>
 * 
 * @author micwin@gmx.org
 * @since 10.12.2003 08:36:08
 */
public class ConfigException extends Exception {

    /**
         * 
         */
    private static final long serialVersionUID = -5960680866678415468L;

    Throwable _reason = null;

    /**
         * Hidden constructor.
         * 
         * @param s
         */
    ConfigException(String s) {
	super(s);
    }

    /**
         * Hidden constructor.
         */
    ConfigException() {
	super();
    }

    /**
         * Creates a new ConfigException.
         * 
         * @param message
         *                A pessage to describe the erroreous event
         * @param reason
         *                If this exception has been issued by another
         *                exception, then this exception should be put here.
         */
    public ConfigException(String message, Throwable reason) {
	super(message);
	_reason = reason;
    }

    /**
         * If this exception bases upon another exception, then this returns
         * this base exception. Otherwise <code>null</code>.
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
	    s.println("--- underlying exception : -----------------");
	    _reason.printStackTrace(s);
	}
    }

    /**
         * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
         */
    public void printStackTrace(PrintStream s) {
	PrintWriter pw = new PrintWriter(s);
	printStackTrace(pw);
	pw.flush();
    }

    /**
         * @see java.lang.Throwable#printStackTrace()
         */
    public void printStackTrace() {
	printStackTrace(System.err);
    }
}