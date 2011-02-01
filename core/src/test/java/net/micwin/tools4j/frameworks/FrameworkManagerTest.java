/*
 * (C) 2003 M.Winkler All Rights reserved
 * 
 * 
 * $Log: FrameworkManagerTest.java,v $
 * Revision 1.3  2007/08/30 07:31:39  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:27  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:23  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:23  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.6  2004/04/18 22:29:05  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.5  2004/04/16 12:27:53  recipient00
 * once more adjusted formattings :( and extended javadocs
 * Revision 1.4 2004/04/15 19:02:21
 * recipient00 Messed around with malformed comments ...
 * 
 * 
 * Revision 1.3 2004/01/11 20:54:59 recipient00 Translated into mere english
 * 
 * Revision 1.2 2004/01/10 20:21:39 recipient00 Added todo tags for translation
 * and legal notes
 * 
 * Revision 1.1 2004/01/07 22:34:20 recipient00 initial check in
 * 
 * Revision 1.2 2003/12/30 21:13:18 micwin pseudo-Kollisionen :(
 * 
 * Revision 1.1 2003/12/16 09:26:22 micwin initial check in
 *  
 */

package net.micwin.tools4j.frameworks;

import java.io.IOException;

import net.micwin.tools4j.config.ConfigException;
import net.micwin.tools4j.config.IConfiguration;
import net.micwin.tools4j.config.impl.InMemoryConfiguration;
import net.micwin.tools4j.config.impl.PropertiesConfiguration;
import junit.framework.TestCase;

/**
 * T Unit tests for class FrameworkManager.
 * 
 * TODO add legal notes
 * 
 * @author micwin@gmx.org
 * @since 10.12.2003 08:59:46
 */
public class FrameworkManagerTest extends TestCase {

    /**
         * Constructor for FrameworkManagerTest.
         * 
         * @param arg0
         */
    public FrameworkManagerTest(String arg0) {
	super(arg0);
    }

    private IConfiguration createDummyConfig() {
	InMemoryConfiguration imc = new InMemoryConfiguration();
	imc.put(FrameworkManager.KEY_FRAMEWORK_LIST,
		new String[] { "dummyFramework" });
	imc.put(FrameworkManager.KEY_RESUME, new Boolean(true));
	InMemoryConfiguration fwConfig = imc.createSub("dummyFramework");
	fwConfig.put(FrameworkManager.KEY_FRAMEWORK_CLASS, DummyFramework.class
		.getName());
	imc.createSub("log4j");
	return imc;
    }

    /**
         * @throws ConfigException
         */
    public void testFrameworkManager() throws ConfigException {
	FrameworkManager fm = new FrameworkManager();
	fm.init(createDummyConfig());
	DummyFramework df = (DummyFramework) fm.getFramework("dummyFramework");
	assertNotNull(df);
    }

    /**
         * @throws ConfigException
         */
    public void testFrameworkManagerIConfiguration() throws ConfigException {
	FrameworkManager fm = new FrameworkManager(createDummyConfig());
	DummyFramework df = (DummyFramework) fm.getFramework("dummyFramework");
	assertNotNull(df);
    }

    /**
         * @throws ConfigException
         */
    public void testShutDown() throws ConfigException {
	FrameworkManager fm = new FrameworkManager(createDummyConfig());
	DummyFramework df = (DummyFramework) fm.getFramework("dummyFramework");
	fm.shutDown();
	assertTrue(df.shutDown);
    }

    /**
         * @throws ConfigException
         * @throws IOException
         */
    public void testInitFromProperties() throws ConfigException, IOException {
	PropertiesConfiguration pc = new PropertiesConfiguration(
		"local:src/test/resources/framework/root.properties");
	FrameworkManager fm = new FrameworkManager(pc);
	DummyFramework df1 = (DummyFramework) fm.getFramework("dummy-1");
	DummyFramework df2 = (DummyFramework) fm.getFramework("dummy-2");
	assertTrue(df1 != df2);
    }
}