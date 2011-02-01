/**
 * 
 */
package net.micwin.tools4j.config.impl;

import java.io.IOException;

import net.micwin.tools4j.config.IConfiguration;
import net.micwin.tools4j.config.IConfigurationBuilder;

/**
 * A IConfigurationBuilder to handle configurations stored in properties files.
 * 
 * @author michael.winkler@micwin.net
 * @see PropertiesConfiguration
 */
public class PropertiesConfigurationBuilder implements IConfigurationBuilder {

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.tools4j.config.IConfigurationBuilder#accepts(java.lang.String)
         */
    public boolean accepts(String eUrl) {
	eUrl = eUrl.toUpperCase();
	return eUrl.endsWith(".PROPS") || eUrl.endsWith(".PROPERTIES");
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.tools4j.config.IConfigurationBuilder#read(java.lang.String)
         */
    public IConfiguration read(String eUrl) throws IOException,
	    IllegalStateException {

	PropertiesConfiguration pc = new PropertiesConfiguration(eUrl);
	return pc;
    }

}
