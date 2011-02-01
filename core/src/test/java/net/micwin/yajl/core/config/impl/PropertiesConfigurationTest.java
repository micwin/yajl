package net.micwin.yajl.core.config.impl;

import java.io.IOException;
import java.util.Properties;

import net.micwin.yajl.core.config.MissingConfigKeyException;
import net.micwin.yajl.core.config.impl.PropertiesConfiguration;
import net.micwin.yajl.core.data.structures.HierarchyPath;
import junit.framework.TestCase;

/**
 * Unit Tests for Class net.micwin.yajl.core.config.impl.PropertiesConfiguration
 * 
 * @see net.micwin.yajl.core.config.impl.PropertiesConfiguration
 * @author micwin@gmx.org
 * @since 09.12.2003 17:40:36
 */
public class PropertiesConfigurationTest extends TestCase {

    PropertiesConfiguration pc = null;

    private static final HierarchyPath stupidPath = new HierarchyPath("//",
	    '/', new String[] { "stupidPath" });

    /**
         * @see junit.framework.TestCase#setUp()
         */
    protected void setUp() throws Exception {
	super.setUp();
	Properties props = new Properties();
	props.put("key1", "  value1");
	props.put("key2", "value2  ");
	props.put("boolean", "true");
	props.put("integer", "754");
	props.put("string", "value");
	props.put("array1", "0,1,2,3");
	props.put("commaStartingArray", ",1,2,3");
	props.put("commaEndingArray", "0,1,2,");
	props.put("commaOnlyArray", ",,,");
	props.put("pc.separator", ",");
	props.put("pc.trim", "true");
	pc = new PropertiesConfiguration(stupidPath, props, System
		.getProperties(), System.getProperties());
    }

    /**
         * 
         * 
         */
    public void testPropertiesConfiguration_Properties() {
	assertTrue(pc.isStringTrimming());
	assertEquals(",", pc.getSeparator());
	assertEquals("//stupidPath", pc.getPathString());
    }

    /**
         * @throws MissingConfigKeyException
         * @throws IOException
         */
    public void testPropertiesConfiguration_File()
	    throws MissingConfigKeyException, IOException {
	// pc aus Datei konfigurieren
	pc = new PropertiesConfiguration(
		"local:src/test/resources/config/root.properties");
	assertTrue(pc.isStringTrimming());
	assertEquals(":", pc.getSeparator());
	assertEquals("valueOne", pc.getString("keyOne"));
	assertEquals("valueTwo", pc.getString("keyTwo"));
	// Unter-Gruppe testen
	PropertiesConfiguration sub = (PropertiesConfiguration) pc
		.sub("groupOne");
	assertTrue(!sub.isStringTrimming());
	assertEquals("-", sub.getSeparator());
	assertEquals("Pfad der Untergruppe", "//pc/groupOne", sub
		.getPathString());
	String[] array = sub.getArray("anArray");
	assertNotNull("array wurde ausgelesen", array);
	assertEquals("array-L�nge", 4, array.length);
	for (int i = 0; i < array.length; i++) {
	    assertEquals("element " + i, "" + i, array[i]);
	}
    }

    /**
         * @throws MissingConfigKeyException
         * @throws IOException
         */
    public void testPropertiesConfiguration_File_BaseDir()
	    throws MissingConfigKeyException, IOException {
	// pc aus Datei konfigurieren
	pc = new PropertiesConfiguration(
		"local:src/test/resources/config/basedir.properties");
	assertTrue(pc.isStringTrimming());
	assertEquals(":", pc.getSeparator());
	assertEquals("valueOne", pc.getString("keyOne"));
	assertEquals("valueTwo", pc.getString("keyTwo"));
	// Unter-Gruppe testen
	PropertiesConfiguration sub = (PropertiesConfiguration) pc
		.sub("groupOne");
	assertTrue(!sub.isStringTrimming());
	assertEquals("-", sub.getSeparator());
	assertEquals("Pfad der Untergruppe", "//pc/groupOne", sub
		.getPathString());
	String[] array = sub.getArray("anArray");
	assertNotNull("array wurde ausgelesen", array);
	assertEquals("array-L�nge", 4, array.length);
	for (int i = 0; i < array.length; i++) {
	    assertEquals("element " + i, "" + i, array[i]);
	}
    }

    /**
         * @throws MissingConfigKeyException
         */
    public void testGetBoolean() throws MissingConfigKeyException {
	assertTrue(pc.getBoolean("boolean").booleanValue());
    }

    /**
         * @throws MissingConfigKeyException
         */
    public void testGetInteger() throws MissingConfigKeyException {
	assertEquals(754, pc.getInteger("integer").intValue());
    }

    /**
         * @throws MissingConfigKeyException
         */
    public void testGetString() throws MissingConfigKeyException {
	assertEquals("value", pc.getString("string"));
    }

    /**
         * 
         * 
         */
    public void testGetString_negative() {
	try {
	    pc.getString("dfkjhaskjfg");
	    fail("exception not thrown");
	} catch (MissingConfigKeyException e) {
	    // w^5
	}
    }

    /**
         * @throws MissingConfigKeyException
         */
    public void testGetArray() throws MissingConfigKeyException {
	String[] array1 = pc.getArray("array1");
	assertEquals(4, array1.length);
	for (int i = 0; i < array1.length; i++) {
	    assertEquals("array1", "" + i, array1[i]);
	}
    }

    /**
         * 
         * 
         */
    public void testGetArray_Negative() {
	try {
	    pc.getArray("dsjhakc hel");
	    fail("exception not thrown");
	} catch (MissingConfigKeyException e) {
	    // w^5
	}
    }

    /**
         * @throws MissingConfigKeyException
         */
    public void testGetArray_CommaStartingArray()
	    throws MissingConfigKeyException {
	String[] array1 = pc.getArray("commaStartingArray");
	assertEquals(4, array1.length);
	assertEquals("", array1[0]);
	for (int i = 1; i < array1.length; i++) {
	    assertEquals("commaStartingArray[" + i + "]", "" + i, array1[i]);
	}
    }

    /**
         * @throws MissingConfigKeyException
         */
    public void testGetArray_CommaEndingArray()
	    throws MissingConfigKeyException {
	String[] array1 = pc.getArray("commaEndingArray");
	assertEquals(4, array1.length);
	assertEquals("", array1[3]);
	for (int i = 0; i < 3; i++) {
	    assertEquals("commaEndingArray[" + i + "]", "" + i, array1[i]);
	}
    }

    /**
         * @throws MissingConfigKeyException
         */
    public void testGetArray_CommaOnlyArray() throws MissingConfigKeyException {
	String[] array1 = pc.getArray("commaOnlyArray");
	assertEquals(4, array1.length);
	for (int i = 0; i < array1.length; i++) {
	    assertEquals("commaOnlyArray[" + i + "]", "", array1[i]);
	}
    }

    /**
         * @throws IOException
         * @throws MissingConfigKeyException
         */
    public void testGetArray_LongSeparator() throws IOException,
	    MissingConfigKeyException {
	Properties props = new Properties();
	props.put("array2", "0blabba 1blabba 2blabba 3");
	props.put("pc.separator", "blabba ");
	props.put("pc.trim", "true");
	pc = new PropertiesConfiguration(props, System.getProperties(), System
		.getProperties());
	String[] array2 = pc.getArray("array2");
	assertEquals(4, array2.length);
	for (int i = 0; i < array2.length; i++) {
	    assertEquals("array2", "" + i, array2[i]);
	}
    }

    /**
         * @throws IOException
         * @throws MissingConfigKeyException
         */
    public void testGetArray_Resolving() throws IOException,
	    MissingConfigKeyException {
	Properties props = new Properties();
	props.put("array2." + System.getProperty("user.name"),
		"${user.home} , ${user.dir}");
	props.put("pc.trim", "true");
	pc = new PropertiesConfiguration(props, System.getProperties(), System
		.getProperties());
	String[] array2 = pc.getArray("array2.${user.name}");
	assertEquals(2, array2.length);
	if ((System.getProperty("user.home") == null)
		|| (System.getProperty("user.dir") == null)) {
	    System.getProperties().list(System.err);
	}
	assertEquals(System.getProperty("user.home"), array2[0]);
	assertEquals(System.getProperty("user.dir"), array2[1]);
    }

    /**
         * @throws MissingConfigKeyException
         */
    public void testAssertAvailable() throws MissingConfigKeyException {
	pc.assertAvailable(new String[] { "key1", "key2" });
    }

    /**
         * 
         * 
         */
    public void testAssertAvailable_Fail() {
	try {
	    pc
		    .assertAvailable(new String[] { "key1", "key2",
			    "nonPresentKey2" });
	    fail("Exception not thrown");
	} catch (MissingConfigKeyException e) {
	    // w^5
	    String[] missingKeys = e.getMissingKeys();
	    assertNotNull(missingKeys);
	    assertEquals(1, missingKeys.length);
	    assertEquals("nonPresentKey2", missingKeys[0]);
	}
    }

    /**
         * 
         * 
         */
    public void testGetPath() {
	assertEquals("//stupidPath", pc.getPathString());
    }

    /**
         * 
         * 
         */
    public void testKeys() {
	String[] keys = pc.keys();
	assertEquals(9, keys.length);
    }
}