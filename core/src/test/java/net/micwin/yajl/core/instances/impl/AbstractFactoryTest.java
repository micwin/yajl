package net.micwin.yajl.core.instances.impl;

import java.util.LinkedList;
import java.util.List;

import net.micwin.yajl.core.config.ConfigException;
import net.micwin.yajl.core.config.impl.InMemoryConfiguration;
import net.micwin.yajl.core.instances.IFactory;
import net.micwin.yajl.core.instances.impl.AbstractFactory;

import junit.framework.TestCase;

public class AbstractFactoryTest extends TestCase {

    InMemoryConfiguration config = null;

    protected void setUp() throws Exception {
	super.setUp();
	AbstractFactory.factories = null;

	config = new InMemoryConfiguration();
	config.put("lenient", Boolean.FALSE);
	config.put("targets", new String[] { List.class.getName() });
	InMemoryConfiguration llConfig = new InMemoryConfiguration();
	llConfig.put("class", LinkedList.class.getName());
	config.put(List.class.getName(), llConfig);
    }

    protected void tearDown() throws Exception {
	super.tearDown();
	AbstractFactory.factories = null;
    }

    public void testGetFactory() throws ConfigException {
	AbstractFactory.init(config);
	IFactory<List> ff = AbstractFactory.getFactory(List.class);
	List newInstance = ff.create();
	assertNotNull(newInstance);
	assertEquals(LinkedList.class, newInstance.getClass());
	newInstance.add("");
	assertEquals(1, newInstance.size());

    }

    public void testInit_lenient_true() throws ConfigException {

	config.put("lenient", Boolean.TRUE);
	AbstractFactory.init(config);
	assertTrue(AbstractFactory.lenient);
    }

    public void testInit_lenient_false() throws ConfigException {
	AbstractFactory.init(config);
	assertFalse(AbstractFactory.lenient);
    }
}