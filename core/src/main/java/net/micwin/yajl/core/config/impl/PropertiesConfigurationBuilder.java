/**
 * 
 */
package net.micwin.yajl.core.config.impl;

import java.io.IOException;

import net.micwin.yajl.core.config.IConfiguration;
import net.micwin.yajl.core.config.IConfigurationBuilder;

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
         * @see net.micwin.yajl.core.config.IConfigurationBuilder#accepts(java.lang.String)
         */
    public boolean accepts(String eUrl) {
	eUrl = eUrl.toUpperCase();
	return eUrl.endsWith(".PROPS") || eUrl.endsWith(".PROPERTIES");
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfigurationBuilder#read(java.lang.String)
         */
    public IConfiguration read(String eUrl) throws IOException,
	    IllegalStateException {

	PropertiesConfiguration pc = new PropertiesConfiguration(eUrl);
	return pc;
    }

}
