package net.micwin.yajl.core.instances;

/**
 * A pool is a container that manages elements. This is handy when instances are
 * expensive to create and/or release. Unlike ConnectionPoolDataSource, this
 * instance follows a much more generic way.
 * <p />
 * Since this is a high potential memory leak which can shutdown your whole vm,
 * make sure to release instances in a finally-block.
 * 
 * @author MicWin
 * 
 */
public interface Pool<T> {

	/**
	 * Aquire a resource. Make sure to release it again by calling
	 * {@link #release()} in a <code>finally</code> block.
	 * 
	 * @return
	 */
	T aquire();

	/**
	 * Release a resource previously aquired by using {@link #aquire()}.
	 * 
	 * @param resourcea
	 *            resource previously aquired by using {@link #aquire()} of this
	 *            instance.
	 */
	void release(T resource);
}
