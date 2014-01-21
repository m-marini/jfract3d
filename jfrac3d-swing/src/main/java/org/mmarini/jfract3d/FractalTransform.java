/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * @author US00852
 */
public class FractalTransform {
	private static final FractalTransform IDENTITY = new FractalTransform(
			new AffineTransform(), 1, 0);

	/**
	 * @param f
	 * @param sx
	 * @param sy
	 * @param sz
	 * @param theta
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return
	 */
	public static FractalTransform create(final double sx, final double sy,
			final double sz, final double theta, final double dx,
			final double dy, final double dz) {
		return FractalTransform.createScale(sx, sy, sz).rotate(theta)
				.translate(dx, dy, dz);
	}

	/**
	 * 
	 * @param f
	 * @param t00
	 * @param t01
	 * @param t02
	 * @param t10
	 * @param t11
	 * @param t12
	 * @param zz
	 * @param z0
	 * @return
	 */
	public static FractalTransform create(final double t00, final double t01,
			final double t02, final double t10, final double t11,
			final double t12, final double zz, final double z0) {
		return new FractalTransform(new AffineTransform(t00, t10, t01, t11,
				t02, t12), zz, z0);
	}

	/**
	 * 
	 * @param f
	 * @param sx
	 * @param sy
	 * @param sz
	 * @return
	 */
	public static FractalTransform createScale(final double sx,
			final double sy, final double sz) {
		final AffineTransform t = new AffineTransform();
		t.scale(sx, sy);
		return new FractalTransform(t, sz, 0);
	}

	/**
	 * 
	 * @return
	 */
	public static FractalTransform identity() {
		return IDENTITY;
	}

	/**
	 * 
	 * @param f
	 * @param theta
	 * @return
	 */
	public static FractalTransform rotation(final double theta) {
		final AffineTransform t = new AffineTransform();
		t.rotate(theta);
		return new FractalTransform(t, 1, 0);
	}

	/**
	 * 
	 * @param f
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return
	 */
	public static FractalTransform translation(final double dx,
			final double dy, final double dz) {
		final AffineTransform t = new AffineTransform();
		t.translate(dx, dy);
		return new FractalTransform(t, 1, dz);
	}

	private final AffineTransform tr;
	private final double zz;
	private final double z0;

	/**
	 * @param f
	 * @param tr
	 * @param zz
	 * @param z0
	 */
	public FractalTransform(final AffineTransform tr, final double zz,
			final double z0) {
		this.tr = tr;
		this.zz = zz;
		this.z0 = z0;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param f
	 * @return
	 */
	public double apply(final double x, final double y, final Function3D f) {
		final Point2D p = new Point2D.Double(x, y);
		tr.transform(p, p);
		return f.apply(p.getX(), p.getY()) * zz + z0;
	}

	/**
	 * (x,y) Txy T'xy F T'z Tz
	 * 
	 * @param f
	 * @return
	 */
	public FractalTransform apply(final FractalTransform f) {
		final AffineTransform t = new AffineTransform(f.tr);
		t.concatenate(tr);

		final double rzz = f.zz * zz;
		final double rz0 = f.z0 * zz + z0;
		return new FractalTransform(t, rzz, rz0);
	}

	/**
	 * 
	 * @param theta
	 * @return
	 */
	public FractalTransform rotate(final double theta) {
		final AffineTransform t = new AffineTransform(tr);
		t.rotate(theta);
		return new FractalTransform(t, zz, z0);
	}

	/**
	 * 
	 * @param sx
	 * @param sy
	 * @param sz
	 * @return
	 */
	public FractalTransform scale(final double sx, final double sy,
			final double sz) {
		final AffineTransform t = new AffineTransform(tr);
		t.scale(sx, sy);
		return new FractalTransform(t, zz * sz, z0);
	}

	/**
	 * 
	 * @param theta
	 * @return
	 */
	public FractalTransform translate(final double dx, final double dy,
			final double dz) {
		final AffineTransform t = new AffineTransform(tr);
		t.translate(dx, dy);
		return new FractalTransform(t, zz, z0 + dz);
	}
}
