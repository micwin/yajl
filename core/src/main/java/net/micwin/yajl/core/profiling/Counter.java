package net.micwin.yajl.core.profiling;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple class to count something.
 * 
 * @author MicWin
 * 
 */
public class Counter {

	// -----------------------------------------
	// static field
	// -----------------------------------------

	private static final Log L = LogFactory.getLog(Counter.class);

	private static Map<String, Counter> map = new HashMap<String, Counter>();

	/**
	 * Returns a counter by ID. Each ID is only used once. Call as seldom as
	 * possible, store instances got by this way in static fields.
	 * 
	 * @param id
	 */
	public static synchronized Counter getCounter(String id) {

		Counter c = map.get(id);
		if (c == null) {
			c = new Counter(id);
			map.put(id, c);
			if (L.isInfoEnabled()) {
				L.info("created counter '" + id + "'");
			}
		}

		return c;
	}

	// -----------------------------------------
	// instance field
	// -----------------------------------------
	private final String id;
	int counts = 0;

	private Counter(String id) {
		this.id = id;
	}

	/**
	 * Returns the unique ID of this counter.
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Raise counter by 1.
	 * 
	 * @return
	 */
	public int raise() {
		return ++counts;
	}

	@Override
	public String toString() {
		return id + "=" + counts;
	}

	public int getCounts() {
		return counts;
	}
}
