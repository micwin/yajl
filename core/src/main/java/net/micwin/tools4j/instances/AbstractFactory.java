package net.micwin.tools4j.instances;

import java.util.HashMap;
import java.util.Map;

import net.micwin.tools4j.config.ConfigException;
import net.micwin.tools4j.config.ConfigManager;
import net.micwin.tools4j.config.IConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AbstractFactory {

    private static final Log L = LogFactory.getLog(AbstractFactory.class);

    // ***************************************************
    // *** static
    // ***************************************************
    static Map<Class, IFactory> factories = null;

    static boolean lenient;

    public synchronized static IFactory getFactory(Class targetClass) {

	if (factories == null) {
	    throw new IllegalStateException("not yet initialized");

	}
	IFactory factory = factories.get(targetClass);
	if (factory == null)
	    throw new IllegalArgumentException(
		    "no factory registered for target '"
			    + targetClass.getName() + "'");
	return factory;
    }

    public synchronized static void init(IConfiguration configuration)
	    throws ConfigException {

	if (factories != null) {
	    throw new IllegalStateException("already configured");
	} else
	    factories = new HashMap<Class, IFactory>();

	if (configuration == null) {
	    configuration = ConfigManager.getSingletonConfig().sub(
		    AbstractFactory.class.getName());
	}

	lenient = configuration.getBoolean("lenient");

	String[] targetClassNames = configuration.getArray("targets");

	for (String targetClassName : targetClassNames) {

	    // locate target class
	    IConfiguration factorySub = configuration.sub(targetClassName);
	    try {
		Class targetClass = Class.forName(targetClassName);
		// initialize factory by reflection
		ReflectionFactory factory = new ReflectionFactory(factorySub);
		factories.put(targetClass, factory);
		L.info("installed factory for target='" + targetClass.getName()
			+ "'");
	    } catch (ClassNotFoundException e) {

		if (lenient) {
		    e.printStackTrace();
		} else {
		    throw new ConfigException(
			    "cannot create factory for targetClass '"
				    + targetClassName + "' - " + e, e);
		}
	    }
	}
    }
}
