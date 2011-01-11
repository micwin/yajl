package net.micwin.yajl.core.profiling;

import junit.framework.TestCase;

public class RegionProfilerTest extends TestCase {

	public void testGetProfiler() {
		RegionProfiler profiler = RegionProfiler.getProfiler("dfn uz7hcvk");
		assertNotNull(profiler);
		assertSame(profiler, RegionProfiler.getProfiler("dfn uz7hcvk"));
	}

	public void testEnterExitCount() throws Exception {
		RegionProfiler profiler = RegionProfiler.getProfiler("dfn uz7hcvk");

		int enteredBefore = profiler.getEnteredCount();
		int exitedBefore = profiler.getExitedCount();

		Object handle = profiler.enter();

		assertEquals(enteredBefore + 1, profiler.getEnteredCount());
		assertEquals(exitedBefore, profiler.getExitedCount());
		Thread.sleep(10);
		long time = profiler.exit(handle);
		assertEquals(enteredBefore + 1, profiler.getEnteredCount());
		assertEquals(exitedBefore + 1, profiler.getExitedCount());
		assertTrue(time > 0);
	}
}
