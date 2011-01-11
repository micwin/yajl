package net.micwin.yajl.core.instances.impl;

import net.micwin.yajl.core.instances.impl.DefaultFactory;
import junit.framework.TestCase;

public class DefaultFactoryTest extends TestCase {

	static int counter = 0;

	static class DummyClass {

		int instanceId = ++counter;

		public DummyClass() {
		}

	}

	public void testCreate() throws SecurityException, NoSuchMethodException {

		DefaultFactory<DummyClass> factory = new DefaultFactory<DummyClass>(
				DummyClass.class);

		DummyClass elem = factory.create();
		assertNotNull(elem);
		assertEquals(1, elem.instanceId);
		elem = factory.create();
		assertNotNull(elem);
		assertEquals(2, elem.instanceId);
		elem = factory.create();
		assertNotNull(elem);
		assertEquals(3, elem.instanceId);

	}
}
