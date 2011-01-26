package net.micwin.yajl.core.instances;

/**
 * A IUniqueIdProvider is a source of unique ids. The implementer must quarantee
 * that two calls to {@link #createUniqueId()} will return two different ids.
 * Difference is determined by the result of method <code>toString()</code>. The
 * implementor forms the context in which a returned UniqueId is unique.
 * <p />
 * Please also respect the contract in {@link UniqueId#toString()}.
 * 
 * @author MicWin
 * 
 */
public interface IUniqueIdProvider {

	/**
	 * Returns a newly created unique id.
	 * 
	 * @return
	 */
	public UniqueId createUniqueId();
}
