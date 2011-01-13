package net.micwin.yajl.core.profiling;

import java.util.ArrayList;

import junit.framework.TestCase;

public class RegionProfilerTest extends TestCase {

	static int RUNS = 500000;
	static int RECURSIONS = 5;

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

	public void testEnterExitManyCalls() {
		proProfile(RUNS, RECURSIONS);
	}

	@SuppressWarnings("unchecked")
	private void proProfile(int runs, int recursions) {
		RegionProfiler profiler = RegionProfiler.getProfiler("dfn uz7hcvk");

		System.out.println("testing performance with " + runs + " loops and "
				+ recursions + " recursion levels. This may take a while ...");

		ArrayList recursion = new ArrayList(recursions);

		// outer loop - test some zillion

		long allRunsStartTime = System.currentTimeMillis();
		for (int run = 0; run < runs; run++) {
			recursion.clear();
			// inner loop to test some recursion

			// enter some
			for (int level = 0; level < recursions; level++) {
				recursion.add(profiler.enter());
			}

			// exit
			for (int level = recursion.size(); level > 0; level--) {
				Object item = recursion.get(level - 1);
				profiler.exit(item);
			}
		}

		long totalTime = System.currentTimeMillis() - allRunsStartTime;
		int totalCalls = runs * recursions * 2;
		System.out.println("time (" + runs + " runs, " + recursions
				+ " recursions, " + totalCalls + " total enter/exits): "
				+ totalTime + " ms");

		System.out.println("time per run :" + (1.0 * totalTime / runs)
				+ " ms avg");
		double pairAvg = 1.0 * totalTime / totalCalls;
		System.out.println("time per pair :" + pairAvg + " ms avg");

		assertTrue("becoming too slow", pairAvg / 1.1978E-4 < 2);

	}
}
