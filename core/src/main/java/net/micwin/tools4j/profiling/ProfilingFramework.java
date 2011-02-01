/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * TODO copyright legal notes for final license model
 *
 * $Log: ProfilingFramework.java,v $
 * Revision 1.3  2007/08/30 07:31:37  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:25  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:29  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:29  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.2  2004/11/24 13:57:36  recipient00
 * Some convenience methods
 *
 * Revision 1.1  2004/08/26 11:26:36  recipient00
 * A powerful profiler and a fitting framework-class
 *
 *  
 */
package net.micwin.tools4j.profiling;

import net.micwin.tools4j.frameworks.IFramework;
import net.micwin.yajl.core.config.ConfigException;
import net.micwin.yajl.core.config.IConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO Class ProfilerFramework needs some comment.
 * 
 * @author micwin@gmx.org
 * @since 26.08.2004
 */
public final class ProfilingFramework implements IFramework {

	static final Log L = LogFactory.getLog(ProfilingFramework.class);

	/**
         * 
         */
	public ProfilingFramework() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.micwin.tools4j.frameworks.IFramework#init(net.micwin.yajl.core.config
	 * .IConfiguration)
	 */
	public void init(IConfiguration initialConfig) throws ConfigException {
		L.info("initializing ...");

		ProfilingContext.configure(initialConfig);
		L.info("initialized (" + ProfilingContext.KEY_ENABLED + '='
				+ ProfilingContext.isProfilingEnabled() + ", "
				+ ProfilingContext.KEY_STATE_LENIENT + '='
				+ ProfilingContext._stateLenient + ')');
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.micwin.tools4j.frameworks.IFramework#shutDown()
	 */
	public void shutDown() {
		L.info("shutDown");
	}

}
