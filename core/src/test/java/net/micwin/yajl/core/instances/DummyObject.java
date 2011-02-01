package net.micwin.yajl.core.instances;

import net.micwin.yajl.core.config.IConfiguration;
import net.micwin.yajl.core.instances.impl.ReflectionFactory;
import net.micwin.yajl.core.instances.impl.ReflectionFactory.InvocationStrategy;

public class DummyObject {

    public ReflectionFactory.InvocationStrategy strategy = null;

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
