/**
 * 
 */
package net.micwin.yajl.core.config.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import net.micwin.tools4j.StringFactory;
import net.micwin.tools4j.data.StreamFactory;
import net.micwin.yajl.core.config.IConfiguration;
import net.micwin.yajl.core.config.IConfigurationBuilder;

/**
 * This XmlConfigurationBuilder klnows how to builds XmlConfiguration instances
 * out of untyped .xml-files.
 * 
 * @author michael.winkler@micwin.net
 * 
 */
public class XmlConfigurationBuilder implements IConfigurationBuilder {

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfigurationBuilder#accepts(java.lang.String)
         */
    public boolean accepts(String configUrl) {
	return configUrl.toUpperCase().endsWith(".XML");
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.yajl.core.config.IConfigurationBuilder#read(java.lang.String)
         */
    public IConfiguration read(String eUrl) throws IOException,
	    IllegalStateException {

	StringFactory sf = new StringFactory(1000);
	Reader br = new InputStreamReader(StreamFactory.inputStream(eUrl));
	sf.appendAll(br, null);
	br.close();
	try {
	    sf.resolveVars(System.getProperties());
	    return new XmlConfiguration(sf.toString());
	} catch (Exception e) {
	    throw new IllegalStateException("cannot load XmlConfiguration : "
		    + e.getMessage(), e);
	} catch (Error e) {
	    throw e;
	}
    }
}
