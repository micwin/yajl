package net.micwin.yajl.core.math;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * A mathematical helper class.
 * 
 * @author MicWin
 * 
 */
public class YajlMath {

	public static final BigDecimal TWO = BigDecimal.valueOf(2);
	public static final BigDecimal THREE = BigDecimal.valueOf(3);

	/**
	 * Calculates the squsre root of a
	 * 
	 * @param decimal
	 * @param iterations
	 * @return
	 */
	public static BigDecimal sqrt(BigDecimal decimal, int iterations) {

		if (decimal.doubleValue() <= 1) {
			return new BigDecimal(Math.sqrt(decimal.doubleValue()));
		}

		MathContext mc = new MathContext(10);

		// Specify the starting value in the search for the cube root.
		BigDecimal x = BigDecimal.ONE;

		for (int i = 0; i < iterations; i++) {
			BigDecimal tmp = decimal.divide(x, mc);
			tmp = tmp.add(x, mc);
			x = tmp.divide(TWO, mc);
		}

		return x;
	}
}
