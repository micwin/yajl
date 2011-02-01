package net.micwin.tools4j.instances;

import junit.framework.TestCase;

import net.micwin.tools4j.config.ConfigException;
import net.micwin.tools4j.config.IConfiguration;
import net.micwin.tools4j.config.impl.InMemoryConfiguration;
import net.micwin.tools4j.exceptions.TechException;
import net.micwin.tools4j.instances.ReflectionFactory.InvocationStrategy;

public class ReflectionFactoryTest extends TestCase {

    private ReflectionFactory composeFactory(Class clazz,
	    InvocationStrategy strategy) throws ConfigException {
	InMemoryConfiguration imc = new InMemoryConfiguration();
	imc.put(ReflectionFactory.KEY_INVOCATION_CLASS, clazz.getName());
	imc.put(ReflectionFactory.KEY_INVOCATION_STRATEGY, strategy.toString());

	switch (strategy) {
	case CONFIG_CONSTRUCTOR:
	    imc.put(ReflectionFactory.KEY_INVOCATION_CONFIG, imc);
	    break;

	case STATIC_FIELD:
	    imc.put(ReflectionFactory.KEY_STATIC_FIELD, "INSTANCE");
	    break;

	case DEFAULT_CONSTRUCTOR:
	    break;

	default:
	    throw new TechException("unexpected situation in switch / case");

	}

	ReflectionFactory fac = new ReflectionFactory((IConfiguration) imc);
	return fac;
    }

    public void testDefaultInvocation() throws Exception {

	ReflectionFactory fac = composeFactory(DummyObject.class,
		ReflectionFactory.InvocationStrategy.DEFAULT_CONSTRUCTOR);
	DummyObject instance = (DummyObject) fac.newInstance();
	assertNotNull(instance);
	assertEquals(ReflectionFactory.InvocationStrategy.DEFAULT_CONSTRUCTOR,
		instance.strategy);
    }

    public void testConfigInvocation() throws Exception {

	ReflectionFactory fac = composeFactory(DummyObject.class,
		ReflectionFactory.InvocationStrategy.CONFIG_CONSTRUCTOR);
	DummyObject instance = (DummyObject) fac.newInstance();
	assertNotNull(instance);
	assertEquals(ReflectionFactory.InvocationStrategy.CONFIG_CONSTRUCTOR,
		instance.strategy);
    }

    public void testFieldInvocation() throws Exception {

	ReflectionFactory fac = composeFactory(DummyObject.class,
		ReflectionFactory.InvocationStrategy.STATIC_FIELD);
	DummyObject instance = (DummyObject) fac.newInstance();
	assertNotNull(instance);
	assertEquals(ReflectionFactory.InvocationStrategy.STATIC_FIELD,
		instance.strategy);
    }

}
