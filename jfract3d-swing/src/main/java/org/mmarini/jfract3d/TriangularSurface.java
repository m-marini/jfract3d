/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.geom.Point2D;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

/**
 * @author US00852
 * 
 */
public class TriangularSurface implements Surface {
	/**
	 * 
	 */
	private static final double K = Math.sqrt(3.0) / 2.0;

	/**
	 * 
	 * <pre>
	 * (u=-1,v=-1)   (u=1,v=-1)
	 *           a---b
	 *            \ /
	 *             c
	 *        (u=-1,v=1)
	 * 
	 * x = u + v/2 + 1/2
	 * y = m u + p v + q
	 * z = v * sqrt(3)/2
	 * 
	 * m = (b - a) / 2
	 * p = (c - a) / 2
	 * q = (b + c) / 2
	 * </pre>
	 * 
	 * @param d
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static Surface create(final Matrix3d d, final double a,
			final double b, final double c) {
		return new TriangularSurface(d, new Matrix3d(1, 0.5, 0.5, (b - a) / 2,
				(c - a) / 2, (b + c) / 2, 0, K, 0));
	}

	/**
	 * 
	 * @param d
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static Surface createHeight(final Matrix3d d, final double a,
			final double b, final double c) {
		return new TriangularSurface(d, new Matrix3d(0, 0, 0, (b - a) / 2,
				(c - a) / 2, (b + c) / 2, 0, 0, 0));
	}

	private final Matrix3d domain;

	private final Matrix3d matrix;

	/**
	 * @param domain
	 * @param matrix
	 */
	private TriangularSurface(final Matrix3d domain, final Matrix3d matrix) {
		this.domain = domain;
		this.matrix = matrix;
	}

	/**
	 * @see org.mmarini.fp.Functor1#apply(java.lang.Object)
	 */
	@Override
	public Point3d apply(final Point2D p) {
		final Point3d p1 = new Point3d(p.getX(), p.getY(), 1);
		domain.transform(p1);
		final double w = p1.getZ();
		final double u = p1.getX() / w;
		final double v = p1.getY() / w;
		if (contains(u, v)) {
			matrix.transform(p1);
			return p1;
		} else
			return null;
	}

	/**
	 * @param u
	 * @param v
	 * @return
	 */
	private boolean contains(final double u, final double v) {
		return u >= -1.0 && v >= -1.0 && u + v < 0.0;
	}
}
