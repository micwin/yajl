/*
 * (C) 2003 M.Winkler All Rights reserved
 * 
 * $Log: IFramework.java,v $
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
 * Revision 1.5  2004/04/18 22:54:18  recipient00
 * translated into english
 *
 * Revision 1.4  2004/04/18 22:29:05  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.3  2004/04/15 19:02:21  recipient00
 * Messed around with malformed comments ...
 *
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

import net.micwin.tools4j.config.ConfigException;
import net.micwin.tools4j.config.IConfiguration;

/**
 * Specifies the methods then a FrameworkManager needs to setup and shutdown a
 * Framework implementation. This implementation must provide a public default
 * constructor, and must respond to the methods below as described in order to
 * function correctly.
 * 
 * Most important part is that all initialization is done in init, and no lazy
 * initialization (by means of initializing external dependencies like
 * datasources, loading static files etc) is done later.
 * 
 * TODO add legal notes
 * 
 * @author micwin@gmx.org
 * @since 02.12.2003 22:16:07
 */
public interface IFramework {

    /**
         * Completely initializes this framework. The method should check
         * availability of mandatory keys <i>before</i> starting initializing.
         * A convenient way to do this is calling
         * <code>configuration.assertAvailable</code>.
         * 
         * @param configuration
         *                The configuratgion to take keys from..
         * @throws ConfigException
         *                 If at least one mandatory key is missing.
         */
    void init(IConfiguration configuration) throws ConfigException;

    /**
         * Shuts down this framework, ie releases resources and pools, clears
         * and nullifies internal arrays, collections, lists, sets and maps.
         * Take this serious: clearing collections and hashmaps before
         * nullifying their references speeds up things.
         */
    void shutDown();
}