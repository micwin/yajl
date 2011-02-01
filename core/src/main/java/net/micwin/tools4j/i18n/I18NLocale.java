/**
 * 
 */
package net.micwin.tools4j.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.micwin.yajl.core.config.IConfiguration;
import net.micwin.yajl.core.config.MissingConfigKeyException;
import net.micwin.yajl.core.data.StreamFactory;

/**
 * A container for specific locale data.
 * 
 * @author michael.winkler@micwin.net
 * 
 */
public class I18NLocale {

    static Logger L = Logger.getLogger(I18NLocale.class.getName());

    String localeName;

    Properties keys;

    boolean isTransient = false;

    /**
         * Creates a new locale.
         * 
         * @param localeName
         * @param config
         */
    public I18NLocale(String localeName, IConfiguration config) {
	this.localeName = localeName;
	keys = new Properties();
	if (config == null) {
	    // making a transient locale - returning only the keys with
	    // locale
	    isTransient = true;
	    return;
	}

	try {
	    InputStream in = StreamFactory.inputStream(config.getString("url"));
	    keys.load(in);
	} catch (MissingConfigKeyException e) {
	    L.log(Level.WARNING, "cannot access configuration for locale '"
		    + localeName + "'", e);

	    isTransient = true;
	} catch (IOException e) {
	    L.log(Level.WARNING, "cannot load keys of locale '" + localeName
		    + "'", e);
	    isTransient = true;
	}
    }

    /**
         * Creates an empty locale.
         * 
         * @param localeName
         */
    public I18NLocale(String localeName) {
	this(localeName, null);
    }

    /**
         * Translates the specified key into the language denoted by this
         * locale.
         * 
         * @param key
         * @return
         */
    public String translate(String key) {
	String value = keys.getProperty(key);
	if (value == null) {
	    value = localeName + ':' + key;
	    if (!isTransient) {
		L.log(Level.WARNING, "unknown key : " + value);
	    }
	}
	return value;
    }
}
