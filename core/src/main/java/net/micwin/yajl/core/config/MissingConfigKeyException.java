/*
 * (C) 2003 M.Winkler All Rights reserved
 * 
 * $Log: MissingConfigKeyException.java,v $
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
 * Revision 1.7.2.1  2006/12/29 12:04:06  recipient00
 * Added serialVersionUIDs, removed "disabled constructors"
 *
 * Revision 1.7  2004/04/18 23:08:00  recipient00
 * reworked comments and javadocs
 *
 * Revision 1.6  2004/04/18 21:44:43  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.5  2004/04/16 12:29:24  recipient00
 * once more adjusted formattings :( and extended javadocs
 *
 * 
 * Revision 1.4 2004/04/04 11:00:46 recipient00 Added some more comments, and
 * some reformattings
 * 
 * Revision 1.3 2004/01/11 19:51:29 recipient00 Translated into mere english
 * 
 * Revision 1.2 2004/01/10 20:21:39 recipient00 Added todo tags for translation
 * and legal notes
 * 
 * Revision 1.1 2004/01/07 22:34:20 recipient00 initial check in
 *  
 */

package net.micwin.yajl.core.config;

import net.micwin.tools4j.StringFactory;

/**
 * Signals that at least one configuration key is missing. You can get the list
 * of keys either by calling <code>getMissingKeys</code>, or by simply
 * looking at the Exception's message.
 * 
 * @author micwin@gmx.org
 * @since 08.12.2003 20:13:37
 */
public class MissingConfigKeyException extends ConfigException {

    /**
         * 
         */
    private static final long serialVersionUID = 4098528110801444135L;

    String _path;

    /**
         * List of missing keys.
         */
    String[] _missingKeys;

    /**
         * Disabled constructor.
         */
    MissingConfigKeyException() {
	super();
    }

    /**
         * Disabled constructor.
         * 
         * @param message
         *                passed thru.
         */
    protected MissingConfigKeyException(String message) {
	super(message);
    }

    /**
         * Creates a new MissingConfigException based upon a configuration's
         * path and the list of missing keys.
         * 
         * @param configPath
         *                Path of the malicious configuration (to give the
         *                application's administrator a good idea wghich
         *                configuration group (ie which file) to adjust..
         * @param missingKeys
         *                the (complete!) list of missing (mandatory) keys.
         */
    public MissingConfigKeyException(String configPath, String[] missingKeys) {
	super("Missing key(s) "
		+ StringFactory.toString("[", missingKeys, ",", "]")
		+ " in configuration [" + configPath + ']');
	_path = configPath;
	_missingKeys = missingKeys;
    }

    /**
         * Convenience constructor to create an Exception for only one missing
         * key.
         * 
         * @param configPath
         *                The path of the Configuration that is raising this
         *                exception.
         * @param missingKey
         *                The missing key.
         */
    public MissingConfigKeyException(String configPath, String missingKey) {
	this(configPath, new String[] { missingKey });
    }

    /**
         * Returns the array of missing keys.
         * 
         * @return
         */
    public String[] getMissingKeys() {
	return _missingKeys;
    }

    /**
         * The human readable path of the malicious configuration group.
         * 
         * @return
         */
    public String getPath() {
	return _path;
    }
}