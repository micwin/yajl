package net.micwin.yajl.core.data.structures;

import net.micwin.yajl.core.data.structures.HierarchyPath;
import junit.framework.TestCase;

public class HierarchyPathTest extends TestCase {

    public HierarchyPathTest(String arg0) {
	super(arg0);
    }

    public void testConstructor_StringStringArray() {
	HierarchyPath path = new HierarchyPath("-", '?', new String[] { "1",
		"&", "�" });
	assertEquals("-", path._rootSymbol);
	assertEquals('?', path._pathSeparator);
	assertEquals("1", path._pathNames[0]);
	assertEquals("&", path._pathNames[1]);
	assertEquals("�", path._pathNames[2]);
    }

    public void testConstructor_StringStringArray_Nulls() {
	try {
	    new HierarchyPath(null, '?', new String[] { "1", "&", "�" });
	    fail("accepting null root symbol");
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	try {
	    new HierarchyPath("-", '?', (String[]) null);
	    fail("accepting null names array");
	} catch (IllegalArgumentException e) {
	    // w^5
	}
    }

    public void testConstructor_StringStringString() {
	HierarchyPath path = new HierarchyPath("-", '?', "-1?&?�");
	assertEquals("-", path._rootSymbol);
	assertEquals('?', path._pathSeparator);
	assertEquals("1", path._pathNames[0]);
	assertEquals("&", path._pathNames[1]);
	assertEquals("�", path._pathNames[2]);
    }

    public void testConstructor_StringStringString_EmptyRootSymbol() {
	HierarchyPath path = new HierarchyPath("", '?', "1?&?�");
	assertEquals("", path._rootSymbol);
	assertEquals('?', path._pathSeparator);
	assertEquals("1", path._pathNames[0]);
	assertEquals("&", path._pathNames[1]);
	assertEquals("�", path._pathNames[2]);
    }

    public void testConstructor_StringStringString_NoNulls() {
	try {
	    new HierarchyPath(null, '?', "1&�");
	    fail("accepting null root symbol");
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	try {
	    new HierarchyPath("-", '?', (String) null);
	    fail("accepting null names array");
	} catch (IllegalArgumentException e) {
	    // w^5
	}
    }

    public void testConstructor_ParentName() {
	HierarchyPath parent = new HierarchyPath("//", '/', "//hello");
	HierarchyPath child = new HierarchyPath(parent, "world");
	assertEquals("//", child._rootSymbol);
	assertEquals('/', child._pathSeparator);
	assertEquals("hello", child._pathNames[0]);
	assertEquals("world", child._pathNames[1]);
    }

    public void testConstructor_ParentName_Nulls() {
	try {
	    new HierarchyPath(null, "world");
	    fail("null parent accepted");
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	try {
	    new HierarchyPath(new HierarchyPath("//", '/', "//blafasle"), null);
	    fail("null name accepted");
	} catch (IllegalArgumentException e) {
	    // w^5
	}
    }

    public void testGetPathString() {
	HierarchyPath path = new HierarchyPath("//", '/', "//blafasle");
	assertEquals("//blafasle", path.getPathString());
	assertSame(path.getPathString(), path.getPathString());
    }
}
