/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class FractalTransform {
	public static final FractalTransform IDENTITY = new FractalTransform(
			new AffineTransform(), new Transform3D());
	private final AffineTransform uvTrans;
	private final Transform3D xyzTrans;

	/**
	 * @param uvTrans
	 * @param xyzTrans
	 */
	public FractalTransform(final AffineTransform uvTrans,
			final Transform3D xyzTrans) {
		super();
		this.uvTrans = uvTrans;
		this.xyzTrans = xyzTrans;
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public Surface createSurface(final Surface s) {
		return new Surface() {
			@Override
			public Point3d apply(final Point2D p) {
				final Point3d p2 = s.apply(uvTrans.transform(p, null));
				if (p2 != null)
					xyzTrans.transform(p2);
				return p2;
			}
		};
	}

	/**
	 * 
	 * @param f
	 */
	public FractalTransform apply(final FractalTransform f) {
		final AffineTransform uv1 = new AffineTransform(f.uvTrans);
		uv1.concatenate(uvTrans);
		final Transform3D xyz1 = new Transform3D(xyzTrans);
		xyz1.mul(f.xyzTrans);
		return new FractalTransform(uv1, xyz1);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("FractalTransform [uvTrans=").append(uvTrans)
				.append(", xyzTrans=").append(xyzTrans).append("]");
		return builder.toString();
	}

	/**
	 * @param p
	 * @return
	 */
	public Point2D transform(final Point2D p) {
		return uvTrans.transform(p, null);
	}

	/**
	 * @param p
	 * @return
	 */
	public Point3d transform(final Point3d p) {
		final Point3d r = new Point3d(p);
		xyzTrans.transform(r);
		return r;
	}
}
