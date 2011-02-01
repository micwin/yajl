/*
 * (C) 2003 M.Winkler All Rights reserved
 * 
 * TODO copyright legal notes for final license model
 * 
 * $Log: AConfiguration.java,v $
 * Revision 1.4  2007/09/06 15:57:58  recipient00
 * reformatted code, removed some code quality flaws
 *
 * Revision 1.3  2007/08/30 07:31:24  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:17  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:23  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:22  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.6.2.2  2007/02/14 22:39:24  recipient00
 * now path is of type HierarchyPath
 *
 * Revision 1.6.2.1  2006/12/29 12:06:35  recipient00
 * A new IFactory for building streams and the like
 *
 * Revision 1.6  2004/04/18 16:29:36  recipient00
 * Implemented FormatException and adjusted Interfaces and abstract
 *
 * Revision 1.5  2004/04/16 12:28:46  recipient00
 * once more adjusted formattings :( and extended javadocs
 *
 * 
 * Revision 1.4 2004/04/04 11:00:46 recipient00 Added some more comments, and
 * some reformattings
 * 
 * Revision 1.3 2004/01/11 19:52:00 recipient00 Translated into mere english
 * 
 * Revision 1.2 2004/01/10 20:19:17 recipient00 Translated into mere english
 * 
 * Revision 1.1 2004/01/07 22:34:20 recipient00 initial check in
 * 
 * Revision 1.2 2003/12/30 21:13:18 micwin pseudo-Kollisionen :(
 * 
 * Revision 1.1 2003/12/16 09:31:22 micwin Basis-Klasse f�r Konfigurationen
 */

package net.micwin.yajl.core.config.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import net.micwin.tools4j.StringFactory;
import net.micwin.tools4j.data.StreamFactory;
import net.micwin.tools4j.data.structures.HierarchyPath;
import net.micwin.tools4j.exceptions.TechException;
import net.micwin.yajl.core.config.FormatException;
import net.micwin.yajl.core.config.IConfiguration;
import net.micwin.yajl.core.config.MissingConfigKeyException;

/**
 * An abstract implementation of most of the methods of
 * <code>IConfiguration</code>. To comfortably handle urls AND files, this
 * class also provides a baseUrl and shortcut mechanism (see
 * <code>protected InputStream openStream(String) throws IOException</code>
 * for details).
 * 
 * @author micwin@gmx.org
 * @since 10.12.2003 22:34:18
 */
public abstract class AConfiguration implements IConfiguration {

    /**
         * This is the prefix to identify local files instead of urls when
         * opening a stream via
         * <code>protected InputStream openStream(String) throws IOException</code>.
         * 
         * @see InputStream openStream(String) throws IOException
         * @deprecated Use {@link StreamFactory#LOCAL_MARKER} instead
         */
    public static final String LOCAL_MARKER = StreamFactory.LOCAL_MARKER;

    /**
         * Used to separate the levels for <code>getPathString()</code>.
         */
    public static final String PATH_SEPARATOR = "/";

    /**
         * Takes the string separator used to tokenize list type entries.
         */
    String _separator;

    /**
         * Takes the location of this configuration within the complete
         * configuration hierarchy.
         */
    HierarchyPath _path;

    /**
         * Replacements for variables of the form ${var_name}, which are used to
         * resolve variables in config keys.
         */
    Properties _keyResolver;

    /**
         * Replacements for variables of the form ${var_name}, which are used to
         * resolve variables in config values.
         */
    Properties _valuesResolver;

    /**
         * A flag triggering the resolvement of keys.
         */
    boolean _resolveKeys;

    /**
         * Convenience variant of
         * <code>this(path, ",", null, System.getProperties());</code>
         * 
         * @param path
         *                Hierarchy path identifier of this instance.
         */
    protected AConfiguration(HierarchyPath path) {
	this(path, ",", null, System.getProperties());
    }

    /**
         * Initializes this level of abstraction.
         * 
         * @param path
         *                A readable and comprehensible location identifier of
         *                this instance.
         * @param separator
         *                A list separator used by dividing elements for
         *                getArray (...).
         * @param keysResolver
         *                When not null, then all keys passed in are searched
         *                for occurences of keys surrounded by ${}. If the
         *                Configuration gets passed in a key
         *                <code>home.of.${login}</code> and
         *                <code>keyResolver</code> contains a key/value
         *                <code>login=nobody</code>, then the key used to
         *                locate a specific configuration key becomes
         *                <code>home.of.nobody</code>.
         * @param valuesResolver
         *                What <code>keysResolver</code> does for keys, this
         *                one does for values : they first will be searched for
         *                keys found in valuesResolver and surrounded by ${}.
         *                Optional, use <code>null</code> to ignore.
         */
    protected AConfiguration(HierarchyPath path, String separator,
	    Properties keysResolver, Properties valuesResolver) {
	super();
	if (path == null) {
	    throw new IllegalArgumentException("argument 'path' is null");
	}
	_path = path;
	_separator = separator != null ? separator : ",";
	_keyResolver = keysResolver;
	_valuesResolver = valuesResolver;
    }

    /**
         * Determines wether or not string values will be trimmed before passing
         * out and therefore can contain spaces and blanks as sole values.
         * 
         * @return
         */
    protected abstract boolean isStringTrimming();

    /**
         * A good idea of the order / hierarchy level of this configuration
         * group.
         * 
         * @return the absolute config path.
         */
    public String getPathString() {
	return _path.getPathString();
    }

    /**
         * Sets a list separator used to separate the elements for
         * getArray(...).
         * 
         * @param newSeparator
         */
    void setSeparator(String newSeparator) {
	_separator = newSeparator;
    }

    /**
         * Returns the base url that is used to prefix urls starting with a
         * <code>*</code>.
         * 
         * @return
         */
    public abstract String getBaseUrl();

    /**
         * Resolves the key, asks <code>getString(String)</code> (supplied by
         * the sub class) for a string value. If present, then resolves the
         * value, and returns this one.
         * 
         * @param key
         *                A key containing (or not) some variables embraced by
         *                ${...}.
         * @return A resolved value.
         * @throws MissingConfigKeyException
         *                 The (resolved)key could not be found.
         */
    String getResolvedString(String key) throws MissingConfigKeyException {
	if (_keyResolver != null) {
	    try {
		key = StringFactory.resolveVars(key, _keyResolver);
	    } catch (ParseException e) {
		System.err.println("Cannot resolve key '" + key + "'");
		e.printStackTrace();
	    }
	}
	String value = getString(key);
	if (_valuesResolver != null) {
	    try {
		value = StringFactory.resolveVars(value, _valuesResolver);
	    } catch (ParseException e) {
		System.err.println("Cannot resolve value '" + value
			+ "' of key '" + key + "'");
		e.printStackTrace();
	    }
	}
	return value;
    }

    /**
         * Returns a configuration value as a string. If the string isnt
         * present, then <code>defaultValue</code> is returned.
         * 
         * @see net.micwin.yajl.core.config.IConfiguration#getString(java.lang.String,
         *      java.lang.String)
         */
    public String getString(String key, String defaultString) {
	try {
	    return getResolvedString(key);
	} catch (MissingConfigKeyException e) {
	    return defaultString;
	}
    }

    /**
         * Retrieves and interprets the named configuration value as a Boolean.
         * 
         * @param key
         *                A name of a configuration value. If specified, a
         *                keysResolver is applied before.
         * @return The value identified by the key, interpreted as a boolean. If
         *         a valueResolver is specified, then the value will be resolved
         *         before interpretation.
         * @throws MissingConfigKeyException
         *                 If the specified key does not lead to a corresponding
         *                 entry.
         * @throws NumberFormatException
         *                 If the specified value cannot be interpreted as a
         *                 boolean. (look message for further details).
         */
    public Boolean getBoolean(String key) throws MissingConfigKeyException,
	    FormatException {
	String resolvedValue = getResolvedString(key);
	try {
	    return Boolean.valueOf(resolvedValue);
	} catch (NumberFormatException e) {
	    throw new FormatException(_path.getPathString(), key,
		    resolvedValue, Boolean.class);
	}
    }

    /**
         * Retrieves and interprets the named configuration value as a Boolean.
         * 
         * @param key
         *                A name of a configuration value. If specified, a
         *                keysResolver is applied before.
         * @param defaultBool
         *                If the value specified by the key is not available,
         *                then this one is returned instead.
         * @return The value identified by the key, interpreted as a boolean. If
         *         a valueResolver is specified, then the value will be resolved
         *         before interpretation.
         * @throws FormatException
         *                 If the specified value cannot be interpreted as a
         *                 boolean. (look message for further details).
         */
    public Boolean getBoolean(String key, Boolean defaultBool)
	    throws FormatException {
	try {
	    return getBoolean(key);
	} catch (MissingConfigKeyException e) {
	    return defaultBool;
	}
    }

    /**
         * Retrieves and interprets the named configuration value as an Integer.
         * 
         * @param key
         *                A name of a configuration value. If specified, a
         *                keysResolver is applied before.
         * @return The value identified by the key, interpreted as a boolean. If
         *         a valueResolver is specified, then the value will be resolved
         *         before interpretation.
         * @throws MissingConfigKeyException
         *                 If the specified key does not lead to a corresponding
         *                 entry.
         * @throws FormatException
         *                 If the specified value cannot be interpreted as an
         *                 Integer. (look message for further details).
         */
    public Integer getInteger(String key) throws MissingConfigKeyException,
	    FormatException {
	String resolvedValue = getResolvedString(key);
	try {
	    return new Integer(resolvedValue);
	} catch (NumberFormatException e) {
	    throw new FormatException(_path.getPathString(), key,
		    resolvedValue, Integer.class);
	}
    }

    /**
         * Retrieves and interprets the named configuration value as an Integer.
         * 
         * @param key
         *                A name of a configuration value. If specified, a
         *                keysResolver is applied before.
         * @param defaultInt
         *                If the value specified by the key is not available,
         *                then this one is returned instead.
         * @return The value identified by the key, interpreted as an integer.
         *         If a valueResolver is specified, then the value will be
         *         resolved before interpretation.
         * @throws FormatException
         *                 If the specified value cannot be interpreted as an
         *                 integer. (look message for further details).
         */
    public Integer getInteger(String key, Integer defaultInt)
	    throws FormatException {
	try {
	    return getInteger(key);
	} catch (MissingConfigKeyException e) {
	    return defaultInt;
	}
    }

    /**
         * Retrieves and interprets the named configuration value as an Array of
         * Strings. The corresponding value is tokenized using the separator
         * specified in a construktor.
         * 
         * @param key
         *                A name of a configuration value. If specified, a
         *                keysResolver is applied before retrieval.
         * @return The value identified by the key, interpreted as a boolean. If
         *         a valueResolver is specified, then the value will be resolved
         *         before interpretation.
         * @throws MissingConfigKeyException
         *                 If the specified key does not lead to a corresponding
         *                 entry.
         */
    public String[] getArray(String key) throws MissingConfigKeyException {
	String value = getResolvedString(key);
	if (value == null)
	    return null;
	LinkedList<String> elements = new LinkedList<String>();
	int startIndex = 0;
	do {
	    int endIndex = value.indexOf(_separator, startIndex);
	    if (endIndex < startIndex) {
		endIndex = value.length();
	    }
	    String element = value.substring(startIndex, endIndex);
	    if (isStringTrimming())
		element = element.trim();
	    elements.add(element);
	    startIndex = value.indexOf(_separator, endIndex)
		    + _separator.length();
	} while (startIndex > _separator.length() - 1);
	// here i go ...
	return (String[]) elements.toArray(new String[elements.size()]);
    }

    /**
         * Retrieves and interprets the named configuration value as an Array of
         * Strings. The corresponding value is tokenized using the separator
         * specified in a construktor.
         * 
         * @param key
         *                A name of a configuration value. If specified, a
         *                keysResolver is applied before retrieval.
         * @param defaultArray
         *                If the value specified by the key is not available,
         *                then this one is returned instead.
         * @return The value identified by the key, interpreted as a string
         *         array . If a valueResolver is specified, then the elements
         *         will be resolved before interpretation.
         */
    public String[] getArray(String key, String[] defaultArray) {
	String[] retVal;
	try {
	    retVal = getArray(key);
	} catch (MissingConfigKeyException e) {
	    retVal = defaultArray;
	}
	return retVal;
    }

    /**
         * Ensures that the specified keys are present.
         * 
         * @param keys
         *                The keys that have to be present
         * @throws MissingConfigKeyException
         *                 If at least one key is missing (see message of
         *                 exception for further details).
         */
    public void assertAvailable(String[] keys) throws MissingConfigKeyException {
	List<String> missingList = new LinkedList<String>();
	for (int i = 0; i < keys.length; i++) {
	    String key = keys[i];
	    try {
		getResolvedString(key);
	    } catch (MissingConfigKeyException mcke) {
		missingList.add(key);
	    }
	}
	if (missingList.size() > 0) {
	    throw new MissingConfigKeyException(_path.getPathString(),
		    (String[]) missingList.toArray(new String[missingList
			    .size()]));
	}
    }

    /**
         * Opens a stream that is identified with the url-like string. If the
         * string starts with <code>*</code>, then <code>getBaseUrl()</code>
         * will be prefixed. THe so-modified eUrl then is passed to
         * StreamFactory.inputSTream(String) to effectively open the stream.
         * 
         * @param eUrl
         *                a url-like string to identify the stream to be opened.
         * @return
         * @see StreamFactory#inputStream(String)
         */
    protected InputStream openStream(String eUrl) throws IOException {
	// extension eUrl
	if (eUrl.startsWith("*")) {
	    eUrl = getBaseUrl() + eUrl.substring(1);
	}
	return StreamFactory.inputStream(eUrl);
    }

    /**
         * Returns the stored Separator.
         * 
         * @return
         */
    public String getSeparator() {
	return _separator;
    }
}