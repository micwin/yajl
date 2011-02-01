package net.micwin.yajl.core.instances.impl;

import java.util.Collection;
import net.micwin.yajl.core.instances.IFactory;

/**
 * provides a Pool that manages Collection instances. Gets its instances from a
 * collection factory and calls {@link Collection#clear()} upon
 * {@link #release(Object)}.
 * 
 * @author MicWin
 * 
 * @param <E>
 */
public class CollectionPool<T extends Collection> extends SimplePool<T> {

	public CollectionPool(IFactory<T> factory) {
		super(factory);
	}

	@Override
	public void release(T resource) {
		resource.clear();
		super.release(resource);
	}
}
