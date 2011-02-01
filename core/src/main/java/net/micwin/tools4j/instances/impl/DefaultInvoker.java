package net.micwin.tools4j.instances.impl;

import net.micwin.tools4j.exceptions.TechException;
import net.micwin.tools4j.instances.IInvoker;

/**
 * An invoker instantiating by calling the default constructor.
 * 
 * F@author micwin@micwin.net
 * 
 */
public class DefaultInvoker<T> implements IInvoker {

    private final Class<T> clazz;

    public DefaultInvoker(Class<T> name) {
	this.clazz = name;

    }

    public T invoke() throws TechException {
	try {
	    return clazz.newInstance();
	} catch (InstantiationException e) {
	    throw new TechException("cannot invoke class '" + clazz + "'", e);

	} catch (IllegalAccessException e) {
	    throw new TechException("cannot invoke class '" + clazz + "'", e);
	}
    }

}
