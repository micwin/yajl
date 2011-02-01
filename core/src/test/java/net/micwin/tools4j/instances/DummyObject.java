package net.micwin.tools4j.instances;

import net.micwin.tools4j.config.IConfiguration;
import net.micwin.tools4j.instances.ReflectionFactory.InvocationStrategy;

public class DummyObject {

    ReflectionFactory.InvocationStrategy strategy = null;

    public static final DummyObject INSTANCE = new DummyObject(
	    InvocationStrategy.STATIC_FIELD);

    public DummyObject() {
	strategy = ReflectionFactory.InvocationStrategy.DEFAULT_CONSTRUCTOR;
    }

    public DummyObject(IConfiguration config) {
	strategy = ReflectionFactory.InvocationStrategy.CONFIG_CONSTRUCTOR;
    }

    private DummyObject(InvocationStrategy strategy) {
	this.strategy = strategy;
    }
}
