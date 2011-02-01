/**
 * 
 */
package net.micwin.yajl.core.instances.impl;

import java.util.Iterator;

/**
 * Wraps an iterator to return strings. This is done by calling toString() on
 * every element returned.
 * 
 * @author micwin@micwin.net
 * 
 */
public class StringIterator implements Iterator<String> {

    Iterator iter = null;

    public StringIterator(Iterator iter) {
	this.iter = iter;
    }

    /*
         * (non-Javadoc)
         * 
         * @see java.util.Iterator#hasNext()
         */
    public boolean hasNext() {
	return this.iter.hasNext();
    }

    /*
         * (non-Javadoc)
         * 
         * @see java.util.Iterator#next()
         */
    public String next() {
	return this.iter.next().toString();
    }

    /*
         * (non-Javadoc)
         * 
         * @see java.util.Iterator#remove()
         */
    public void remove() {
	this.iter.remove();

    }

}
