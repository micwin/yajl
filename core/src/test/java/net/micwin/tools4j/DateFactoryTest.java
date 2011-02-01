package net.micwin.tools4j;

import java.util.Date;

import junit.framework.TestCase;

public class DateFactoryTest extends TestCase {

    static final Date ZERO = new Date(0);

    static final Date ETERNITY = new Date(Long.MAX_VALUE - 5000);

    public void testBetween() {
	assertTrue(DateFactory.between(ZERO, ETERNITY, new Date()));
    }

    public void testBetween_Null() {
	assertFalse(DateFactory.between(ZERO, ETERNITY, null));
    }

    public void testBetween_FromIsNull() {
	assertTrue(DateFactory.between(null, ETERNITY, new Date()));
    }

    public void testBetween_ToIsNull() {
	assertTrue(DateFactory.between(ZERO, null, new Date()));
    }

    public void testBetween_UnBordered() {
	assertTrue(DateFactory.between(null, null, new Date()));
    }

    public void testBetween_Twisted() {
	try {
	    DateFactory.between(ETERNITY, ZERO, new Date());
	    fail();
	} catch (IllegalArgumentException e) {
	    // w^5
	}

    }
}
