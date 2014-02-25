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
	 * <pre>
	 * FULL_OPEN
	 * 
	 * a-b
	 * |/
	 * c
	 * 
	 * CLOSED1
	 * 
	 * a-B
	 * |/
	 * c
	 * 
	 * CLOSED2
	 * 
	 * A-B
	 * |/
	 * c
	 * 
	 * FULL_CLOSED
	 * 
	 * A-B
	 * |/
	 * C
	 * 
	 * </pre>
	 * 
	 * @author us00852
	 * 
	 */
	public enum Type {
		FULL_OPEN, CLOSED1, CLOSED2, FULL_CLOSED
	}

	/**
	 * 
	 */
	private static final double K = Math.sqrt(3.0) / 2.0;
	private static final Domain[] DOMAINS = { new Domain() {

		@Override
		public Boolean apply(final Point3d p) {
			final double w = p.getZ();
			final double u = p.getX() / w;
			final double v = p.getY() / w;
			return u > -1 && v > -1 && u + v < 0;
		}
	}, new Domain() {

		@Override
		public Boolean apply(final Point3d p) {
			final double w = p.getZ();
			final double u = p.getX() / w;
			final double v = p.getY() / w;
			return (u >= -1 && v >= -1 && u + v <= 0) && !(u == -1 && v == -1)
					&& !(u == -1 && v == 1);
		}
	}, new Domain() {

		@Override
		public Boolean apply(final Point3d p) {
			final double w = p.getZ();
			final double u = p.getX() / w;
			final double v = p.getY() / w;
			return (u >= -1 && v >= -1 && u + v <= 0) && !(u == -1 && v == 1);
		}
	}, new Domain() {

		@Override
		public Boolean apply(final Point3d p) {
			final double w = p.getZ();
			final double u = p.getX() / w;
			final double v = p.getY() / w;
			return u >= -1 && v >= -1 && u + v <= 0;
		}
	} };

	/**
	 * 
	 * <pre>
	 *     |
	 *   A---B
	 *   | |/
	 * --+-+-----> u
	 *   |/|
	 *   C |
	 *     |
	 *   v V
	 *  
	 * 
	 *                \|
	 * (u=-1,v=-1) A---*---B (u=1,v=-1)      
	 *              \  |\ /
	 *            ---\-+-*----------------> u=x
	 *                \|/ \ 
	 *      (u=-1,v=1) C   \
	 *                 |    \
	 *               z V   v V 
	 *                   
	 * x = u + v/2 + 1/2
	 * y = m u + p v + q
	 * z = v * sqrt(3)/2
	 * 
	 * m = (B - A) / 2
	 * p = (C - A) / 2
	 * q = (B + C) / 2
	 * </pre>
	 * 
	 * @param type
	 * @param d
	 * @param a
	 * @param b
	 * @param c
	 * 
	 * @return
	 */
	public static Surface create(final Type type, final Matrix3d d,
			final double a, final double b, final double c) {
		return new TriangularSurface(d, new Matrix3d(1, 0.5, 0.5, (b - a) / 2,
				(c - a) / 2, (b + c) / 2, 0, K, 0), DOMAINS[type.ordinal()]);
	}

	/**
	 * 
	 * @param type
	 * @param d
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public static Surface createHeight(final Type type, final Matrix3d d,
			final double a, final double b, final double c) {
		return new TriangularSurface(d, new Matrix3d(0, 0, 0, (b - a) / 2,
				(c - a) / 2, (b + c) / 2, 0, 0, 0), DOMAINS[type.ordinal()]);
	}

	private final Matrix3d domain;
	private final Matrix3d matrix;
	private final Domain domainFunc;

	/**
	 * @param domain
	 * @param matrix
	 */
	private TriangularSurface(final Matrix3d domain, final Matrix3d matrix,
			final Domain domainFunc) {
		this.domain = domain;
		this.matrix = matrix;
		this.domainFunc = domainFunc;
	}

	/**
	 * @see org.mmarini.fp.Functor1#apply(java.lang.Object)
	 */
	@Override
	public Point3d apply(final Point2D p) {
		final Point3d p1 = new Point3d(p.getX(), p.getY(), 1);
		domain.transform(p1);
		if (domainFunc.apply(p1)) {
			matrix.transform(p1);
			return p1;
		} else
			return null;
	}
}
