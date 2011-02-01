/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * 
 * $Log: CounterRunnable.java,v $
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
 * Revision 1.2  2004/04/18 21:42:51  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.1  2004/04/16 12:12:28  recipient00
 * Initial check in
 *
 *  
 */

package net.micwin.yajl.core.threads;

/**
 * A Class that provides sort ot a tester runnable. You can specify a sleep
 * time. This time will be "spent" each loop.
 * 
 * @author micwin@gmx.org
 * @since 15.04.2004 23:12:44
 */
public class CounterRunnable implements Runnable {

    private int _runCounter = 0;

    private long _sleepTime = -1;

    /**
         * Initializes a new runnable.
         * 
         * @param sleepTime
         *                THe millis the runnable sleeps each run.
         */
    protected CounterRunnable(long sleepTime) {
	super();
	_sleepTime = sleepTime;
    }

    /**
         * Returns how often the runnable has been launched til now.
         * 
         * @return
         */
    public int getRunCounter() {
	synchronized (this) {
	    return _runCounter;
	}
    }

    /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
    public void run() {
	synchronized (this) {
	    _runCounter++;
	}
	if (_sleepTime > 0) {
	    try {
		Thread.sleep(_sleepTime);
	    } catch (InterruptedException e) {
		// ??? this shouldnt happen. Lets see ...
		e.printStackTrace();
	    }
	}
    }
}