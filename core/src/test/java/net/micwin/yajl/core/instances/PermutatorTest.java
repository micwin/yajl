/**
 * 
 */
package net.micwin.yajl.core.instances;

import net.micwin.yajl.core.instances.Permutator;
import junit.framework.TestCase;

/**
 * @author michael.winkler@micwin.net
 * 
 */
public class PermutatorTest extends TestCase {

    /**
         * @param arg0
         */
    public PermutatorTest(String arg0) {
	super(arg0);
    }

    /**
         * Test method for
         * {@link net.micwin.yajl.core.instances.Permutator#Permutator(java.lang.Object[], int)}.
         */
    public final void testPermutator() {

	try {
	    new Permutator(null, 25);
	    fail("null terminals array accepted");
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	try {
	    new Permutator(new String[] {}, 25);
	    fail("empty terminals array accepted");
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	try {
	    new Permutator(new String[] { "a" }, 25);
	    fail("too few terminals accepted");
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	try {
	    new Permutator(new String[] { "a", "b", "c" }, 0);
	    fail("too small length accepted");
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	try {
	    new Permutator(new String[] { "a", "b", "c" }, -1);
	    fail("negative length accepted");
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	Permutator p = new Permutator(new String[] { "0", "1" }, 3);
	assertFalse(p.overflow);
	assertNotNull(p.terminals);
	assertEquals("0", p.terminals[0]);
	assertEquals("1", p.terminals[1]);
	assertNotNull(p.activeState);
	assertEquals(3, p.activeState.length);
	assertEquals(0, p.activeState[0]);
	assertEquals(0, p.activeState[1]);
	assertEquals(0, p.activeState[2]);
    }

    /**
         * Test method for {@link net.micwin.yajl.core.instances.Permutator#hasNext()}.
         */
    public final void testRaise() {
	Permutator p = new Permutator(new String[] { "0", "1" }, 1);
	assertTrue(p.raise());
	assertFalse(p.raise());
    }

    /**
         * Test method for
         * {@link net.micwin.yajl.core.instances.Permutator#next(java.lang.Object[])}.
         */
    public final void testRaise_Easy() {
	Permutator p = new Permutator(new String[] { "0", "1" }, 1);
	Object[] result = p.terminals(null);
	assertNotNull(result);
	assertEquals(1, result.length);
	assertEquals("0", result[0]);
	p.raise();
	p.terminals(result);
	assertEquals("1", result[0]);
    }

    /**
         * Test method for
         * {@link net.micwin.yajl.core.instances.Permutator#next(java.lang.Object[])}.
         */
    public final void testRaise_Complex() {
	Permutator p = new Permutator(new String[] { "0", "1", "2" }, 2);
	Object[] result = p.terminals(null);
	assertNotNull(result);
	assertEquals(2, result.length);
	assertEquals("0", result[0]);
	assertEquals("0", result[1]);
	p.raise();
	p.terminals(result);
	assertEquals("1", result[0]);
	assertEquals("0", result[1]);
	p.raise();
	p.terminals(result);
	assertEquals("2", result[0]);
	assertEquals("0", result[1]);
	p.raise();
	p.terminals(result);
	assertEquals("0", result[0]);
	assertEquals("1", result[1]);
	p.raise();
	p.terminals(result);
	assertEquals("1", result[0]);
	assertEquals("1", result[1]);
	p.raise();
	p.terminals(result);
	assertEquals("2", result[0]);
	assertEquals("1", result[1]);

	p.raise();
	p.terminals(result);
	assertEquals("0", result[0]);
	assertEquals("2", result[1]);

	p.raise();
	p.terminals(result);
	assertEquals("1", result[0]);
	assertEquals("2", result[1]);
	p.raise();
	p.terminals(result);
	assertEquals("2", result[0]);
	assertEquals("2", result[1]);

    }

    /**
         * Test method for {@link net.micwin.yajl.core.instances.Permutator#restart()}.
         */
    public final void testRestart() {

	Permutator p = new Permutator(new String[] { "0", "1", "2" }, 2);
	while (p.raise())
	    ;
	p.restart();
	Object[] result = p.terminals(null);
	assertEquals("0", result[0]);
	assertEquals("0", result[1]);
	assertTrue(p.raise());
    }
}
