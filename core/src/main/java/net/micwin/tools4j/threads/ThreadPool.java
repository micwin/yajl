/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * 
 * $Log: ThreadPool.java,v $
 * Revision 1.3  2007/08/30 07:31:37  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:25  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:28  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:28  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.2  2004/04/18 21:42:51  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.1  2004/04/16 12:12:28  recipient00
 * Initial check in
 *
 *  
 */

package net.micwin.tools4j.threads;

import net.micwin.tools4j.config.ConfigException;
import net.micwin.tools4j.config.IConfiguration;
import net.micwin.tools4j.frameworks.IFramework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A "Framework" managing a pool of threads.
 * 
 * @author micwin@gmx.org
 * @since 15.04.2004 21:38:20
 */
public class ThreadPool implements IFramework {

    // --------------------------------------------------------------
    // -- static field
    // --------------------------------------------------------------
    private static final Log L = LogFactory.getLog(ThreadPool.class);

    /**
         * Determines how many threads will be created on init time.
         */
    public static final String KEY_SIZE = "poolSize";

    /**
         * Determines how long a pooled thread should sleep before doing another
         * loop.
         */
    public static final String KEY_SLEEP_TIME = "sleepTime";

    /**
         * Determines wether we wait for threads when shutting down.
         */
    public static final String KEY_JOIN_ON_SHUTDOWN = "joinOnShutdown";

    /**
         * Determines wether or not we should use deamons instead of normal
         * threads.
         */
    public static final String KEY_USE_DEAMONS = "useDeamons";

    /**
         * Optional; determines the thread priority the pooled threads run with.
         */
    public static final String KEY_THREAD_PRIORITY = "threadPriority";

    // --------------------------------------------------------------
    // -- instance field
    // --------------------------------------------------------------
    /**
         * The pool where the threads are put in.
         */
    PooledThread[] _pool = null;

    /**
         * Determines wether or not a thread is joined when shutting down.
         */
    boolean _joinOnShutdown = false;

    /**
         * Creates a thread pool without any pooled threads so far.
         * 
         */
    public ThreadPool() {
	super();
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.tools4j.frameworks.IFramework#init(net.micwin.tools4j.config.IConfiguration)
         */
    public void init(IConfiguration configuration) throws ConfigException {
	if (L.isDebugEnabled())
	    L.debug("Initializing Thread pool ...");
	configuration.assertAvailable(new String[] { KEY_SIZE, KEY_SLEEP_TIME,
		KEY_JOIN_ON_SHUTDOWN, KEY_USE_DEAMONS });
	int poolSize = configuration.getInteger(KEY_SIZE).intValue();
	_pool = new PooledThread[poolSize];
	int sleepTime = configuration.getInteger(KEY_SLEEP_TIME).intValue();
	boolean useDeamons = configuration.getBoolean(KEY_USE_DEAMONS)
		.booleanValue();
	Integer threadPrio = configuration
		.getInteger(KEY_THREAD_PRIORITY, null);
	_joinOnShutdown = configuration.getBoolean(KEY_JOIN_ON_SHUTDOWN)
		.booleanValue();
	if (L.isDebugEnabled()) {
	    L.debug("creating "
		    + poolSize
		    + " threads with a sleep time of "
		    + sleepTime
		    + "ms "
		    + (threadPrio == null ? " with normal priority "
			    : "with thread priority " + threadPrio) + ", "
		    + (useDeamons ? "as deamons " : "as normal threads ")
		    + (!_joinOnShutdown ? "NOT " : "")
		    + "waiting on shutdown()");
	}
	for (int i = 0; i < poolSize; i++) {
	    _pool[i] = new PooledThread("Pooled Thread #" + (i + 1) + "/"
		    + poolSize, sleepTime);
	    _pool[i].setDaemon(useDeamons);
	}
	_joinOnShutdown = configuration.getBoolean(KEY_JOIN_ON_SHUTDOWN)
		.booleanValue();
	L.info("ThreadPool created containing " + _pool.length + " threads");
    }

    /**
         * Runs a runnable in a pooled thread.
         * 
         * @param r
         *                the runnable to run.
         * @return Wether or not r has been queued for execution.
         */
    public synchronized boolean run(Runnable r) {
	PooledThread t = null;
	for (int i = 0; i < _pool.length; i++) {
	    if (_pool[i].isEmpty()) {
		t = _pool[i];
		break;
	    }
	}
	if (t != null) {
	    t.setNextRunnable(r);
	    return true;
	} else {
	    return false;
	}
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.micwin.tools4j.frameworks.IFramework#shutDown()
         */
    public void shutDown() {
	// first, send shutdown signal to all
	for (int i = 0; i < _pool.length; i++) {
	    _pool[i].shutDown(false);
	}
	// shall we wait for threads ?!?
	if (_joinOnShutdown) {
	    for (int i = 0; i < _pool.length; i++) {
		try {
		    _pool[i].join();
		} catch (InterruptedException e) {
		    // Well since we're shutting down, we dont care what
		    // happened ...
		}
	    }
	}
	// clean up
	for (int i = 0; i < _pool.length; i++) {
	    _pool[i] = null;
	}
	_pool = null;
	L.info("ThreadPool shut down");
    }
}