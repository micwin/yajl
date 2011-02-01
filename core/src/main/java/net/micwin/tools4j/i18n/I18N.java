package net.micwin.tools4j.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.micwin.tools4j.config.ConfigManager;
import net.micwin.tools4j.config.IConfiguration;
import net.micwin.tools4j.config.MissingConfigKeyException;

/**
 * This class manages multi language packs in a more flexible way than the java
 * libraries. And of course, there is a way to transparently access the
 * ConfgigManager.
 * 
 * @author michael.winkler@micwin.net
 * 
 */
public class I18N {

    static final Logger L = Logger.getLogger(I18N.class.getName());

    IConfiguration config;

    Map<String, I18NLocale> locales = new HashMap<String, I18NLocale>();

    /**
         * The locale if no locale is specified.
         */
    private I18NLocale defaultLocale;

    static I18N singleton = null;

    /**
         * Returns the singleton configured by ConfigManager. If not yet
         * configured, configure it.
         * 
         * @return
         */
    public static I18N getSingleton() {
	if (singleton == null) {
	    try {
		return initSingleton();
	    } catch (MissingConfigKeyException e) {
		throw new IllegalStateException("cannot configure i18n", e);
	    }
	} else {
	    return singleton;
	}
    }

    private static I18N initSingleton() throws MissingConfigKeyException {
	singleton = new I18N(ConfigManager.getSingletonConfig().sub("i18n"));
	return singleton;
    }

    /**
         * Creates a version of I18N independent from the singleton.
         * 
         * @param config
         * @throws MissingConfigKeyException
         */
    public I18N(IConfiguration config) {
	this.config = config;
	String defaultLocaleName = config.getString("defaultLocale", Locale
		.getDefault().toString());
	String[] locales = config.getArray("activeLocales", new String[0]);
	for (int i = 0; i < locales.length; i++) {
	    String localeName = locales[i];

	    I18NLocale locale = null;
	    try {

		locale = new I18NLocale(localeName, config.sub(localeName));

	    } catch (MissingConfigKeyException e) {
		L
			.log(
				Level.WARNING,
				"active locale '"
					+ localeName
					+ "' not having a configuration - takimg transient key mapper");
		locale = new I18NLocale(localeName);
	    }

	    // put into map
	    this.locales.put(localeName, locale);

	    // check for being default locale.
	    if (defaultLocaleName.equals(localeName)) {
		defaultLocale = locale;
	    }

	}

	if (defaultLocale == null) {
	    // the default locale is not active - try to load it anyway
	    try {
		defaultLocale = new I18NLocale(defaultLocaleName, config
			.sub(defaultLocaleName));
	    } catch (MissingConfigKeyException e) {
		L
			.log(
				Level.WARNING,
				"default locale '"
					+ defaultLocaleName
					+ "' not having a configuration - taking transient key mapper");
		defaultLocale = new I18NLocale(defaultLocaleName);

	    }

	    // put default locale into map
	    this.locales.put(defaultLocaleName, defaultLocale);
	}
    }

    /**
         * Returns the locale configured to be default.
         * 
         * @return
         */
    public static I18NLocale getDefaultLocale() {
	return getSingleton().defaultLocale;
    }

    /**
         * Returns the I18NLocale with the given name.
         * 
         * @param localeName
         * @return
         */
    public I18NLocale getLocale(String localeName) {
	return locales.get(localeName);
    }

    /**
         * short cut method for getting a specific key from the default locale.
         * 
         * @param key
         * @return
         * @see I18NLocale#translate(String)
         */
    public static String translate(String key) {
	return getDefaultLocale().translate(key);
    }
}
