package net.micwin.yajl.core.instances;

/**
 * A factory is a structure that creates instances of other classes.
 * 
 * @author MicWin
 * 
 * @param <T>
 */
public interface Factory<T> {

	/**
	 * Create a new instance.
	 * 
	 * @return
	 */
	public T create();
}
