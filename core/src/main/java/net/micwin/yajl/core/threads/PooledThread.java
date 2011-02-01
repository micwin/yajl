/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * 
 * $Log: PooledThread.java,v $
 * Revision 1.4  2007/09/06 15:57:59  recipient00
 * reformatted code, removed some code quality flaws
 *
 * Revision 1.3  2007/08/30 07:31:37  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:26  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:28  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:28  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.2.2.1  2006/12/29 12:05:15  recipient00
 * introducing generics
 *
 * Revision 1.2  2004/04/18 21:42:51  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.1  2004/04/16 12:12:28  recipient00
 * Initial check in
 *
 *  
 */

package net.micwin.yajl.core.threads;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A Thread that can switch Runnables and get pooled by ThreadPool.
 * 
 * @author micwin@gmx.org
 * @since 15.04.2004 21:43:05
 */
final class PooledThread extends Thread {

    // --------------------------------------------------------------
    // -- static field
    // --------------------------------------------------------------
    static final Log L = LogFactory.getLog(PooledThread.class);

    // --------------------------------------------------------------
    // -- instance field
    // --------------------------------------------------------------
    /**
         * While a runnable is in execution, you can set another runnable as
         * next.
         */
    protected Runnable _customRunnable = null;

    /**
         * If this is true, then the thread should shutdown after finishing the
         * actual job.
         */
    protected boolean _shutdown = false;

    /**
         * Number of millis to sleep after job.
         */
    protected long _sleepMillis = -1;

    /**
         * Determines wether or not a custom runnable is being processed.
         */
    protected boolean _isRunning = false;

    /**
         * Determines wether or not start () has been called.
         */
    protected boolean _started = false;

    /**
         * Disabled constructor.
         * 
         */
    private PooledThread() {
	super();
    }

    /**
         * Creates a new pooleable Thread with a given sleep time an d thread
         * name.
         * 
         * @param name
         * @param sleepTime
         */
    public PooledThread(String name, long sleepTime) {
	super(name);
	_sleepMillis = sleepTime;
	if (L.isDebugEnabled()) {
	    L.debug("PooledThread created.");
	}
    }

    /**
         * This is the loop that handles the custom runnable.
         * 
         * @see java.lang.Thread#run()
         */
    public void run() {
	if (L.isDebugEnabled()) {
	    L.debug("running ...");
	}
	// starting an endless loop
	while (true) {
	    // to make a short synchronized block, assign field to another
	    // variable and nullify field
	    Runnable runner = null;
	    synchronized (this) {
		if (_customRunnable != null) {
		    runner = _customRunnable;
		    _customRunnable = null;
		}
	    }
	    // do we have something to run ?!?
	    if (runner != null) {
		// well then ...
		// set running flag
		synchronized (this) {
		    _isRunning = true;
		}
		// run runnable OUTSIDE the sync block, which is the point of
		// this excercise.
		try {
		    runner.run();
		} catch (RuntimeException e) {
		    // we catch RuntimeExsceptions, pass
		    // them out to logging an console, and continue
		    // functioning.
		    e.printStackTrace();
		    L.fatal("Something went wrong while executing "
			    + "a custom runnable", e);
		}
		// unset running flag
		synchronized (this) {
		    _isRunning = false;
		}
	    } else {
		// nothing to run, so sleep.
		try {
		    sleep(_sleepMillis);
		} catch (InterruptedException e) {
		    // if this happens, then either a new custom runnable is
		    // available or shutdown has been initiated.
		}
	    }
	    // check wether to shutdown (which means leaving the while
	    // block)
	    synchronized (this) {
		if (_shutdown) {
		    // finish up
		    _customRunnable = null;
		    if (L.isDebugEnabled()) {
			L.debug("leaving run() ...");
		    }
		    return;
		}
	    }
	}
    }

    /**
         * Sets a new Runnable to launch. This can happen while another custom
         * runnable is working, but must not happen if another customRunnable
         * waits for execution.
         * 
         * @param newCustomRunnable
         *                a new customRunnable to wait for execution.
         * @throws IllegalStateException
         *                 If another customRunnable already is set.
         */
    public void setNextRunnable(Runnable newCustomRunnable)
	    throws IllegalStateException {
	synchronized (this) {
	    if (_customRunnable != null) {
		// this must not happen !
		throw new IllegalStateException(
			"another customRunnable already waiting");
	    } else {
		_customRunnable = newCustomRunnable;
	    }
	    // already started ?!?
	    if (!_started) {
		this.start();
	    } else {
		// if sleeping, wake it up.
		this.interrupt();
	    }
	}
    }

    /**
         * Shuts this thread down.
         * 
         * @param join
         */
    public void shutDown(boolean join) {
	// since we are coming from another thread, we have to synchronize in
	// order to access fields
	synchronized (this) {
	    if (_shutdown) {
		L.warn("this thread already shutting down!");
	    } else {
		if (L.isDebugEnabled()) {
		    L.debug("shutting down thread ...");
		}
		_shutdown = true;
		_customRunnable = null;
		// if the thread is sleeping, wake it up.
		this.interrupt();
	    }
	}
	// should we join ?!?
	if (join) {
	    try {
		if (L.isDebugEnabled()) {
		    L.debug("joining thread ...");
		}
		this.join();
	    } catch (InterruptedException e) {
		// we dont mind wether we cancel join.
	    }
	}
    }

    /**
         * Checks wether this thread has neither a queued runnable nor a thing
         * running.
         * 
         * @return
         */
    public boolean isEmpty() {
	synchronized (this) {
	    return !_isRunning && (_customRunnable == null);
	}
    }

    /**
         * Starts the thread and sets a flag. If already started, then do
         * nothing.
         * 
         * @see java.lang.Thread#start()
         */
    public synchronized void start() {
	synchronized (this) {
	    if (!_started) {
		if (L.isDebugEnabled()) {
		    L.debug(".start()ing ...");
		}
		super.start();
		_started = true;
	    } else {
		L.warn(".start () : Thread already started!");
	    }
	}
    }
}