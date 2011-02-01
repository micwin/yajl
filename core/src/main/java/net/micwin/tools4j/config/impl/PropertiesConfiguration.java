/*
 * $Log: PropertiesConfiguration.java,v $
 * Revision 1.5  2007/09/06 15:57:58  recipient00
 * reformatted code, removed some code quality flaws
 *
 * Revision 1.4  2007/09/06 07:38:21  recipient00
 * removed messed up chars
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
 * Revision 1.7.2.2  2007/02/14 22:39:24  recipient00
 * now path is of type HierarchyPath
 *
 * Revision 1.7.2.1  2006/12/29 12:07:56  recipient00
 * distinction between the configuration implementation and its build process
 *
 * Revision 1.7  2006/06/23 19:19:36  recipient00
 * Made a little Java 5 compatible
 *
 * Revision 1.6  2004/04/18 21:46:25  recipient00
 * changed author's email to micwin@gmx.org
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
 * Revision 1.2 2004/01/10 20:20:53 recipient00 Added todo tags for translation
 * and legal notes
 * 
 * Revision 1.1 2004/01/07 22:34:20 recipient00 initial check in
 * 
 * 
 * Revision 1.3 2003/12/30 21:13:18 micwin pseudo-Kollisionen :(
 * 
 * Revision 1.2 2003/12/16 09:32:12 micwin Reformattings
 * 
 * Revision 1.1 2003/12/09 20:16:57 micwin initial check in
 */

package net.micwin.tools4j.config.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.micwin.tools4j.StringFactory;
import net.micwin.tools4j.config.IConfiguration;
import net.micwin.tools4j.config.MissingConfigKeyException;
import net.micwin.tools4j.data.structures.HierarchyPath;

/**
 * Provides an implementation of IConfiguration that gets its values from a
 * Properties stream, containing textual key-value pairs. The values get parsed
 * into integer or boolean values as requested, depending on the method used to
 * get them. <br />
 * You can specify the used stream by providing a url. If this url starts with
 * <i>local: </i>, then the rest of the url is regarded to be a local file path.
 * If not, then the specified url is opened using a class loader. To
 * transparently enable sub group handling, sub configurations can be configured
 * and included by using keys of the form <code>pc.sub.groupName=url</code>,
 * where <code>subName</code> must be replaced by the sub group's name, and
 * <code>url</code> by the typical, url-like stream locator. The referenced
 * stream will be used to form a sub group that will be passed out when called
 * <code>pc.sub ("subName");</code>.<br />
 * The PropertiesConfiguration can be configured in multiple ways :
 * <ul>
 * <li><code>pc.separator</code> Determines the character(s) used to tokenize
 * array values. Defaults to <code>,</code> if omitted.</li>
 * <li><code>pc.trimStrings</code> If set to true, then strings will be
 * trimmed before returned or interpreted as integers or booleans. Defaults to
 * <code>true</code> if omitted.</li>
 * <li><code>pc.baseUrl</code> If set, then urls starting with <code>*</code>
 * will be prefixed by the specified value of this key. The askerisk will be
 * removed.</li>
 * </ul>
 * 
 * @author micwin@gmx.org
 * @since 09.12.2003 16:56:45
 */
public final class PropertiesConfiguration extends AConfiguration {

    // --------------------------------------------------------------
    // -- static field
    // --------------------------------------------------------------
    /**
         * path of the top level group.
         */
    public static final HierarchyPath ROOT_PATH = new HierarchyPath("//", '/',
	    new String[] { "pc" });

    /**
         * Prefix for administrative keys of PropertiesConfiguration..
         */
    public static final String KEY_PC_PREFIX = "pc.";

    /**
         * Administrative key to include other properties as sub configurations.
         */
    public static final String KEY_SUB_PREFIX = KEY_PC_PREFIX + "sub.";

    /**
         * Key to specify a base url. set this to <code>local:</code> and
         * prefix pc.sub.* values with <code>*</code> if you want to force
         * urls to be local paths instead of urls. Change back to
         * <code>http://properties.server:port/baseDir/</code> to switch back
         * to common url behavior.
         */
    public static final String KEY_BASE_URL = KEY_PC_PREFIX + "baseUrl";

    /**
         * Key to the separator used to tokenize array values.
         */
    public static final String KEY_SEPARATOR = KEY_PC_PREFIX + "separator";

    /**
         * This value defines wether values will be trimmed before
         * interpretation as integer /boolean /array elements and before passed
         * out.
         */
    public static final String KEY_TRIM_STRINGS = KEY_PC_PREFIX + "trimStrings";

    Map<String, Object> _keys;

    boolean _stringTrimming;

    String _baseUrl;

    /**
         * Creates a new PropertiesConfiguration reading all needed values from
         * a properties. Very useful to initialise a base configuration from
         * <code>System.getProperties()</code>: You could store the actaul
         * configuration in a sub configuration, for example, named
         * <code>config</code>, linked in via a system property
         * <code>pc.sub.config=local:./config.properties</code>.
         * 
         * @param prop
         *                A set of properties to be used and accessed with this
         *                configuration group.
         * @param keyResolver
         *                Keys passed in via getString(String) et al will be
         *                searched and resolved for shell style variable names
         *                like ${name}. These occurrences will be replaced by
         *                values taken from <code>keyResolver</code>, where
         *                <code>name</code> serves as key to the map.
         * @param valueResolver
         *                Does for values what keyResolver does for keys.
         *                resolving happens <i>before </i> interpretation as
         *                integers and booleans and before passing out, but
         *                <i>after </i> trimming.
         * @throws IOException
         *                 Stream not found, unable to open or something else.
         */
    public PropertiesConfiguration(Properties prop, Properties keyResolver,
	    Properties valueResolver) throws IOException {
	this(ROOT_PATH, prop, keyResolver, valueResolver);
    }

    /**
         * Creates a new PropertiesConfiguration reading all needed values from
         * a properties. Very useful to initialise a base configuration from
         * <code>System.getProperties()</code>: You could store the actaul
         * configuration in a sub configuration, for example, named
         * <code>config</code>, linked in via a system property
         * <code>pc.sub.config=local:./config.properties</code>.
         * 
         * @param url
         *                The source from which this configuration should load..
         * @param keyResolver
         *                Keys passed in via getString(String) et al will be
         *                searched and resolved for shell style variable names
         *                like ${name}. These occurrences will be replaced by
         *                values taken from <code>keyResolver</code>, where
         *                <code>name</code> serves as key to the map.
         * @param valueResolver
         *                Does for values what keyResolver does for keys.
         *                resolving happens <i>before </i> interpretation as
         *                integers and booleans and before passing out, but
         *                <i>after </i> trimming.
         * @throws IOException
         *                 If the url could not be processed.
         */
    public PropertiesConfiguration(String url, Properties keyResolver,
	    Properties valueResolver) throws IOException {
	this(ROOT_PATH, url, null, keyResolver, valueResolver);
    }

    /**
         * Convenience replacement for
         * <code>PropertiesConfiguration (url, null, System.getProperties())</code>.
         * Written out this means this builds a configuration that reads its
         * data from a specified url, does no key resolving and takes its value
         * resolving from the system properties.
         * 
         * @param url
         *                The url to load the configuration from.
         * @see PropertiesConfiguration#PropertiesConfiguration(String,
         *      Properties, Properties)
         * @throws IOException
         *                 If this url could not be processed.
         */
    public PropertiesConfiguration(String url) throws IOException {
	this(url, (Properties) null, System.getProperties());
    }

    /**
         * Creates a sub-configuration from an url. This is a sole internal
         * method and should not be called for purposes farer than testing.
         * 
         * @param path
         *                The configuration path to store..
         * @param url
         *                The url to load configuration and administrative keys
         *                and values from.
         * @param baseUrl
         *                A base url to take. Can differ for that groub, and
         *                will be overwritten for that group and sub groups, if
         *                the configuration stream declares another value.
         * @param keyResolver
         *                Optional references to resolve in keys. may be
         *                <code>null</code>.
         * @param valueResolver
         *                Optional references to resolve in values. may be
         *                <code>null</code>.
         * @throws IOException
         *                 If something is wrong with the configuration stream.
         */
    PropertiesConfiguration(HierarchyPath path, String url, String baseUrl,
	    Properties keyResolver, Properties valueResolver)
	    throws IOException {
	super(path, null, keyResolver, valueResolver);
	_baseUrl = baseUrl;
	InputStream in = null;
	in = openStream(url);
	Properties props = new Properties();
	props.load(in);
	in.close();
	configure(props);
    }

    /**
         * Creates a sub-configuration from an existing instance of
         * java.util.Properties. This is a sole internal method and should not
         * be called for purposes farer than testing.
         * 
         * @param path
         *                The configuration path to store..
         * @param prop
         *                The properties instance to take configuration and
         *                administrative keys and values from.
         * @param keyResolver
         *                Optional references to resolve in keys. may be
         *                <code>null</code>.
         * @param valueResolver
         *                Optional references to resolve in values. may be
         *                <code>null</code>.
         * @throws IOException
         *                 If something is wrong with the inbound sub
         *                 configuration respective the specified urls.
         */
    PropertiesConfiguration(HierarchyPath path, Properties prop,
	    Properties keyResolver, Properties valueResolver)
	    throws IOException {
	super(path, null, keyResolver, valueResolver);
	configure(prop);
    }

    /**
         * Loads and configures using the passed instance of Properties.
         * 
         * @see Properties
         * @param prop
         * @throws IOException
         *                 If something went wrong.
         */
    private void configure(Properties prop) throws IOException {
	setSeparator(prop.getProperty(KEY_SEPARATOR, getSeparator()));
	_stringTrimming = Boolean.valueOf(
		prop.getProperty(KEY_TRIM_STRINGS, "true")).booleanValue();
	_baseUrl = prop.getProperty(KEY_BASE_URL, _baseUrl);
	_keys = new HashMap<String, Object>(); // untergruppen

	// konfigurieren
	// keys laden
	for (Enumeration keysEnum = prop.keys(); keysEnum.hasMoreElements();) {
	    String key = keysEnum.nextElement().toString();
	    if (key.startsWith(KEY_PC_PREFIX)) {
		if (key.startsWith(KEY_SUB_PREFIX)) { // Untergruppe laden
		    String name = key.substring(KEY_SUB_PREFIX.length());
		    String url = prop.getProperty(key).trim();
		    PropertiesConfiguration sub = new PropertiesConfiguration(
			    _path.createChild(name), url, _baseUrl,
			    _keyResolver, _valuesResolver);
		    _keys.put(name, sub);
		}
	    } else {
		String value = prop.getProperty(key);
		if (_stringTrimming)
		    value = value.trim();
		_keys.put(key, value);
	    }
	}
    }

    /**
         * @see net.micwin.tools4j.config.IConfiguration#getString(java.lang.String)
         */
    public String getString(String key) throws MissingConfigKeyException {
	String value = (String) _keys.get(key);
	if (value == null) {
	    throw new MissingConfigKeyException(_path.getPathString(), key);
	}
	try {
	    // Schaumermal, ob wir den resolven k���nnen ...
	    value = StringFactory.resolveVars(value, System.getProperties());
	} catch (ParseException e) {
	    // kein resolve, also übernehmen wir ihn "einfach so"
	    e.printStackTrace();
	}
	return value;
    }

    /**
         * @see net.micwin.tools4j.config.IConfiguration#sub(java.lang.String)
         */
    public IConfiguration sub(String key) throws MissingConfigKeyException {
	IConfiguration sub = (IConfiguration) _keys.get(key);
	if (sub == null) {
	    throw new MissingConfigKeyException(getPathString(), key);
	}
	return sub;
    }

    /**
         * @see AConfiguration#isStringTrimming()
         */
    public boolean isStringTrimming() {
	return _stringTrimming;
    }

    /**
         * @see net.micwin.tools4j.config.IConfiguration#keys()
         */
    public String[] keys() {
	return (String[]) _keys.keySet().toArray(new String[0]);
    }

    /**
         * @see net.micwin.tools4j.config.impl.AConfiguration#getBaseUrl()
         */
    public String getBaseUrl() {
	return _baseUrl;
    }
}