/*
 * (C) 2004 M.Winkler All Rights reserved
 * 
 * 
 * $Log: XmlConfigurationTest.java,v $
 * Revision 1.3  2007/08/30 07:31:37  recipient00
 * reformatted to meet builtin sun java code conventions
 *
 * Revision 1.2  2007/08/30 07:18:26  recipient00
 * gone jdk 1.5 and eclipse 3.2.x
 *
 * Revision 1.1.2.1  2007/06/03 13:15:23  recipient00
 * Migrating to maven2
 *
 * Revision 1.1.2.1  2007/02/15 07:06:22  recipient00
 * Renaming ALL packages from de.micwin.* to net.micwin.tools4j.*
 *
 * Revision 1.2.2.1  2007/02/14 22:39:24  recipient00
 * now path is of type HierarchyPath
 *
 * Revision 1.2  2004/04/18 21:47:32  recipient00
 * changed author's email to micwin@gmx.org
 *
 * Revision 1.1  2004/04/17 16:03:58  recipient00
 * Initial check in
 *
 *  
 */

package net.micwin.yajl.core.config.impl;

import java.net.URL;

import net.micwin.yajl.core.config.IConfiguration;
import net.micwin.yajl.core.config.impl.XmlConfiguration;
import junit.framework.TestCase;

/**
 * TODO Add some useful comment here
 * 
 * @author micwin@gmx.org
 * @since 16.04.2004 15:55:22
 */
public class XmlConfigurationTest extends TestCase {

    static final String XML_STRING = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>"
	    + "<sub name=\"root\">"
	    + "<intKey>4</intKey><booleanKey>true</booleanKey>"
	    + "<sub name=\"sub1\"><subKey>hello</subKey></sub>" + "</sub>";

    /**
         * Check work of XmlConfiguration (String) with correct input data.
         * 
         * @throws Throwable
         */
    public void testXmlConfigurationString() throws Throwable {
	XmlConfiguration xc = new XmlConfiguration(XML_STRING);
	assertEquals("root", xc.getName());
	assertEquals("//root", xc.getPathString());
	assertEquals(new Integer(4), xc.getInteger("intKey"));
	assertEquals(Boolean.TRUE, xc.getBoolean("booleanKey"));
	IConfiguration sub1 = xc.sub("sub1");
	assertNotNull("sub1 present", sub1);
	assertEquals("sub has correct path", "//root/sub1", sub1
		.getPathString());
	assertEquals("subKey present", "hello", sub1.getString("subKey"));
    }

    /**
         * Class to test for void XmlConfiguration(URL)
         * 
         * @throws Throwable
         */
    public void testXmlConfigurationURL() throws Throwable {
	XmlConfiguration xc = new XmlConfiguration(new URL(
		"file:src/test/resources/config/xmlconfig.xml"));
	assertEquals("root", xc.getName());
	assertEquals("//root", xc.getPathString());
	assertEquals(new Integer(4), xc.getInteger("intKey"));
	assertEquals(Boolean.TRUE, xc.getBoolean("booleanKey"));
	IConfiguration sub1 = xc.sub("sub1");
	assertNotNull("sub1 present", sub1);
	assertEquals("sub has correct path", "//root/sub1", sub1
		.getPathString());
	assertEquals("subKey present", "hello", sub1.getString("subKey"));
    }

    /**
         * Class to test for Boolean getBoolean(String, Boolean)
         * 
         * @throws Throwable
         */
    public void testGetBooleanStringBoolean() throws Throwable {
	XmlConfiguration xc = new XmlConfiguration(new URL(
		"file:src/test/resources/config/xmlconfig.xml"));
	assertEquals(Boolean.TRUE, xc
		.getBoolean("nonExistingKey", Boolean.TRUE));
    }

    /**
         * Class to test for Integer getInteger(String, Integer)
         * 
         * @throws Throwable
         */
    public void testGetIntegerStringInteger() throws Throwable {
	XmlConfiguration xc = new XmlConfiguration(new URL(
		"file:src/test/resources/config/xmlconfig.xml"));
	assertEquals(new Integer(3), xc.getInteger("nonExistingKey",
		new Integer(3)));
    }

    /**
         * Class to test for String getString(String, String)
         * 
         * @throws Throwable
         */
    public void testGetStringStringString() throws Throwable {
	XmlConfiguration xc = new XmlConfiguration(new URL(
		"file:src/test/resources/config/xmlconfig.xml"));
	assertEquals("default", xc.getString("nonExistingKey", "default"));
    }

    /**
         * Class to test for String getString(String) when requesting a CDATA
         * element.
         * 
         * @throws Throwable
         */
    public void testGetString_CDATA() throws Throwable {
	XmlConfiguration xc = new XmlConfiguration(new URL(
		"file:src/test/resources/config/xmlconfig.xml"));
	assertEquals("<<Some>>", xc.getString("cdataKey"));
    }

    /**
         * Class to test for String getString(String) when requesting a multi
         * line element.
         * 
         * @throws Throwable
         */
    public void testGetString_MultiLine() throws Throwable {
	XmlConfiguration xc = new XmlConfiguration(new URL(
		"file:src/test/resources/config/xmlconfig.xml"));
	String multiLineValue = xc.getString("multiLineKey");
	assertTrue(multiLineValue.startsWith("Line0"));
	assertTrue(multiLineValue.endsWith("Line2"));
    }

    /**
         * Class to test for String[] getArray(String)
         * 
         * @throws Throwable
         */
    public void testGetArrayString() throws Throwable {
	XmlConfiguration xc = new XmlConfiguration(new URL(
		"file:src/test/resources/config/xmlconfig.xml"));
	// String [] defaultArray = new Stroing [5] ;
	String[] array = xc.getArray("arrayKey");
	assertNotNull("arrayKey present", array);
	assertEquals("array length", 3, array.length);
	for (int i = 0; i < array.length; i++) {
	    assertEquals("elem" + (i + 1), array[i]);
	}
    }

    /**
         * Class to test for String[] getArray(String, String[])
         * 
         * @throws Throwable
         */
    public void testGetArrayStringStringArray() throws Throwable {
	XmlConfiguration xc = new XmlConfiguration(new URL(
		"file:src/test/resources/config/xmlconfig.xml"));
	String[] defaultArray = new String[5];
	assertSame(defaultArray, xc.getArray("nonExistingKey", defaultArray));
    }

    public void testAssertAvailable() {
	// TODO Implement assertAvailable().
    }

    public void testKeys() {
	// TODO Implement keys().
    }
}