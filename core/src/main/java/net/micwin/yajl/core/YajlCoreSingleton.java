package net.micwin.yajl.core;

import net.micwin.yajl.core.config.Configuration;

/**
 * This is a singleton for convenient access to some elements of yajl-core. Note
 * that you have to activate this explicisely by setting the system property
 * <code>YajlCoreSingleton.rootConfigClass</code> to a class implementing
 * {@link Configuration} and providing a default constructor.
 * 
 * If not done, calls to this class result in <code>IllegalStateException</code>
 * . Note that, for classes outside this package, there is no chance in finding
 * out wether or not this behavior is enabled; so you have to talk to each other
 * and find a convention.
 * 
 * @author MicWin
 * 
 */
public class YajlCoreSingleton {

	static Configuration rootConfig;

	static {

		String rootConfigClassName = System.getProperty(YajlCoreSingleton.class
				.getSimpleName() + ".rootConfigClass");
		if (rootConfigClassName != null) {
			rootConfigClassName = rootConfigClassName.trim();
			try {
				Class rootConfigClass = Class.forName(rootConfigClassName);
				rootConfig = (Configuration) rootConfigClass.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

	private static void checkEnabled() {

		if (rootConfig == null)
			throw new IllegalStateException("feature '"
					+ YajlCoreSingleton.class.getSimpleName()
					+ "' not enabled but used.");
	}

	/**
	 * Returns a singleton root configuration.
	 * 
	 * @return
	 */
	public static synchronized Configuration getRootConfig() {
		checkEnabled();
		return rootConfig;
	}
}
