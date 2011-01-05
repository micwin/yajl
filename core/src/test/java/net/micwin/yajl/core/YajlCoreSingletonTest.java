package net.micwin.yajl.core;

import net.micwin.yajl.core.config.impl.PropertiesConfigImpl;
import junit.framework.TestCase;

public class YajlCoreSingletonTest extends TestCase {

	public void testGetRootConfig() {
		PropertiesConfigImpl propsConfig = new PropertiesConfigImpl();
		YajlCoreSingleton.rootConfig = propsConfig;
		assertNotNull(YajlCoreSingleton.getRootConfig());
		assertEquals(propsConfig, YajlCoreSingleton.getRootConfig());
	}

	public void testGetRootConfig_Fail() {
		YajlCoreSingleton.rootConfig = null;
		try {
			YajlCoreSingleton.getRootConfig();
			fail();
		} catch (IllegalStateException e) {
			// w^5
		}
	}

}
