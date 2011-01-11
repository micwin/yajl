package net.micwin.yajl.core.instances.impl;

import java.util.LinkedList;

import junit.framework.TestCase;

public class CollectionPoolTest extends TestCase {

	public void testAquireRelease() throws SecurityException,
			NoSuchMethodException {
		@SuppressWarnings("unchecked")
		Class<LinkedList<String>> clazz = (Class<LinkedList<String>>) new LinkedList<String>()
				.getClass();
		DefaultFactory<LinkedList<String>> factory = new DefaultFactory<LinkedList<String>>(
				clazz);

		CollectionPool<LinkedList<String>> pool = new CollectionPool<LinkedList<String>>(
				factory);
		LinkedList<String> first = pool.aquire();
		assertNotNull(first);
		assertEquals(0, first.size());
		first.add("blahblah");
		pool.release(first);
		assertEquals(0, first.size());
		assertEquals(1, pool.free.size());
	}
}
