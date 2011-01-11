package net.micwin.yajl.core.profiling;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to profile bypasses of source code regions.
 * 
 * @author MicWin
 * 
 */
public class RegionProfiler {

	// ----------------------------------------------------------------------
	// static field
	// ----------------------------------------------------------------------
	static Map<String, RegionProfiler> regions = new HashMap<String, RegionProfiler>();

	/**
	 * gets a region profiler for the region specified by <code>regionId</code>.
	 * 
	 * @param regionId
	 * @return
	 */
	public synchronized static RegionProfiler getProfiler(String regionId) {
		RegionProfiler profiler = regions.get(regionId);

		if (profiler == null) {
			profiler = new RegionProfiler(regionId);
			regions.put(regionId, profiler);
		}
		return profiler;
	}

	// ----------------------------------------------------------------------
	// instance field
	// ----------------------------------------------------------------------

	String regionId;
	private int enteredCount = 0;
	private int exitedCount = 0;

	public RegionProfiler(String regionId) {
		this.regionId = regionId;

	}

	/**
	 * The number of calls to {@link #enter()}, hence the number of times the
	 * region to profile has been entered.
	 * 
	 * @return
	 */
	public int getEnteredCount() {
		return enteredCount;
	}

	/**
	 * Call this when entering the region to profile.
	 * 
	 * @return the profiler handle you have to give back as-is when calling
	 *         {@link #exit(Object)}. Do not do anything with this; class and
	 *         type may change, it even will be <code>null</code> corresponding
	 *         how the profiler is configured.
	 */

	public Object enter() {
		enteredCount += 1;
		return System.currentTimeMillis();
	}

	/**
	 * Call this when exiting the region to profile.
	 * 
	 * @param profilerHandle
	 *            The handle you received when calling {@link #enter()}.
	 * @return The time used to pass the region.
	 */
	public long exit(Object profilerHandle) {
		exitedCount += 1;
		return System.currentTimeMillis() - (Long) profilerHandle;
	}

	/**
	 * The number of calls to {@link #exit()}, hence the number of times the
	 * region to profile has been exited.
	 * 
	 * @return
	 */
	public int getExitedCount() {
		return exitedCount;
	}

}
