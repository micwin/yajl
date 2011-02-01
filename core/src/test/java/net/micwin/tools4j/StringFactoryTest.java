package net.micwin.tools4j;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * Tests the class <code>StringFactory</code>.
 * 
 * TODO translate javadocs into english, add legal notes
 * 
 * @see net.micwin.tools4j.StringFactory
 * @author micwin@gmx.org
 * @since 09.04.2003
 */
public class StringFactoryTest extends TestCase {

    /**
         * Constructor for StringFactoryTest.
         * 
         * @param arg0
         */
    public StringFactoryTest(String arg0) {
	super(arg0);
    }

    // --------------------------------------------------------------
    // -- test the static methods
    // --------------------------------------------------------------

    public void testToString_ObjectA() {
	String[] array = { "one", "two", "three" };
	String result = StringFactory.toString("{", array, ",", "}");
	assertEquals("{one,two,three}", result);

    }

    public void testToString_ObjectA_SingleNullElement() {
	String[] array = { "one", null, "three" };
	String result = StringFactory.toString("{", array, ",", "}");
	assertEquals("{one,null,three}", result);
    }

    public void testToString_ObjectA_EmptyArray() {
	String[] array = {};
	String result = StringFactory.toString("{", array, ",", "}");
	assertEquals("{}", result);
    }

    public void testToString_ObjectA_OneElementOnly() {
	String[] array = { "one" };
	String result = StringFactory.toString("{", array, ",", "}");
	assertEquals("{one}", result);
    }

    public void testToString_ObjectA_Null() {
	String[] array = null;
	String result = StringFactory.toString("{", array, ",", "}");
	assertEquals("{null}", result);
    }

    public void testWrap_HonorSpaces() {
	String text = "Dies ist ein Text, den es umzubrechen gilt.";
	String result = StringFactory.wrap(text, 10, true);
	assertEquals("Dies ist\nein Text,\nden es\numzubrechen\ngilt.", result);
    }

    public void testWrap_HonorSpaces_MultipleSpaces() {
	String text = "Dies\t \nist\tein\nText,\t\t     \tden\n\n\nes\t\n\t\numzubrechen     gilt.";
	String result = StringFactory.wrap(text, 10, true);
	assertEquals("Dies ist\nein Text,\nden es\numzubrechen\ngilt.", result);
    }

    public void testWrap_DontHonorSpaces() {
	String text = "Dies ist ein Text, den es umzubrechen gilt.";
	String result = StringFactory.wrap(text, 10, false);
	assertEquals("Dies ist e\nin Text, d\nen es umzu\nbrechen gi\nlt.",
		result);
    }

    public void testAlignLeft_Legacy() {
	assertEquals("text++++++", StringFactory.alignLeft("text",
		"++++++++++", true));
    }

    public void testAlignLeft_EmptyText() {
	assertEquals("++++++++++", StringFactory.alignLeft("", "++++++++++",
		true));
    }

    public void testAlignLeft_EmptyMargin() {
	assertEquals("", StringFactory.alignLeft("text", "", true));
    }

    public void testAlignLeft_TextLongerThanMargin() {
	assertEquals("tex", StringFactory.alignLeft("text", "---", true));
    }

    public void testAlignRight() {
	String margin = "--------------------";
	String text = "text";
	String result = StringFactory.alignRight(text, margin, true);
	assertEquals("----------------text", result);
    }

    public void testAlignRight_TextEmpty() {
	String margin = "--------------------";
	String text = "";
	String result = StringFactory.alignRight(text, margin, true);
	assertEquals("--------------------", result);
    }

    public void testAlignRight_MarginEmpty_NoStrip() {
	String margin = "";
	String text = "text";
	String result = StringFactory.alignRight(text, margin, false);
	assertEquals("text", result);
    }

    public void testAlignRight_MarginEmpty_Strip() {
	String margin = "";
	String text = "text";
	String result = StringFactory.alignRight(text, margin, true);
	assertEquals("", result);
    }

    public void testAlignRight_MarginShort_NoStrip() {
	String margin = "---";
	String text = "text";
	String result = StringFactory.alignRight(text, margin, false);
	assertEquals("text", result);
    }

    public void testAlignRight_MarginShort_Strip() {
	String margin = "---";
	String text = "text";
	String result = StringFactory.alignRight(text, margin, true);
	assertEquals("ext", result);
    }

    public void testResolveVars_SystemProperties() throws ParseException {
	String value = "${user.home}";
	String result = StringFactory
		.resolveVars(value, System.getProperties());
	assertEquals(System.getProperty("user.home"), result);
    }

    public void testResolveVars_Easy() throws ParseException {
	String value = "number=${number} null='${null}'";
	Properties props = new Properties();
	props.put("number", "4");
	String result = StringFactory.resolveVars(value, props);
	assertEquals("number=4 null='$?{null}'", result);
    }

    public void testResolveVars_Tricky() throws ParseException {
	String value = "refOne=${refOne}";
	Properties props = new Properties();
	props.put("refOne", "${refTwo}");
	props.put("refTwo", "hit!");
	String result = StringFactory.resolveVars(value, props);
	assertEquals("refOne=hit!", result);
    }

    public void testResolveVars_SimpleRecurse() throws ParseException {
	Properties values = new Properties();
	values.put("x", "${x}");
	String result = StringFactory.resolveVars("having some ${x}", values);
	assertEquals("having some $?{x}", result);
    }

    public void _testResolveVars_IndirectRecurse() throws ParseException {
	Properties values = new Properties();
	values.put("x", "${y}");
	values.put("y", "${z}");
	values.put("z", "${x}");
	String result = StringFactory.resolveVars("having some ${x}", values);
	assertEquals("having some $!{x}", result);
    }

    public void testTokenizeToArrayStringInt() {

	// defaults and shortcuts
	assertEquals(0, StringFactory.tokenizeToArray(null, 25).length);
	assertEquals(0, StringFactory.tokenizeToArray("", 25).length);
	try {
	    StringFactory.tokenizeToArray("asdfkjasdfkj", -25);
	    fail("illegal argument not catched");
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	// precisely one token
	String[] result = StringFactory.tokenizeToArray("asdfkjasdfkj", 25);
	assertEquals(1, result.length);
	assertEquals("asdfkjasdfkj", result[0]);

	// multiple tokens, exact length
	result = StringFactory.tokenizeToArray("asdfkjasdfkj", 3);
	assertEquals(4, result.length);
	assertEquals("asd", result[0]);
	assertEquals("fkj", result[1]);
	assertEquals("asd", result[2]);
	assertEquals("fkj", result[3]);

	// multiple tokens, misfit length 1 over
	result = StringFactory.tokenizeToArray("abcdefghij", 3);
	assertEquals(4, result.length);
	assertEquals("abc", result[0]);
	assertEquals("def", result[1]);
	assertEquals("ghi", result[2]);
	assertEquals("j", result[3]);

	// multiple tokens, misfit length 2 over
	result = StringFactory.tokenizeToArray("abcdefghijk", 3);
	assertEquals(4, result.length);
	assertEquals("abc", result[0]);
	assertEquals("def", result[1]);
	assertEquals("ghi", result[2]);
	assertEquals("jk", result[3]);

    }

    public void testPermutate_invalidArguments() {
	try {
	    StringFactory.permutate(new String[] {}, 5, null, true);
	    fail("empty terminal array accepted");
	} catch (IllegalArgumentException iae) {
	    // w^5
	}

	try {
	    StringFactory.permutate(null, 5, null, true);
	    fail("null terminal array accepted");
	} catch (IllegalArgumentException iae) {
	    // w^5
	}

	try {
	    StringFactory.permutate(new String[] { "5", "p", "z" }, 0, null,
		    true);
	    fail("invalid repetition length accepted");
	} catch (IllegalArgumentException iae) {
	    // w^5
	}

    }

    public void testPermutate_easyOnes() {

	// once through
	String[] result = StringFactory.permutate(new String[] { "a", "b" }, 1,
		null, true);
	assertNotNull(result);
	assertTrue(result.length == 2);
	assertEquals("a", result[0]);
	assertEquals("b", result[1]);

	// a simple permutation
	result = StringFactory.permutate(new String[] { "a", "b" }, 2, null,
		true);
	assertNotNull(result);
	assertTrue(result.length == 4);
	assertEquals("aa", result[0]);
	assertEquals("ab", result[1]);
	assertEquals("ba", result[2]);
	assertEquals("bb", result[3]);

	// a more complex permutation, terminal count > length
	result = StringFactory.permutate(new String[] { "a", "b", "c" }, 2,
		null, true);
	assertNotNull(result);
	assertTrue(result.length == 9);
	assertEquals("aa", result[0]);
	assertEquals("ab", result[1]);
	assertEquals("ac", result[2]);
	assertEquals("ba", result[3]);
	assertEquals("bb", result[4]);
	assertEquals("bc", result[5]);
	assertEquals("ca", result[6]);
	assertEquals("cb", result[7]);
	assertEquals("cc", result[8]);

	// a more complex permutation, terminal count < length
	result = StringFactory.permutate(new String[] { "0", "1" }, 3, null,
		true);
	assertNotNull(result);
	assertTrue(result.length == 8);
	assertEquals("000", result[0]);
	assertEquals("001", result[1]);
	assertEquals("010", result[2]);
	assertEquals("011", result[3]);
	assertEquals("100", result[4]);
	assertEquals("101", result[5]);
	assertEquals("110", result[6]);
	assertEquals("111", result[7]);
    }

    // --------------------------------------------------------------
    // -- test instance methods
    // --------------------------------------------------------------

    public void testStringFactoryInt() {
	StringFactory sf = new StringFactory(27);
	assertEquals(0, sf._count);
	assertEquals(27, sf._data.length);
    }

    public void testStringFactoryStringInt() {
	StringFactory sf = new StringFactory("hello", 5);
	assertEquals(5, sf._count);
	assertEquals(10, sf._data.length);
	assertEquals("hello", sf.toString());
    }

    public void testStringFactoryStringInt_ShorterString() {
	StringFactory sf = new StringFactory("hello, folks", 27);
	assertEquals(12, sf._count);
	assertEquals(27, sf._data.length);
	assertEquals("hello, folks", sf.toString());
    }

    public void testStringFactoryStringInt_LongerString() {
	StringFactory sf = new StringFactory("hello, folks", 3);
	assertEquals(12, sf._count);
	assertEquals(12, sf._data.length);
	assertEquals("hello, folks", sf.toString());
    }

    public void testStringFactoryString() {
	StringFactory sf = new StringFactory("ya");
	assertEquals(2, sf._count);
	assertEquals("ya", sf.toString());
    }

    public void testAppendString() {
	StringFactory sf = new StringFactory(5);
	sf.append("Dies ist ein String");
	assertEquals(19, sf._count);
	assertEquals('D', sf._data[0]);
	assertEquals('i', sf._data[5]);
	assertEquals('g', sf._data[18]);
    }

    public void testAppendString_Null() {
	StringFactory sf = new StringFactory(5);
	sf.append((String) null);
	assertEquals(4, sf._count);
	assertEquals('n', sf._data[0]);
	assertEquals('u', sf._data[1]);
	assertEquals('l', sf._data[2]);
	assertEquals('l', sf._data[3]);
    }

    public void testAppendObject() {
	StringFactory sf = new StringFactory(5);
	sf.append(new Integer(54));
	assertEquals(2, sf._count);
	assertEquals('5', sf._data[0]);
	assertEquals('4', sf._data[1]);
    }

    public void testAppendObject_Null() {
	StringFactory sf = new StringFactory(5);
	sf.append((Object) null);
	assertEquals(4, sf._count);
	assertEquals('n', sf._data[0]);
	assertEquals('u', sf._data[1]);
	assertEquals('l', sf._data[2]);
	assertEquals('l', sf._data[3]);
    }

    public void testToString() {
	StringFactory sf = new StringFactory(5);
	sf.append("Dies ist ein String").append("Dies ist ein String");
	assertEquals("Dies ist ein StringDies ist ein String", sf.toString());
    }

    public void testToString_Empty() {
	StringFactory sf = new StringFactory(5);
	assertEquals("", sf.toString());
    }

    public void testCapacityHandling() {
	StringFactory sf = new StringFactory(20);
	sf.append("Dies ist ein String ");
	assertTrue(sf.capacity() == 20);
	assertTrue(sf.capacity() == sf._data.length);
	sf.append("Hallo!");
	assertTrue(sf.capacity() == 30);
	assertTrue(sf.capacity() == sf._data.length);

	sf.ensureCapacity(100);
	assertEquals(100, sf._data.length);
	sf.ensureCapacity(50);
	assertEquals(100, sf._data.length);
    }

    public void testReverseIntInt() {
	StringFactory sf = new StringFactory(32);
	sf.append("Hallo, Welt!");
	sf.reverse(7, 10);
	assertEquals("Hallo, tleW!", sf.toString());
    }

    public void testReverse() {
	StringFactory sf = new StringFactory(20);
	sf.append("hello, dude");
	sf.reverse();
	assertEquals("edud ,olleh", sf.toString());
    }

    public void testAppendFileString() throws IOException {

	StringFactory sf = new StringFactory(1000);
	sf.append(new File("src/test/resources/StringFactoryTest.txt"), "..");
	assertEquals("Zeile 1..Zeile 2..Zeile 3..", sf.toString());
    }

    public void testIndexOf() {
	StringFactory sf = new StringFactory("Dies ist ein Test-String");

	// Ganz vorne
	assertEquals(0, sf.indexOf("Dies", 0));

	// in der mitte
	assertEquals(9, sf.indexOf("ein", 0));
	assertEquals(9, sf.indexOf("ein", 9));
	// Am Ende
	assertEquals(13, sf.indexOf("Test-String", 0));
	assertEquals(13, sf.indexOf("Test-String", 13));

    }

    public void testIndexOf_Shortcuts() {
	StringFactory sf = new StringFactory("Dies ist ein Test-String");

	// Short-Cut : firstIndex zu weit hinten
	assertEquals(-1, sf.indexOf("Test-String", 14));

	// Short-Cut : zu langer Text
	assertEquals(-1, sf.indexOf(
		"dfjghkjghdlgjhdsdfjhgksdgkhdsfglgjhdfgasdkfhg", 0));

    }

    public void testIndexOf_Negative() {

	StringFactory sf = new StringFactory("Dies ist ein Test-String");
	try {
	    sf.indexOf("Dies", -1);
	    fail();
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	try {
	    sf.indexOf("Dies", 30);
	    fail();
	} catch (IllegalArgumentException e) {
	    // w^5
	}

	try {
	    sf.indexOf(null, 0);
	    fail();
	} catch (IllegalArgumentException e) {
	    // w^5
	}
    }

    public void testResolveVarsProperties() throws ParseException {
	// Bezieht sich auf die "instanz"-Variante der Methode.
	StringFactory sf = new StringFactory(
		"This is a ${name} with ${content}");
	Properties p = new Properties();
	p.put("name", "house");
	p.put("content", "people");
	sf.resolveVars(p);
	assertEquals("This is a house with people", sf.toString());
    }

    public void testReplaceIntString() {
	StringFactory sf = new StringFactory("This is one  world");

	// check in-the-middle
	sf.replace(8, "some");
	assertEquals(18, sf._count);
	assertEquals("This is some world", sf.toString());

	// check past-the -end
	sf.replace(13, "handkerchief");
	assertEquals(25, sf._count);
	assertEquals("This is some handkerchief", sf.toString());

	// check from start
	sf.replace(0, "Them");
	assertEquals("Them is some handkerchief", sf.toString());
    }

    public void testReplaceIntString_Negative() {
	StringFactory sf = new StringFactory("This is one  world");

	// beyond
	try {
	    sf.replace(18, "somewhere");
	} catch (IllegalArgumentException e) {
	    assertTrue(e.getMessage().indexOf("> 17") > -1);
	}

	// below
	try {
	    sf.replace(-1, "somewhere");
	} catch (IllegalArgumentException e) {
	    assertTrue(e.getMessage().indexOf("< 0") > -1);
	}

    }

    public void testInsertStringIntInt() {
	StringFactory sf = new StringFactory("");
	assertEquals("DemoDemo", sf.insert("Demo", 0, 2).toString());
	assertEquals("DemoDemoStringString", sf.insert("String", 8, 2)
		.toString());
	assertEquals("DemoBlahBlahDemoStringString", sf.insert("Blah", 4, 2)
		.toString());

    }

    public void testInsertCharIntInt() {
	StringFactory sf = new StringFactory("");
	assertEquals("---", sf.insert('-', 0, 3).toString());
	assertEquals("+++---", sf.insert('+', 0, 3).toString());
	assertEquals("+++---!!!", sf.insert('!', 6, 3).toString());
	assertEquals("+++///---!!!", sf.insert('/', 3, 3).toString());
    }

    public void testAppendMulti() {
	StringFactory sf = new StringFactory(5);
	assertEquals(".....", sf.appendMulti('.', 5).toString());
	assertEquals(".....", sf.appendMulti('.', 0).toString());
	assertEquals(".....---", sf.appendMulti('-', 3).toString());
    }

    public void testRemoveIntInt() {
	StringFactory sf = new StringFactory("Test-String");

	// shortcuts
	assertEquals("Test-String", sf.remove(4, 0).toString());

	// Eckwert-tests
	assertEquals("est-String", sf.remove(0, 1).toString());
	assertEquals("estString", sf.remove(3, 1).toString());
	assertEquals("estStrin", sf.remove(8, 1).toString());

	// Mehr als hintenraus da
	assertEquals("est", sf.remove(4, 20).toString());

	// genau soviel wie da
	assertEquals("", sf.remove(0, 3).toString());

	// genau die kapazit�t voll, genau diese entfernen
	sf.appendMulti('-', sf.capacity());
	assertEquals(sf._count, sf._data.length);
	sf.remove(0, sf._data.length);
	assertEquals(0, sf._count);

	// Mehr als �berhaupt da
	sf.append("Test-String");
	assertEquals("", sf.remove(0, 20).toString());

    }

    public void testToCharArray_Lenient() {
	char[] result = StringFactory.toCharArray(new String[] { "one", "two",
		"thres" }, 4, true);
	assertEquals('e', result[0]);
	assertEquals('o', result[1]);
	assertEquals('s', result[2]);
    }

    public void testToCharArray_notLenient() {
	char[] result = StringFactory.toCharArray(new String[] { "one", "two",
		"three" }, 0, false);
	assertEquals('o', result[0]);
	assertEquals('t', result[1]);
	assertEquals('t', result[2]);
    }

    public void testToCharArray_notLenient_Failure() {
	try {
	    char[] result = StringFactory.toCharArray(new String[] { "three",
		    "two", "one" }, 3, false);
	    fail();
	} catch (IllegalArgumentException e) {
	    // w^5
	}
    }

}
