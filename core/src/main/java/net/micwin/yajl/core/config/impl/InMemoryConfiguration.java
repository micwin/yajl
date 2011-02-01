/*
 * (C) 2003 M.Winkler All Rights reserved
 * 
 * $Log: InMemoryConfiguration.java,v $
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
 * Revision 1.8.2.2  2007/02/14 22:39:24  recipient00
 * now path is of type HierarchyPath
 *
 * Revision 1.8.2.1  2006/12/29 12:06:58  recipient00
 * introducing generics
 *
 * Revision 1.8  2004/08/26 11:25:20  recipient00
 * More explicite information.
 *
 * Revision 1.7  2004/04/18 21:46:25  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.6  2004/04/16 12:28:46  recipient00
 * once more adjusted formattings :( and extended javadocs
 *
 * 
 * Revision 1.5 2004/04/16 08:24:54 recipient00 more comment
 * 
 * Revision 1.4 2004/04/04 11:00:46 recipient00 Added some more comments, and
 * some reformattings
 * 
 * Revision 1.3 2004/01/11 19:52:00 recipient00 Translated into mere english
 * 
 * Revision 1.2 2004/01/10 20:20:53 recipient00 Added todo tags for translation
 * and legal notes
 * 
 * Revision 1.1 2004/01/07 22:34:20 recipient00 initial check in
 * 
 * Revision 1.2 2003/12/30 21:13:18 micwin pseudo-Kollisionen :(
 * 
 * Revision 1.1 2003/12/16 09:31:51 micwin initial check in
 */

package net.micwin.yajl.core.config.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.micwin.yajl.core.config.FormatException;
import net.micwin.yajl.core.config.IConfiguration;
import net.micwin.yajl.core.config.MissingConfigKeyException;

/**
 * Provides an in-memory configuration. To add some keys and values, use
 * <code>put</code>.
 * 
 * @author micwin@gmx.org
 * @since 14.12.2003 13:44:25
 */
public class InMemoryConfiguration implements IConfiguration {

    String _path;

    Map<String, Object> _values;

    /**
         * @see net.micwin.yajl.core.config.impl.AConfiguration#isStringTrimming()
         * @return <code>true</code> if leading and trailing white space will
         *         be removed.
         */
    protected boolean isStringTrimming() {
	return false;
    }

    /**
         * @see net.micwin.yajl.core.config.IConfiguration#getString(java.lang.String)
         */
    public String getString(String key) throws MissingConfigKeyException,
	    FormatException {
	return (String) getObject(key);
    }

    /**
         * Returns the specified object. If not present, a
         * MissingConfigKeyException is thrown.
         * 
         * @param key
         * @return The key's object in its object wrapped form, if native.
         * @throws MissingConfigKeyException
         */
    private Object getObject(String key) throws MissingConfigKeyException {
	Object obj = _values.get(key);
	if (obj == null) {
	    throw new MissingConfigKeyException(getPathString(), key);
	}
	return obj;
    }

    /**
         * @see net.micwin.yajl.core.config.IConfiguration#sub(java.lang.String)
         */
    public IConfiguration sub(String key) throws MissingConfigKeyException,
	    FormatException {
	IConfiguration configuration = (IConfiguration) getObject(key);
	return configuration;
    }

    /**
         * @see net.micwin.yajl.core.config.IConfiguration#getBoolean(java.lang.String)
         */
    public Boolean getBoolean(String key) throws MissingConfigKeyException,
	    FormatException {
	Object valueObject = getObject(key);
	try {

	    return (Boolean) valueObject;
	} catch (ClassCastException e) {
	    throw new FormatException(_path, key, valueObject, Boolean.class);
	}
    }

    /**
         * @see net.micwin.yajl.core.config.IConfiguration#getInteger(java.lang.String)
         */
    public Integer getInteger(String key) throws MissingConfigKeyException,
	    FormatException {
	Object valueObject = getObject(key);
	try {

	    return (Integer) valueObject;
	} catch (ClassCastException e) {
	    throw new FormatException(_path, key, valueObject, Integer.class);
	}
    }

    /**
         * @see net.micwin.yajl.core.config.IConfiguration#getArray(java.lang.String)
         */
    public String[] getArray(String key) throws MissingConfigKeyException,
	    FormatException {
	Object valueObject = getObject(key);
	try {

	    return (String[]) valueObject;
	} catch (ClassCastException e) {
	    throw new FormatException(_path, key, valueObject, String[].class);
	}

    }

    /**
         * @see net.micwin.yajl.core.config.IConfiguration#assertAvailable(java.lang.String[])
         */
    public void assertAvailable(String[] keys) throws MissingConfigKeyException {
	List<String> checkList = new LinkedList<String>();
	for (int i = 0; i < keys.length; i++) {
	    if (!_values.containsKey(keys[i])) {
		checkList.add(keys[i]);
	    }
	}
	if (checkList.size() > 0)
	    throw new MissingConfigKeyException(getPathString(),
		    (String[]) checkList.toArray(new String[checkList.size()]));
    }

    /**
         * @see net.micwin.yajl.core.config.IConfiguration#getPathString()
         */
    public String getPathString() {
	return _path;
    }

    /**
         * Creates a new InMemoryConfiguration using the specified path.
         * 
         * @param path
         *                The absolute path of this newly created configuration.
         */
    public InMemoryConfiguration(String path) {
	super();
	_values = new HashMap<String, Object>();
	_path = path;
    }

    /**
         * Creates a new InMemoryConfiguration as root node.
         */
    public InMemoryConfiguration() {
	this("//imc");
    }

    /**
         * Adds a key/value pair to the configuration. Make sure that you put in
         * the right type of element:
         * <li>If you want the element to be taken via getString(..), put in a
         * String.</li>
         * <li>If you want the element to be taken via getArray(..), put in a
         * String Array .</li>
         * 
         * @param key
         * @param value
         * @return If the specified key already was in use, then the old value
         *         is returned. <code>null</code> otherwise.
         */
    public Object put(String key, Object value) {
	return _values.put(key, value);
    }

    /**
         * Creates a new sub configuration.
         * 
         * @param name
         *                of the sub configuration to be created.
         * @return a new sub configuration.
         */
    public InMemoryConfiguration createSub(String name) {
	InMemoryConfiguration returnValue = new InMemoryConfiguration(_path
		+ '/' + name);
	_values.put(name, returnValue);
	return returnValue;
    }

    /**
         * @see net.micwin.yajl.core.config.IConfiguration#keys()
         */
    public String[] keys() {
	return (String[]) _values.keySet().toArray(new String[_values.size()]);
    }

    /**
         * @see net.micwin.yajl.core.config.IConfiguration#getBoolean(java.lang.String,
         *      java.lang.Boolean)
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
         * @see net.micwin.yajl.core.config.IConfiguration#getInteger(java.lang.String,
         *      java.lang.Integer)
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
         * @see net.micwin.yajl.core.config.IConfiguration#getString(java.lang.String,
         *      java.lang.String)
         */
    public String getString(String key, String defaultString)
	    throws FormatException {
	try {
	    return getString(key);
	} catch (MissingConfigKeyException e) {
	    return defaultString;
	}
    }

    /**
         * @see net.micwin.yajl.core.config.IConfiguration#getArray(java.lang.String,
         *      java.lang.String[])
         */
    public String[] getArray(String key, String[] defaultArray)
	    throws FormatException {
	try {
	    return getArray(key);
	} catch (MissingConfigKeyException e) {
	    return defaultArray;
	}
    }
}