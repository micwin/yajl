package net.micwin.yajl.core.instances.impl;

import java.util.LinkedList;
import java.util.List;

import net.micwin.yajl.core.instances.Factory;
import net.micwin.yajl.core.instances.Pool;

/**
 * This pool implements a pool that gets its instances from a factory. This does
 * NEITHER call any initialization of any kind NOR any cleanup, so this one here
 * only works for very simple objects.
 * 
 * @author MicWin
 * 
 * @param <T>
 */
public class SimplePool<T> implements Pool<T> {

	private final Factory<T> factory;

	List<T> free = new LinkedList<T>();

	public SimplePool(Factory<T> factory) {
		this.factory = factory;
	}

	public T aquire() {

		T rv = null;
		if (free.size() < 1) {
			rv = factory.create();
		} else {
			rv = free.get(0);
		}
		return rv;
	}

	public void release(T resource) {
		free.add(resource);
	}
}
