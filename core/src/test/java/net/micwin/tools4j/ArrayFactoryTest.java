package net.micwin.tools4j;

import junit.framework.TestCase;

public class ArrayFactoryTest extends TestCase {

    public ArrayFactoryTest(String arg0) {
	super(arg0);
    }

    public final void testReverse() {
	String[] array = new String[] { "This", "is", "a", "ridiculous",
		"array" };
	ArrayFactory.reverse(array);

	assertEquals("This", array[4]);
	assertEquals("is", array[3]);
	assertEquals("a", array[2]);
	assertEquals("ridiculous", array[1]);
	assertEquals("array", array[0]);
    }

    public void testEquals_same() {
	String[] array = new String[] { "blah", "blubb", "bloo" };
	assertTrue(ArrayFactory.equals(array, array));
    }

    public void testEquals() {
	String[] array1 = new String[] { "blah", "blubb", "bloo" };
	String[] array2 = new String[] { "blah", "blubb", "bloo" };
	assertTrue(ArrayFactory.equals(array1, array2));
    }

    public void testEquals_different() {
	String[] array1 = new String[] { "blah", "bloo", "blubb" };
	String[] array2 = new String[] { "blah", "blubb", "bloo" };
	assertFalse(ArrayFactory.equals(array1, array2));
    }

    public void testEquals_different_length() {
	String[] array1 = new String[] { "blah", "blubb" };
	String[] array2 = new String[] { "blah", "blubb", "bloo" };
	assertFalse(ArrayFactory.equals(array1, array2));
    }

    public void testEquals_Null() {
	String[] array = new String[] { "blah", "blubb", "bloo" };
	assertFalse(ArrayFactory.equals(array, null));
	assertFalse(ArrayFactory.equals(null, array));
	assertTrue(ArrayFactory.equals(null, null));

    }

}
