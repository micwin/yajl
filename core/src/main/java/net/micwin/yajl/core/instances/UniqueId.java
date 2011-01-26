package net.micwin.yajl.core.instances;

/**
 * A uniqueId is a value that is unique in respect of the context implemented by
 * its {@link IUniqueIdProvider}. It mostly is used to identify an entity.
 * 
 * @author MicWin
 * 
 */
public abstract class UniqueId {

	private String code;
	IUniqueIdProvider uniqueIdProvider;

	/**
	 * Creates a new
	 * 
	 * @param uniqueIdProvider
	 * @param code
	 */
	protected UniqueId(IUniqueIdProvider uniqueIdProvider, String code) {
		this.uniqueIdProvider = uniqueIdProvider;
		this.code = code;
	}

	/**
	 * The uniqueId as a string. This string has to follow the following rules:
	 * <ul>
	 * <li>There may not be two uniqueIds to have the same provider AND the same
	 * string representation.</li>
	 * <li>If two subsequent uniqueIds only change in a small amount of data, then
	 * their string representation has to be arranged from left to right, most
	 * changing parts left, least changing parts right. This is to optimize
	 * String comparison.</li>
	 * </ul>
	 */
	@Override
	public String toString() {
		return code;
	}

	/**
	 * Returns the {@link IUniqueIdProvider} that created this UniqueId.
	 * 
	 * @return
	 */
	public IUniqueIdProvider getUniqueIdProvider() {
		return uniqueIdProvider;
	}
}
