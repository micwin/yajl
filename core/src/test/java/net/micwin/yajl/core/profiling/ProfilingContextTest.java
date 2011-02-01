/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * TODO copyright legal notes for final license model
 *
 * $Log: ProfilingContextTest.java,v $
 * Revision 1.3  2007/08/30 07:31:31  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:19  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.2  2007/06/04 04:55:06  recipient00
 * Fixed some unit test issues
 *
 * Revision 1.1.2.1  2007/06/03 13:15:30  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:29  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.1  2004/08/26 11:26:36  recipient00
 * A powerful profiler and a fitting framework-class
 *
 *  
 */
package net.micwin.yajl.core.profiling;

import net.micwin.yajl.core.config.ConfigException;
import net.micwin.yajl.core.config.impl.InMemoryConfiguration;
import junit.framework.TestCase;

/**
 * TODO Class ProfilingContextTest needs some comment.
 * 
 * @author micwin@gmx.org
 * @since 26.08.2004
 */
public class ProfilingContextTest extends TestCase {

    /**
         * Constructor for ProfilingContextTest.
         * 
         * @param arg0
         */
    public ProfilingContextTest(String arg0) {
	super(arg0);
    }

    /*
         * @see TestCase#setUp()
         */
    protected void setUp() throws Exception {
	super.setUp();
	ProfilingContext._initiallyEnabled = false;
	ProfilingContext._root = new ProfilingContext(false, true, "");

    }

    /*
         * @see TestCase#tearDown()
         */
    protected void tearDown() throws Exception {
	super.tearDown();
    }

    public void testConfigure() throws ConfigException {

	InMemoryConfiguration imc = new InMemoryConfiguration();

	imc.put(ProfilingContext.KEY_ENABLED, "true");
	imc.put(ProfilingContext.KEY_STATES, new String[] {
		"net.micwin.tools4j: false : true ",
		"net.micwin.tools4j.profiling.*: true",
		" " + this.getClass().getName() + " :false" });
	ProfilingContext.configure(imc);

	// globally enabled
	assertTrue(ProfilingContext._initiallyEnabled);

	// package net.micwin.tools4j disabled
	assertTrue(!ProfilingContext.getContext(
		new String[] { "net", "micwin", "tools4j" }).isEnabled());

	// package net.micwin.tools4j.profiling enabled
	assertTrue(ProfilingContext.getContext(
		new String[] { "net", "micwin", "tools4j", "profiling" })
		.isEnabled());

	// this class disabled
	assertTrue(!ProfilingContext.getContext(ProfilingContextTest.class)
		.isEnabled());
    }

    public void testIsProfilingEnabled() {

	ProfilingContext._initiallyEnabled = false;
	assertTrue(!ProfilingContext.isProfilingEnabled());
	ProfilingContext._initiallyEnabled = true;
	assertTrue(ProfilingContext.isProfilingEnabled());
    }

    public void testSetProfilingEnabled() {
	ProfilingContext.setProfilingEnabled(true);
	assertTrue(ProfilingContext._initiallyEnabled);
	ProfilingContext.setProfilingEnabled(false);
	assertTrue(!ProfilingContext._initiallyEnabled);
    }

    /*
         * Test for ProfilingContext getContext(String[])
         */
    public void testGetContextStringArray() {
	ProfilingContext one = new ProfilingContext(true, true, "");
	ProfilingContext._root._sub.put("one", one)  ; 
	assertSame(one, ProfilingContext.getContext(new String[] { "one" }));
	ProfilingContext two = one.getSub("two");
	assertSame(two, ProfilingContext
		.getContext(new String[] { "one", "two" }));
	ProfilingContext three = two.getSub("three");
	assertSame(three, ProfilingContext.getContext(new String[] { "one",
		"two", "three" }));
    }

    /*
         * Test for ProfilingContext getContext(Class)
         */
    public void testGetContextClass() {

	ProfilingContext ctxByClass = ProfilingContext
		.getContext(ProfilingContextTest.class);
	ProfilingContext ctxByPath = ProfilingContext
		.getContext(new String[] { "net", "micwin", "yajl", "core" ,
			"profiling", "ProfilingContextTest" });
	assertSame(ctxByClass, ctxByPath);

    }

    /*
         * Test for ProfilingContext getContext(Class, String)
         */
    public void testGetContextClassString() {

	ProfilingContext ctxByClass = ProfilingContext.getContext(
		ProfilingContextTest.class, "someSub");
	ProfilingContext ctxByPath = ProfilingContext.getContext(new String[] {
		"net", "micwin", "yajl", "core" , "profiling",
		"ProfilingContextTest", "someSub" });
	assertSame(ctxByClass, ctxByPath);

    }

    /*
         * Test for ProfilingContext getContext(Package)
         */
    public void testGetContextPackage() {
	ProfilingContext ctxByPackage = ProfilingContext
		.getContext(ProfilingContextTest.class.getPackage());
	ProfilingContext ctxByPath = ProfilingContext.getContext(new String[] {
		"net", "micwin", "yajl", "core" , "profiling" });
	assertSame(ctxByPackage, ctxByPath);
    }

    public void testProfilingContext() {

	ProfilingContext ctx = new ProfilingContext(true, true, "");
	assertTrue(ctx._enabled);
	assertEquals(0, ctx._totalCalls);
	assertEquals(0, ctx._totalTime);
	assertTrue(ctx._threadSafe);
    }

    public void testGetSub() {
	ProfilingContext ctx = new ProfilingContext(true, true, "");
	ProfilingContext noneCtx = new ProfilingContext(false, true, "");
	ctx._sub.put("none", noneCtx);
	assertSame(noneCtx, ctx.getSub("none"));
    }

    public void testIsEnabled_AllOn() {
	// simplest case : all on
	ProfilingContext ctx = new ProfilingContext(false, true, "");
	ctx._enabled = true;
	ProfilingContext._initiallyEnabled = true;
	assertTrue(ctx.isEnabled());
    }

    public void testIsEnabled_AllOff() {
	// simplest case : all on
	ProfilingContext ctx = new ProfilingContext(false, true, "");
	ctx._enabled = false;
	assertTrue(!ctx.isEnabled());
    }

    public void testIsEnabled_GlobalOffLocalOn() {
	// simplest case : all on
	ProfilingContext ctx = new ProfilingContext(false, true, "");
	ctx._enabled = true;
	assertTrue(!ctx.isEnabled());
    }

    public void testIsEnabled_GlobalOnLocalOff() {
	// simplest case : all on
	ProfilingContext ctx = new ProfilingContext(false, true, "");
	ctx._enabled = false;
	ProfilingContext._initiallyEnabled = true;
	assertTrue(!ctx.isEnabled());
    }

    /*
         * Test for void setEnabled(boolean, boolean)
         */
    public void testSetEnabledbooleanboolean_dontPropagate() {
	ProfilingContext parent = new ProfilingContext(false, true, "");
	ProfilingContext child = new ProfilingContext(false, true, "");
	parent._sub.put("child", child);
	parent.setEnabled(true, false);
	assertTrue(parent._enabled);
	assertTrue(!child._enabled);
    }

    public void testSetEnabledbooleanboolean_propagate() {
	ProfilingContext parent = new ProfilingContext(false, true, "");
	ProfilingContext child = new ProfilingContext(false, true, "");
	parent._sub.put("child", child);
	parent.setEnabled(true, true);
	assertTrue(parent._enabled);
	assertTrue(child._enabled);
    }

    public void testStartStop() throws InterruptedException {
	ProfilingContext ctx = new ProfilingContext(true, false, "");
	ProfilingContext.setProfilingEnabled(true);
	assertNull(ctx._starts.get(Thread.currentThread()));
	ctx.start();
	assertNotNull(ctx._starts.get(Thread.currentThread()));
	Thread.sleep(1000);
	ctx.stop();
	assertNull(ctx._starts.get(Thread.currentThread()));
	assertEquals(1, ctx.getTotalCalls());
	assertTrue(ctx.getAverageTime() > 999);
    }

    public void testStartStop_Profiling() {

	// some profiling the profiler

	ProfilingContext.setProfilingEnabled(true);
	int runs = 1000;
	int ctxCount = 1000;
	ProfilingContext[] ctxs = new ProfilingContext[ctxCount];
	for (int i = 0; i < ctxs.length; i++) {
	    ctxs[i] = ProfilingContext.getContext(new String[] { "" + i });
	    // ctxs[i].setEnabled(true) ;
	}

	ProfilingContext myContext = ProfilingContext.getContext(
		ProfilingContext.class, "profiling the profiler");
	myContext.setEnabled(true);
	for (int runIndex = 0; runIndex < runs; runIndex++) {
	    myContext.start();
	    for (int i = 0; i < ctxs.length; i++) {
		ctxs[i].start();
	    }

	    for (int i = 0; i < ctxs.length; i++) {
		ctxs[i].stop();
	    }
	    myContext.stop(ctxs.length);
	}

	System.out.println("Calls             : " + myContext.getTotalCalls());
	System.out.println("Time              : " + myContext.getTotalTime());
	System.out.println("avg start/stop    : " + myContext.getAverageTime());
    }

}
