package net.micwin.yajl.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.StringTokenizer;

import net.micwin.yajl.core.config.IConfiguration;

/**
 * This is a singleton for convenient access to some elements of yajl-core. Note
 * that you have to activate this explicisely by setting the system property
 * <code>YajlCoreSingleton.rootConfigClass</code> to a class implementing
 * {@link IConfiguration} and providing a default constructor.
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

	static IConfiguration rootConfig;

	static {

		initRoot();

	}

	static void initRoot() {
		rootConfig = null;

		String rootConfigProperty = System.getProperty(YajlCoreSingleton.class
				.getSimpleName() + ".rootConfig");

		if (rootConfigProperty == null) {
			return;
		}

		StringTokenizer tokenizer = new StringTokenizer(rootConfigProperty, ",");
		String rootConfigClassName = tokenizer.nextToken().trim();
		try {
			Class rootConfigClass = Class.forName(rootConfigClassName);

			Class[] paramTypes = new Class[tokenizer.countTokens() - 1];
			Arrays.fill(paramTypes, String.class);

			String[] params = new String[paramTypes.length];
			rootConfig = (IConfiguration) rootConfigClass.getConstructor(
					paramTypes).newInstance(params);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
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
	public static synchronized IConfiguration getRootConfig() {
		checkEnabled();
		return rootConfig;
	}
}
