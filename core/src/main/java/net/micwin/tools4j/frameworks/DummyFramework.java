/*
 * (C) 2003 M.Winkler All Rights reserved
 * 
 * $Log: DummyFramework.java,v $
 * Revision 1.3  2007/08/30 07:31:38  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:26  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:29  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:23  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.7  2004/04/18 22:29:05  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.6  2004/04/16 12:27:53  recipient00
 * once more adjusted formattings :( and extended javadocs
 *
 * 
 * Revision 1.5 2004/04/15 19:02:21 recipient00 Messed around with malformed
 * comments ...
 * 
 * Revision 1.4 2004/04/04 11:00:47 recipient00 Added some more comments, and
 * some reformattings
 * 
 * Revision 1.3 2004/01/11 19:59:21 recipient00 Translated into mere english
 * 
 * Revision 1.2 2004/01/10 20:21:39 recipient00 Added todo tags for translation
 * and legal notes
 * 
 * Revision 1.1 2004/01/07 22:34:20 recipient00 initial check in
 * 
 * Revision 1.1 2003/12/16 09:26:22 micwin initial check in
 */

package net.micwin.tools4j.frameworks;

import net.micwin.tools4j.config.ConfigException;
import net.micwin.tools4j.config.IConfiguration;

/**
 * Test class for unit testing the FrameworkManager TODO add legal notes
 * 
 * @see net.micwin.tools4j.frameworks.IFramework
 * @author micwin@gmx.org
 * @since 14.12.2003 13:37:23
 */
public class DummyFramework implements IFramework {

    boolean shutDown = false;

    /**
         * TODO Add some useful comment here
         * 
         */
    public DummyFramework() {
	super();
    }

    IConfiguration initConfig = null;

    ConfigException initException = null;

    /**
         * @see net.micwin.tools4j.frameworks.IFramework#init(net.micwin.tools4j.config.IConfiguration)
         */
    public void init(IConfiguration configuration) throws ConfigException {
	if (initConfig != null)
	    throw new IllegalStateException("already initialized");
	if (initException != null)
	    throw initException;
	initConfig = configuration;
    }

    /**
         * @see net.micwin.tools4j.frameworks.IFramework#shutDown()
         */
    public void shutDown() {
	shutDown = true;
    }
}