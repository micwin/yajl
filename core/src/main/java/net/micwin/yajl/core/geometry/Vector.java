/**
 * 
 */
package net.micwin.yajl.core.geometry;

import java.math.BigDecimal;

import net.micwin.yajl.core.math.YajlMath;

/**
 * A Vector is a geometry pointer in n dimensions. It is immutable.
 * 
 * @author MicWin
 * 
 */
public class Vector {

	BigDecimal[] ordinates;
	private BigDecimal length;

	/**
	 * Creates a Vector with the specified ordinates as {@link Number} values.
	 * 
	 * @param ordinates
	 */
	public Vector(Number... ordinates) {
		if (ordinates == null || ordinates.length < 1) {
			throw new IllegalArgumentException(
					"argument 'ordinates' has no dimenstion or is null");
		}
		this.ordinates = new BigDecimal[ordinates.length];
		for (int i = 0; i < ordinates.length; i++) {
			this.ordinates[i] = new BigDecimal(ordinates[i].doubleValue());
		}
	}

	/**
	 * Creates a Vector with the specified ordinates as {@link BigDecimal}s.
	 * THsi does not create new BigDecimals but takes the instances passed in
	 * (but not the array).
	 * 
	 * @param ordinates
	 */
	public Vector(BigDecimal... ordinates) {
		if (ordinates == null || ordinates.length < 1) {
			throw new IllegalArgumentException(
					"argument 'ordinates' has no dimenstion or is null");
		}
		this.ordinates = new BigDecimal[ordinates.length];
		for (int i = 0; i < ordinates.length; i++) {
			this.ordinates[i] = ordinates[i];
		}
	}

	/**
	 * Adds this Vector to another one.
	 * 
	 * @param v
	 *            another Vector with the same ampount of dimensions.
	 * @return A new instance containing the sum of both Vectors.
	 */
	public Vector add(Vector v) {
		if (ordinates.length != v.ordinates.length) {
			throw new IllegalArgumentException("argument v has "
					+ v.ordinates.length + " dimensions while this Vector has "
					+ ordinates.length);
		}
		BigDecimal[] results = new BigDecimal[ordinates.length];
		for (int i = 0; i < results.length; i++) {
			results[i] = ordinates[i].add(v.ordinates[i]);
		}
		return new Vector(results);
	}

	/**
	 * Returns the cartesian length of this vector
	 * 
	 * @return
	 */
	public BigDecimal getLength() {
		if (length == null) {
			BigDecimal sum = new BigDecimal(0);
			for (int i = 0; i < ordinates.length; i++) {
				BigDecimal square = ordinates[i].multiply(ordinates[i]);
				sum = sum.add(square);
			}
			length = YajlMath.sqrt(sum, (int) ((sum.scale()+ 1) * 10));
		}
		return length;
	}
}
