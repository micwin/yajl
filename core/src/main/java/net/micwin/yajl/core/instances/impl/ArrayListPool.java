package net.micwin.yajl.core.instances.impl;

import java.util.ArrayList;

/**
 * A pool managing ArrayLists.
 * 
 * @author MicWin
 * 
 * @param <E>
 *            Element type of arrayLists.
 */
public class ArrayListPool<E> extends CollectionPool<ArrayList<E>> {

	/**
	 * a singleton pool for String based array lists.
	 */
	public static ArrayListPool<String> FOR_STRINGS = new ArrayListPool<String>();

	
	/**
	 * Initialize a new pool.
	 */
	public ArrayListPool() {
		super(new ArrayListFactory<E>());
	}

}
