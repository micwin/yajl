package net.micwin.tools4j.instances.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.micwin.tools4j.config.IConfiguration;
import net.micwin.tools4j.exceptions.TechException;
import net.micwin.tools4j.instances.IInvoker;

/**
 * An invoker invoking by using an Iconfiguration instance.
 * 
 * @author micwin@micwin.net
 * 
 */

public class ConfigurationInvoker implements IInvoker {

    private static final Class[] CONSTRUCTOR_PARAMETER_CLASSES = new Class[] { IConfiguration.class };

    final Constructor constructor;

    final Object[] parameters;

    private final Class clazz;

    public ConfigurationInvoker(Class clazz, IConfiguration configuration)
	    throws SecurityException, NoSuchMethodException {
	this.clazz = clazz;
	parameters = new Object[] { configuration };
	constructor = clazz.getConstructor(CONSTRUCTOR_PARAMETER_CLASSES);
    }

    public Object invoke() throws TechException {
	try {
	    return constructor.newInstance(parameters);
	} catch (IllegalArgumentException e) {
	    throw new TechException("cannot invoke class '" + clazz
		    + "' with config constructor", e);
	} catch (InstantiationException e) {
	    throw new TechException("cannot invoke class '" + clazz
		    + "' with config constructor", e);
	} catch (IllegalAccessException e) {
	    throw new TechException("cannot invoke class '" + clazz
		    + "' with config constructor", e);
	} catch (InvocationTargetException e) {
	    throw new TechException("cannot invoke class '" + clazz
		    + "' with config constructor", e);
	}
    }
}
