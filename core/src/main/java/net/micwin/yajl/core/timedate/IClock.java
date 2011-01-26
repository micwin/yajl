package net.micwin.yajl.core.timedate;

/**
 * A clock is a unit to measure time inside a continuum. This is intended to be
 * a singleton.
 * 
 * @author MicWin
 * 
 */
public interface IClock {

	/**
	 * Returns the actual point in time.
	 * 
	 * @return
	 */
	public IPointInTime getPointInTime();

}
