package net.micwin.yajl.core.config.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.StringTokenizer;

import net.micwin.yajl.core.config.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implements a configuration that configures from System Properties.
 * 
 * @author micwin
 * 
 */
public class PropertiesConfigImpl implements Configuration {
	private static Log L = LogFactory.getLog(PropertiesConfigImpl.class);
	private final Properties props;

	public PropertiesConfigImpl() {
		this(System.getProperties());
	}

	public PropertiesConfigImpl(String propsUrl) throws MalformedURLException,
			IOException {

		props = new Properties(System.getProperties());
		InputStream propsStream = getClass().getClassLoader()
				.getResource(propsUrl).openStream();
		props.load(propsStream);
		propsStream.close();

		L.info("instantiated with url " + propsUrl + "'");
	}

	public PropertiesConfigImpl(Properties props) {
		this.props = props;
		L.info("instantiated.");
	}

	public String getString(String key) {
		return props.getProperty(key);
	}

	public String[] getStringArray(String key) {
		String value = getString(key);
		if (value == null) {
			return null;
		} else if ("".equals(value.trim())) {
			return new String[0];
		}
		StringTokenizer tokenizer = new StringTokenizer(value, ";");
		String[] returnValue = new String[tokenizer.countTokens()];
		for (int i = 0; i < returnValue.length; i++) {
			returnValue[i] = tokenizer.nextToken();
		}
		return returnValue;
	}
}
