/**
 * 
 */
package net.micwin.yajl.core.config;

import java.io.IOException;
import java.text.ParseException;

import net.micwin.tools4j.StringFactory;
import net.micwin.yajl.core.config.impl.PropertiesConfiguration;
import net.micwin.yajl.core.config.impl.PropertiesConfigurationBuilder;
import net.micwin.yajl.core.config.impl.XmlConfiguration;
import net.micwin.yajl.core.config.impl.XmlConfigurationBuilder;

/**
 * The config manager is a seemless factory for IConfiguration, and manages a
 * central config file. At first access, it is initialized by the system
 * property <code>tools4j.cm.config</code>, where a place is described from
 * which the config should load. The property is in the typical, extended url
 * (url plus additional protocol "local:").<br />
 * Which configuration implementation is taken depends upon the file extension
 * of the url:
 * <ul>
 * <li><b>.xml</b> is activating the XmlConfiguration</li>
 * <li>anything else is resulting in the PropertiesConfiguration to be taken.</li>
 * </ul>
 * 
 * @see {@link IConfiguration} see {@link IConfigurationBuilder}
 * @see {@link XmlConfiguration}
 * @see {@link XmlConfigurationBuilder}
 * @see {@link PropertiesConfiguration}
 * @see {@link PropertiesConfigurationBuilder}
 * 
 * @author michael.winkler@micwin.net
 * 
 */
public class ConfigManager {

    /**
         * If this system property is set, then additional outputs are done.
         */
    public static final String DEBUG_KEY = "tools4j.debug";

    /**
         * Set this system property (<code>tools4j.cm.config</code>) to a
         * xml configuration file for this config manager to read. The
         * property's value can contain references to other system properties
         * like <code>${user.home}</code>.
         */
    public static final String CONFIG_KEY = "tools4j.cm.config";

    private static final IConfigurationBuilder[] CONFIG_BUILDERS = {
	    new XmlConfigurationBuilder(), new PropertiesConfigurationBuilder() };

    static IConfiguration _singleton = null;

    /**
         * Checks wether or not the singleton already has been configured.
         * 
         * @return
         */
    public static boolean isSingletonConfigured() {
	return _singleton != null;
    }

    public static IConfiguration getSingletonConfig() {
	if (_singleton == null) {
	    // initialize singleton instance
	    initSingleton();
	}

	return _singleton;
    }

    /**
         * Initializes the singleton instance. Can only be called once, another
         * call leads to an IllegalStateException.
         */
    private static void initSingleton() {

	if (_singleton != null) {
	    throw new IllegalStateException("ConfigManager already initialized");
	}

	if (Boolean.getBoolean(DEBUG_KEY)) {
	    System.out.println(DEBUG_KEY + " enabled. System properties:");
	    System.getProperties().list(System.out);
	}
	// retrieve configured configUrl
	String configUrl = System.getProperties().getProperty(CONFIG_KEY);
	if (configUrl == null) {
	    throw new IllegalStateException("mandatory system property '"
		    + CONFIG_KEY + "' is empty or config manager disabled");
	}

	// parse system variables in this url
	try {
	    configUrl = StringFactory.resolveVars(configUrl, System
		    .getProperties());
	} catch (ParseException e) {
	    throw new IllegalStateException("cannot load configuration url '"
		    + configUrl + "' : parse error while resolving vars", e);
	}

	// load configuration
	System.out
		.println("CONFIG-MANAGER - loading singleton config with url '"
			+ configUrl + "' ...");
	try {
	    _singleton = loadConfig(configUrl);
	} catch (IOException e) {
	    throw new IllegalStateException("cannot load configuration url '"
		    + configUrl + "' : " + e.getMessage(), e);

	}
	if (_singleton == null) {
	    // url does not resuolve to a target
	    throw new IllegalStateException("cannot load configuration url '"
		    + configUrl + "' : url does not resolve to a legal target");
	}
    }

    /**
         * Loads the specified config. This is done in the following manner:
         * <ul>
         * <li>Analyze the file type</li>
         * <li>looks up appropriate config builder</li>
         * <li>faciliates config builder to read the data</li>
         * </ul>
         * 
         * @param eUrl
         *                a extended url to a config file to read from.
         * @return an instance of IConfiguration, if an appropriate builder has
         *         been found. Otherwise <code>null</code>.
         * @throws IOException
         *                 If the specified URL could not be opened due to IO
         *                 issues.
         * @throws IllegalStateException
         *                 If the specified config could not be created due to
         *                 configurative issues (missing modules etc).
         */
    public static IConfiguration loadConfig(String eUrl) throws IOException {
	for (int i = 0; i < CONFIG_BUILDERS.length; i++) {
	    IConfigurationBuilder b = CONFIG_BUILDERS[i];
	    if (b.accepts(eUrl)) {
		return b.read(eUrl);
	    }
	}
	return null;
    }
}
