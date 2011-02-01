package net.micwin.tools4j.instances.impl;

import java.lang.reflect.Field;

import net.micwin.tools4j.config.ConfigException;
import net.micwin.tools4j.exceptions.TechException;
import net.micwin.tools4j.instances.IInvoker;

/**
 * An invoker accessing a static field instead of creating an instance. This
 * field must be accessible, otherwise an exception is thrown.
 * 
 * @author micwin@micwin.net
 * 
 */
public class StaticFieldInvoker implements IInvoker {

    private final Class clazz;

    private final String fieldName;

    private Field field;

    public StaticFieldInvoker(Class clazz, String fieldName)
	    throws ConfigException {
	this.clazz = clazz;
	this.fieldName = fieldName;

	try {
	    field = clazz.getField(fieldName);
	} catch (SecurityException e) {
	    throw new ConfigException("cannot retrieve field '" + fieldName
		    + "' of class '" + clazz + "'", e);
	} catch (NoSuchFieldException e) {
	    throw new ConfigException("cannot retrieve field '" + fieldName
		    + "' of class '" + clazz + "'", e);
	}
    }

    public Object invoke() throws TechException {
	try {
	    return field.get(clazz);
	} catch (IllegalArgumentException e) {
	    throw new TechException("cannot access field '" + fieldName
		    + "' of class '" + clazz + "'", e);

	} catch (IllegalAccessException e) {
	    throw new TechException("cannot access field '" + fieldName
		    + "' of class '" + clazz + "'", e);
	}
    }

}
