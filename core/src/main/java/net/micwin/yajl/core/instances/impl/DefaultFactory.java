package net.micwin.yajl.core.instances.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.micwin.yajl.core.instances.Factory;

/**
 * A Factory that creates instances of class T by calling the default
 * constructor of T (which must be public and may not throw any exceptions).
 * 
 * @author MicWin
 * 
 * @param <T>
 */
public class DefaultFactory<T> implements Factory<T> {

	private Constructor<T> constructor;

	public DefaultFactory(Class<T> clazz) throws SecurityException,
			NoSuchMethodException {
		constructor = clazz.getConstructor();
	}

	/**
	 * Create a new instance of type T
	 * 
	 * @throws IllegalStateException
	 *             if something happens what shouldnt (by means of this class).
	 */
	public T create() {
		try {
			return constructor.newInstance();
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (InstantiationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

}
