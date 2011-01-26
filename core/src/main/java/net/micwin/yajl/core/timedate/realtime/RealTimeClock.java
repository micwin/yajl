package net.micwin.yajl.core.timedate.realtime;

import net.micwin.yajl.core.timedate.IClock;
import net.micwin.yajl.core.timedate.IPointInTime;

public class RealTimeClock implements IClock {

	public IPointInTime getPointInTime() {
		return new PointInRealTime();
	}

}
