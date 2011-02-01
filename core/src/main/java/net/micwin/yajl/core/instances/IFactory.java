package net.micwin.yajl.core.instances;

import net.micwin.yajl.core.exceptions.TechException;

/**
 * A factory is a structure that creates instances of type T. Name it a
 * outsourced constructor. Thats all.
 * 
 * @author MicWin
 * 
 * @param <T>
 */
public interface IFactory<T> {

	/**
	 * Create a new instance.
	 * 
	 * @return
	 */
	public T create() throws TechException;
}
