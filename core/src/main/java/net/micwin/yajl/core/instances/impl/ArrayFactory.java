package net.micwin.yajl.core.instances.impl;

import java.lang.reflect.Array;

/**
 * A convenient class to do some job the jdk's claases miss. TODO UNIT TESTS
 * 
 * @author michael.winkler@micwin.net
 * 
 */
public class ArrayFactory {

    // ------------------------------------------------------
    // static field
    // ------------------------------------------------------

    /**
         * Reverses the elements in the array.
         * 
         * @param array
         *                an array to reverse.
         */
    public static void reverse(Object[] array) {
	for (int i = 0; i < array.length / 2; i++) {
	    Object o = array[i];
	    array[i] = array[array.length - i - 1];
	    array[array.length - i - 1] = o;
	}
    }

    /**
         * Compares two arrays. Only returns true if all elements are equal to
         * each other.
         * 
         * @param array1
         * @param array2
         * @return
         */
    public static boolean equals(Object[] array1, Object[] array2) {
	if (array1 == array2) {
	    // same instance or both null
	    return true;
	} else if (array1 == null || array2 == null) {
	    // only one is null
	    return false;
	} else if (array1.length != array2.length) {
	    // different size
	    return false;
	} else {
	    // same size, run through elements
	    for (int i = 0; i < array1.length; i++) {
		Object o1 = array1[i];
		Object o2 = array2[i];
		if (o1 == o2) {
		    // same element or both null; continue with next loop
		    continue;
		} else if (o1 == null || o2 == null || !o1.equals(o2)) {
		    // only one is null, or elements are different
		    return false;
		}
	    }
	}

	return true;
    }

    //
    /**
         * Creates a new array of the same size and puts in all references from
         * this array to the other. This is a shallow copy.
         * 
         * @param data
         *                The array to be copied.
         * @return
         */
    public static Object[] copy(Object[] data) {
	Object[] dest = (Object[]) Array.newInstance(data.getClass()
		.getComponentType(), data.length);
	System.arraycopy(data, 0, dest, 0, data.length);
	return dest;
    }

    /**
         * Appends a new element to thze passed array., i.e creates a new array
         * where the elements from <code>array</code> lead, and
         * <code>newElement</code> is the last one.
         * 
         * @param array
         * @param newElement
         * @return
         */
    public static Object[] append(Object[] array, Object newElement) {
	Object[] dest = (Object[]) Array.newInstance(array.getClass()
		.getComponentType(), array.length + 1);
	System.arraycopy(array, 0, dest, 0, array.length);
	dest[dest.length - 1] = newElement;
	return dest;
    }
}
