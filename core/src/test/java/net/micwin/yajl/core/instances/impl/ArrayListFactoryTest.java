package net.micwin.yajl.core.instances.impl;

import junit.framework.TestCase;

public class ArrayListFactoryTest extends TestCase {

	public void testCreate() {
		ArrayListFactory<String> f = new ArrayListFactory<String>();
		assertNotNull(f.create());
		assertNotSame(f.create(), f.create());
	}
}
