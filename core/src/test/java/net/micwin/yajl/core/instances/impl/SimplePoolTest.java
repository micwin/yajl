package net.micwin.yajl.core.instances.impl;

import junit.framework.TestCase;

public class SimplePoolTest extends TestCase {

	public void testConstructor() throws SecurityException,
			NoSuchMethodException {
		DefaultFactory<String> factory = new DefaultFactory<String>(
				String.class);
		SimplePool<String> pool = new SimplePool<String>(factory);
		assertEquals(0, pool.free.size());
	}

	public void testAquireRelease() throws SecurityException,
			NoSuchMethodException {

		// init
		DefaultFactory<String> factory = new DefaultFactory<String>(
				String.class);
		SimplePool<String> pool = new SimplePool<String>(factory);

		// straightforward
		String aquired = pool.aquire();
		assertNotNull(aquired);
		assertEquals(0, pool.free.size());
		pool.release(aquired);
		assertEquals(1, pool.free.size());
		assertSame(aquired, pool.free.get(0));
	}
}
