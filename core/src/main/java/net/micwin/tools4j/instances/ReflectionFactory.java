package net.micwin.tools4j.instances;

import net.micwin.tools4j.exceptions.TechException;
import net.micwin.tools4j.instances.impl.ConfigurationInvoker;
import net.micwin.tools4j.instances.impl.DefaultInvoker;
import net.micwin.tools4j.instances.impl.StaticFieldInvoker;
import net.micwin.yajl.core.config.ConfigException;
import net.micwin.yajl.core.config.IConfiguration;

/**
 * Instantiates classes by using reflection. The strategy on how to create /
 * access the instances is specified using the key <code>strategy</code>:
 * <ul>
 * <li><code>DEFAULT_CONSTRUCTOR</code>: create instances by simply calling
 * the default constructor</li>
 * <li><code>CONFIG_CONSTRUCTOR</code>: create instances by calling the
 * constructor taking an instance of <code>IConfiguration</code> that is
 * created using the config's sub group <code>config</code>.</li>
 * <li><code>STATIC_FIELD</code>: create instances by reading a static field
 * specified with configuration key <code>field</code></li>
 * </ul>
 * 
 * @see IConfiguration
 * @author micwin@micwin.net
 * 
 * 
 */
public class ReflectionFactory <T> implements IFactory {

    public static enum InvocationStrategy {

	DEFAULT_CONSTRUCTOR, CONFIG_CONSTRUCTOR, STATIC_FIELD;
    }

    /**
         * key <code>class</code> should contain the fully qualified name to
         * the class to be invoked.
         */
    public static final String KEY_INVOCATION_CLASS = "class";

    /**
         * key <code>strategy</code> states wether strategy
         * <code>DEFAULT_CONSTRUCTOR</code>, <code>CONFIG_CONSTRUCTOR</code>
         * or <code>STATIC_FIELD</code> is used to create instances.
         */
    public static final String KEY_INVOCATION_STRATEGY = "strategy";

    /**
         * If strategy <code>CONFIG_CONSTRUCTOR</code> is used, then this key
         * is used to create the instance of IConfiguration that is to passed to
         * the constructor.
         */
    public static final String KEY_INVOCATION_CONFIG = "config";

    /**
         * If strategy <code>STATIC_FIELD</code> is used, then this key holds
         * the name of the appropriate static field.
         */
    public static final String KEY_STATIC_FIELD = "field";

    IInvoker <T> invoker;

    /**
         * Creates a new ReflectionFactory.
         * 
         * @param configuration
         *                The configuration to hold all keys described above.
         * @throws ConfigException
         */
    public ReflectionFactory(IConfiguration configuration)
	    throws ConfigException {

	String clazzName = configuration.getString(KEY_INVOCATION_CLASS);
	Class clazz = null;
	try {
	    clazz = Class.forName(clazzName);
	    
	} catch (ClassNotFoundException e) {
	    throw new ConfigException(
		    "cannot locate class '" + clazzName + "'", e);
	}

	InvocationStrategy strategy = InvocationStrategy.valueOf(configuration
		.getString(KEY_INVOCATION_STRATEGY,
			InvocationStrategy.DEFAULT_CONSTRUCTOR.name()));

	switch (strategy) {

	case STATIC_FIELD:

	    String fieldName = configuration.getString(KEY_STATIC_FIELD);
	    invoker = new StaticFieldInvoker(clazz, fieldName);
	    break;

	case CONFIG_CONSTRUCTOR:
	    try {
		invoker = new ConfigurationInvoker(clazz, configuration
			.sub(KEY_INVOCATION_CONFIG));
	    } catch (SecurityException e) {
		throw new ConfigException(
			"cannot setup config invoker for class '" + clazz + "'",
			e);
	    } catch (NoSuchMethodException e) {
		throw new ConfigException(
			"cannot setup config invoker for class '" + clazz + "'",
			e);
	    }
	    break;

	case DEFAULT_CONSTRUCTOR:
	default:
	    invoker = new DefaultInvoker <T>(clazz);
	    break;

	}

    }

    /**
         * Creates a new Instance of the specified class.
         * 
         * @return
         * @throws TechException
         */
    public T newInstance() throws TechException {
	return invoker.invoke();
    }
}
