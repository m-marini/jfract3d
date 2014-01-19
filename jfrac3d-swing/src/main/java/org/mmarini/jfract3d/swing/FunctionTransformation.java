/**
 * 
 */
package org.mmarini.jfract3d.swing;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * @author US00852
 */
public class FunctionTransformation {
	private static final FunctionTransformation IDENTITY = new FunctionTransformation(
			new AffineTransform(), 1, 0);

	/**
	 * 
	 * @param sx
	 * @param sy
	 * @param sz
	 * @param theta
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return
	 */
	public static FunctionTransformation create(final double sx,
			final double sy, final double sz, final double theta,
			final double dx, final double dy, final double dz) {
		return FunctionTransformation.createScale(sx, sy, sz).rotate(theta)
				.translate(dx, dy, dz);
	}

	/**
	 * 
	 * @return
	 */
	public static FunctionTransformation identity() {
		return IDENTITY;
	}

	/**
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return
	 */
	public static FunctionTransformation translation(final double dx,
			final double dy, final double dz) {
		final AffineTransform t = new AffineTransform();
		t.translate(dx, dy);
		return new FunctionTransformation(t, 1, dz);
	}

	/**
	 * 
	 * @param theta
	 * @return
	 */
	public static FunctionTransformation rotation(final double theta) {
		final AffineTransform t = new AffineTransform();
		t.rotate(theta);
		return new FunctionTransformation(t, 1, 0);
	}

	/**
	 * 
	 * @param sx
	 * @param sy
	 * @param sz
	 * @return
	 */
	public static FunctionTransformation createScale(final double sx,
			final double sy, final double sz) {
		final AffineTransform t = new AffineTransform();
		t.scale(sx, sy);
		return new FunctionTransformation(t, sz, 0);
	}

	/**
	 * 
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
	public static FunctionTransformation create(final double t00,
			final double t01, final double t02, final double t10,
			final double t11, final double t12, final double zz, final double z0) {
		return new FunctionTransformation(new AffineTransform(t00, t10, t01,
				t11, t02, t12), zz, z0);
	}

	private final AffineTransform tr;
	private final double zz;
	private final double z0;

	/**
	 * @param tr
	 * @param zz
	 * @param z0
	 */
	public FunctionTransformation(final AffineTransform tr, final double zz,
			final double z0) {
		super();
		this.tr = tr;
		this.zz = zz;
		this.z0 = z0;
	}

	/**
	 * 
	 * @param w
	 * @return
	 */
	public FunctionTransformation apply(final double w) {
		return new FunctionTransformation(tr, zz * w, z0);
	}

	/**
	 * 
	 * @param f
	 * @return
	 */
	public Function3D apply(final Function3D f) {
		return new Function3D() {

			@Override
			public Double apply(final Double x, final Double y) {
				final Point2D p = new Point2D.Double(x, y);
				tr.transform(p, p);
				return f.apply(p.getX(), p.getY()) * zz + z0;
			}
		};
	}

	/**
	 * (x,y) Txy T'xy F T'z Tz
	 * 
	 * @param f
	 * @return
	 */
	public FunctionTransformation apply(final FunctionTransformation f) {
		final AffineTransform t = new AffineTransform(f.tr);
		t.concatenate(tr);

		final double rzz = f.zz * zz;
		final double rz0 = f.z0 * zz + z0;
		return new FunctionTransformation(t, rzz, rz0);
	}

	/**
	 * 
	 * @param sx
	 * @param sy
	 * @param sz
	 * @return
	 */
	public FunctionTransformation scale(final double sx, final double sy,
			final double sz) {
		final AffineTransform t = new AffineTransform(tr);
		t.scale(sx, sy);
		return new FunctionTransformation(t, zz * sz, z0);
	}

	/**
	 * 
	 * @param theta
	 * @return
	 */
	public FunctionTransformation rotate(final double theta) {
		final AffineTransform t = new AffineTransform(tr);
		t.rotate(theta);
		return new FunctionTransformation(t, zz, z0);
	}

	/**
	 * 
	 * @param theta
	 * @return
	 */
	public FunctionTransformation translate(final double dx, final double dy,
			final double dz) {
		final AffineTransform t = new AffineTransform(tr);
		t.translate(dx, dy);
		return new FunctionTransformation(t, zz, z0 + dz);
	}
}
