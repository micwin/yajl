package net.micwin.yajl.core.instances.impl;

import java.util.ArrayList;

import junit.framework.TestCase;

public class ArrayListPoolTest extends TestCase {

	public void testAquireRelease() {
		ArrayListPool<String> pool = new ArrayListPool<String>();

		ArrayList<String> first = pool.aquire();
		ArrayList<String> second = pool.aquire();

		assertNotNull(first);
		assertNotNull(second);

		assertNotSame(first, second);

		assertEquals(0, first.size());
		assertEquals(0, second.size());

		pool.release(second);
		assertSame(second, pool.aquire());
		first.add("Holla!");
		pool.release(first);
		assertEquals(0, first.size());
		pool.release(second);
		pool.release(first);
		assertSame(first, pool.aquire());
	}
}
