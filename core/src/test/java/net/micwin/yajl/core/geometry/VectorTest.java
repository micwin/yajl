package net.micwin.yajl.core.geometry;

import java.math.BigDecimal;
import java.math.MathContext;

import junit.framework.TestCase;

public class VectorTest extends TestCase {

	public void testCreate() throws Exception {
		Vector v = new Vector(4.5, 3, 7, 2, 1);
		assertEquals(5, v.ordinates.length);
		assertEquals(new BigDecimal(4.5), v.ordinates[0]);
		assertEquals(new BigDecimal(3), v.ordinates[1]);
		assertEquals(new BigDecimal(7), v.ordinates[2]);
		assertEquals(new BigDecimal(2), v.ordinates[3]);
		assertEquals(new BigDecimal(1), v.ordinates[4]);
	}

	public void testAdd() throws Exception {
		Vector v = new Vector(1, 1.75, -1, -30, -1).add(new Vector(3.5, 1.25,
				8, 32, 2));

		assertEquals(new BigDecimal(4.5), v.ordinates[0]);
		assertEquals(3.0, v.ordinates[1].doubleValue());
		assertEquals(new BigDecimal(7), v.ordinates[2]);
		assertEquals(new BigDecimal(2), v.ordinates[3]);
		assertEquals(new BigDecimal(1), v.ordinates[4]);
	}

	public void testGetLength() throws Exception {
		Vector v = new Vector(0, 0);
		assertEquals(0.0, v.getLength().doubleValue());
		v = new Vector(1, 1);
		assertEquals(Math.sqrt(2), v.getLength().doubleValue(), 0.000000001);
		v = new Vector(4, 2);
		assertEquals(Math.sqrt(20), v.getLength().doubleValue(), 0.00000000001d);

		// double vector and analyse length
		v = v.add(v);
		assertEquals(Math.sqrt(20) * 2, v.getLength().doubleValue(),
				0.00000000001d);

	}

}
