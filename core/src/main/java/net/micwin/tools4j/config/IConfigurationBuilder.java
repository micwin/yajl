package net.micwin.tools4j.config;

import java.io.IOException;

/**
 * A configuration builder abstracts the representation of a configuration
 * instance in the memory from its persistent format.
 * 
 * @author michael.winkler@micwin.net
 * 
 */
public interface IConfigurationBuilder {

    /**
         * Checks wether or not this Configuration builder can handle the
         * specified config.
         * 
         * @param configUrl
         *                A url pointing on a config to check.
         * @return
         */
    boolean accepts(String configUrl);

    /**
         * Reads the specified url and creates an appropriate IConfiguration
         * instance.
         * 
         * @param configUrl
         *                An extenden URL to point to a configuration.
         * @return
         * @throws IOException
         *                 If IO issues prevent this builder to perform its job.
         * @throws IllegalStateException
         *                 If configurative issues (missing class files etc)
         *                 prevent this builder to perform its job.
         */
    IConfiguration read(String configUrl) throws IOException,
	    IllegalStateException;

}
