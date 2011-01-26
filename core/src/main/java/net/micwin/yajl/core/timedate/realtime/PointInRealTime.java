package net.micwin.yajl.core.timedate.realtime;

import java.util.Locale;

import net.micwin.yajl.core.timedate.IPointInTime;

public class PointInRealTime implements IPointInTime {

	private final long currentTimeMillis;
	private final Locale locale;

	/**
	 * Create a PointInTime corresponding to the actual realTime at the system
	 * default locale.
	 */
	public PointInRealTime() {

		this(System.currentTimeMillis(), Locale.getDefault());
	}

	public PointInRealTime(long currentTimeMillis) {
		this(currentTimeMillis, Locale.getDefault());
	}

	public PointInRealTime(long currentTimeMillis, Locale locale) {
		this.currentTimeMillis = currentTimeMillis;
		this.locale = locale;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
