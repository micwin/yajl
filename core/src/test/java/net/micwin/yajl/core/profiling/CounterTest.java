package net.micwin.yajl.core.profiling;

import junit.framework.TestCase;

public class CounterTest extends TestCase {

	public void testGetCounter() {
		Counter c = Counter.getCounter(CounterTest.class.getName());
		assertNotNull(c);
		assertEquals(CounterTest.class.getName(), c.getId());
		assertEquals(0, c.getCounts());
	}

	public void testGetCounts() {

		Counter c = Counter.getCounter("dfvnvnvnvndkvjnvksn");
		c.counts = 5;
		assertEquals(5, c.getCounts());
	}

	public void testRaise() {

		Counter c = Counter.getCounter("dfvnvnvnvndkvjnvkssfjlhn");
		c.counts = 5;
		assertEquals(6, c.raise());
		assertEquals(6, c.counts);
	}

	public void testToString() {
		Counter c = Counter.getCounter("dfvnvnvnv");
		c.counts = 42;
		assertEquals("dfvnvnvnv=42", c.toString());

	}

}
