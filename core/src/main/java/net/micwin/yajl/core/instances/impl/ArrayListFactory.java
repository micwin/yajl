package net.micwin.yajl.core.instances.impl;

import java.util.ArrayList;

import net.micwin.yajl.core.instances.Factory;

/**
 * A factory that creates ArrayLists.
 * 
 * @author MicWin
 * 
 * @param <E>
 */
public class ArrayListFactory<E> implements Factory<java.util.ArrayList<E>> {

	public ArrayList<E> create() {
		return new ArrayList<E>();
	}

}
