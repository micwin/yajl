package net.micwin.yajl.core.instances;

/**
 * A factory is a structure that creates instances of type T. Name it a
 * outsourced constructor. Thats all.
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
