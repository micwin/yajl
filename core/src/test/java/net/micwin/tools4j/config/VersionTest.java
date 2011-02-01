/**
 * 
 */
package net.micwin.tools4j.config;

import junit.framework.TestCase;

/**
 * @author FCTM1298
 * 
 */
public class VersionTest extends TestCase {

    /**
         * @param arg0
         */
    public VersionTest(String arg0) {
	super(arg0);
    }

    /**
         * Test method for
         * {@link net.micwin.tools4j.config.Version#Version(int, int, int)}.
         */
    public final void testVersion() {
	Version v = new Version(1, 2, 3);
	assertEquals(1, v._version);
	assertEquals(2, v._increment);
	assertEquals(3, v._build);

    }

    /**
         * Test method for
         * {@link net.micwin.tools4j.config.Version#compareTo(java.lang.Object)} -
         * equal instances.
         */
    public final void testCompareTo_equal() {
	assertEquals(0, new Version(3, 4, 5).compareTo(new Version(3, 4, 5)));
    }

    /**
         * Test method for
         * {@link net.micwin.tools4j.config.Version#compareTo(java.lang.Object)} -
         * first is smaller.
         */
    public final void testComparefirstIsSmaller() {
	assertEquals("version is smaller", -1, new Version(2, 4, 5)
		.compareTo(new Version(3, 4, 5)));
	assertEquals("increment is smaller", -1, new Version(3, 3, 5)
		.compareTo(new Version(3, 4, 5)));
	assertEquals("build is smaller", -1, new Version(3, 4, 4)
		.compareTo(new Version(3, 4, 5)));
    }

    /**
         * Test method for
         * {@link net.micwin.tools4j.config.Version#compareTo(java.lang.Object)} -
         * first is smaller.
         */
    public final void testCompare_SecondIsSmaller() {
	assertEquals("version is bigger", 1, new Version(4, 4, 5)
		.compareTo(new Version(3, 4, 5)));
	assertEquals("increment is bigger", 1, new Version(3, 5, 5)
		.compareTo(new Version(3, 4, 5)));
	assertEquals("build is bigger", 1, new Version(3, 4, 6)
		.compareTo(new Version(3, 4, 5)));
    }

    /**
         * Test method for {@link net.micwin.tools4j.config.Version#toString()}.
         */
    public final void testToString() {
	assertEquals("1.4.3", new Version(1, 4, 3).toString());
    }

    /**
         * Test method for
         * {@link net.micwin.tools4j.config.Version#toArray(int[])} witn null as
         * argument.
         */
    public final void testToArray_null() {
	int v = 2;
	int i = 3;
	int b = 4;
	int[] array = new Version(v, i, b).toArray(null);
	assertEquals("correct length", 3, array.length);
	checkArray(array, v, i, b);
    }

    /**
         * Test method for
         * {@link net.micwin.tools4j.config.Version#toArray(int[])} with too
         * short argument.
         */
    public final void testToArray_tooShort() {
	int v = 8;
	int i = 7;
	int b = 6;
	int[] array = new Version(v, i, b).toArray(new int[2]);
	assertEquals("correct length", 3, array.length);
	checkArray(array, v, i, b);
    }

    /**
         * Test method for
         * {@link net.micwin.tools4j.config.Version#toArray(int[])} with too
         * long argument.
         */
    public final void testToArray_tooLong() {
	int v = 8;
	int i = 7;
	int b = 6;
	int[] array = new Version(v, i, b).toArray(new int[25]);
	assertEquals("same length", 25, array.length);
	checkArray(array, v, i, b);
    }

    /**
         * @param array
         * @param v
         * @param i
         * @param b
         */
    private void checkArray(int[] array, int v, int i, int b) {
	assertNotNull(array);
	assertEquals(v, array[0]);
	assertEquals(i, array[1]);
	assertEquals(b, array[2]);
    }

    /**
         * Test method for
         * {@link net.micwin.tools4j.config.Version#isBelow(net.micwin.tools4j.config.Version)}.
         */
    public final void testIsBelow() {
	assertTrue(new Version(4, 4, 4).isBelow(new Version(5, 4, 4)));
	assertTrue(new Version(4, 4, 4).isBelow(new Version(4, 5, 4)));
	assertTrue(new Version(4, 4, 4).isBelow(new Version(4, 4, 5)));

	// negative tests
	assertFalse(new Version(4, 4, 4).isBelow(new Version(3, 4, 4)));
	assertFalse(new Version(4, 4, 4).isBelow(new Version(4, 3, 4)));
	assertFalse(new Version(4, 4, 4).isBelow(new Version(4, 4, 3)));

	// problem candidates
	assertTrue(new Version(4, 4, 4).isBelow(new Version(4, 5, 0)));
	assertTrue(new Version(4, 4, 4).isBelow(new Version(5, 0, 0)));

    }

    /**
         * Test method for
         * {@link net.micwin.tools4j.config.Version#isAbove(net.micwin.tools4j.config.Version)}.
         */
    public final void testIsAbove() {
	assertTrue(new Version(6, 6, 6).isAbove(new Version(5, 6, 6)));
	assertTrue(new Version(6, 6, 6).isAbove(new Version(6, 5, 6)));
	assertTrue(new Version(6, 6, 6).isAbove(new Version(6, 6, 5)));

	// negative tests
	assertFalse(new Version(6, 6, 6).isAbove(new Version(7, 6, 6)));
	assertFalse(new Version(6, 6, 6).isAbove(new Version(6, 7, 6)));
	assertFalse(new Version(6, 6, 6).isAbove(new Version(6, 6, 7)));

	// problem candidates
	assertTrue(new Version(6, 6, 6).isAbove(new Version(6, 6, 0)));
	assertTrue(new Version(6, 6, 6).isAbove(new Version(6, 0, 0)));
	assertTrue(new Version(6, 6, 6).isAbove(new Version(0, 0, 0)));

    }

    public void testValueOf() {
	Version v = Version.valueOf("1. 2. 98 ");
	assertEquals(1, v.getVersion());
	assertEquals(2, v.getIncrement());
	assertEquals(98, v.getBuild());

	v = Version.valueOf("157");
	assertEquals(157, v.getVersion());
	assertEquals(0, v.getIncrement());
	assertEquals(0, v.getBuild());
    }

    public void testSnapshot() {
	Version snapShot = new Version(1, 2, 3, true);
	assertTrue(snapShot.isSnapShot());
	Version v2 = new Version(1, 2, 3, true);
	assertTrue(snapShot.equals(v2));
	assertEquals(0, snapShot.compareTo(v2));

	v2 = new Version(1, 2, 3, false);
	assertFalse(snapShot.equals(v2));
	assertEquals(-1, snapShot.compareTo(v2));
	assertEquals(1, v2.compareTo(snapShot));

    }

}
