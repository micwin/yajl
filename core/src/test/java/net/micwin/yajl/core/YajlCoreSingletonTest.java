package net.micwin.yajl.core;

import net.micwin.yajl.core.config.impl.InMemoryConfiguration;
import junit.framework.TestCase;

public class YajlCoreSingletonTest extends TestCase {

	public void testGetRootConfig() {
		YajlCoreSingleton.rootConfig = new InMemoryConfiguration();
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
