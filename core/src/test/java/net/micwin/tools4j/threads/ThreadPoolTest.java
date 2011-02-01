/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * 
 * $Log: ThreadPoolTest.java,v $
 * Revision 1.3  2007/08/30 07:31:38  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:26  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:31  recipient00
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

import junit.framework.TestCase;
import net.micwin.tools4j.config.ConfigException;
import net.micwin.tools4j.config.impl.InMemoryConfiguration;

/**
 * TODO Add some useful comment here
 * 
 * @author micwin@gmx.org
 * @since 15.04.2004 22:53:28
 */
public class ThreadPoolTest extends TestCase {

    /*
         * (non-Javadoc)
         * 
         * @see junit.framework.TestCase#setUp()
         */
    protected void setUp() throws Exception {
	super.setUp();
    }

    /**
         * Tests the standard constructor.
         * 
         */
    public void testThreadPool() {
	ThreadPool tp = new ThreadPool();
	assertNull("pool yet null", tp._pool);
    }

    /**
         * Tests wether init and shutdown runs with a proper configuration.
         * 
         * @throws ConfigException
         */
    public void testInitShutdown_Legacy() throws ConfigException {
	InMemoryConfiguration imc = new InMemoryConfiguration();
	imc.put(ThreadPool.KEY_SIZE, new Integer(5));
	imc.put(ThreadPool.KEY_SLEEP_TIME, new Integer(250));
	imc.put(ThreadPool.KEY_JOIN_ON_SHUTDOWN, Boolean.TRUE);
	imc.put(ThreadPool.KEY_USE_DEAMONS, Boolean.FALSE);
	ThreadPool tp = new ThreadPool();
	tp.init(imc);
	assertNotNull("pool created", tp._pool);
	assertEquals("correct pool size", 5, tp._pool.length);
	for (int i = 0; i < tp._pool.length; i++) {
	    assertNotNull("Element #" + i + " not null", tp._pool[i]);
	    assertFalse("Thread  #" + i + " not yet started running",
		    tp._pool[i]._started);
	    assertNull("Element #" + i + " not yet queued",
		    tp._pool[i]._customRunnable);
	}
	// test shutdown
	// saving thread references for checking
	PooledThread[] poolSave = new PooledThread[tp._pool.length];
	System.arraycopy(tp._pool, 0, poolSave, 0, tp._pool.length);
	// shutting down
	tp.shutDown();
	assertNull("pool cleaned", tp._pool);
	for (int i = 0; i < poolSave.length; i++) {
	    assertTrue(" check threads for shutdown ", poolSave[i]._shutdown);
	}
    }

    /**
         * Tests wether all runs fine.
         * 
         * @throws ConfigException
         */
    public void testRun() throws ConfigException {
	InMemoryConfiguration imc = new InMemoryConfiguration();
	imc.put(ThreadPool.KEY_SIZE, new Integer(5));
	imc.put(ThreadPool.KEY_SLEEP_TIME, new Integer(1000));
	imc.put(ThreadPool.KEY_JOIN_ON_SHUTDOWN, Boolean.TRUE);
	imc.put(ThreadPool.KEY_USE_DEAMONS, Boolean.FALSE);
	ThreadPool tp = new ThreadPool();
	tp.init(imc);
	// fill pool
	CounterRunnable[] runnables = new CounterRunnable[tp._pool.length];
	for (int i = 0; i < runnables.length; i++) {
	    runnables[i] = new CounterRunnable(1000);
	    assertTrue("queued/launched thread #" + i, tp.run(runnables[i]));
	}
	try {
	    // wait some secs
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    fail("unexpected exception thrown");
	}
	// shutDown all
	tp.shutDown();
	// now : did i run all ?
	for (int i = 0; i < runnables.length; i++) {
	    assertEquals("run thread #" + i, 1, runnables[i].getRunCounter());
	}
    }
}