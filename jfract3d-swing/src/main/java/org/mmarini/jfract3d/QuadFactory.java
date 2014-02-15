/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.geom.AffineTransform;

import javax.media.j3d.Transform3D;

import org.mmarini.fp.FPArrayList;
import org.mmarini.fp.FPList;
import org.mmarini.fp.Functor1;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class QuadFactory implements TransformFactory {

	/**
	 * @param center
	 * @param north
	 * @param south
	 * @param east
	 * @param west
	 */
	public QuadFactory(final Randomizer<Double> center,
			final Randomizer<Double> north, final Randomizer<Double> south,
			final Randomizer<Double> east, final Randomizer<Double> west) {
		this.center = center;
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
	}

	private final Randomizer<Double> north;
	private final Randomizer<Double> south;
	private final Randomizer<Double> east;
	private final Randomizer<Double> west;
	private final Randomizer<Double> center;

	/**
	 * Create a list of FractalTransformers.
	 * <p>
	 * Let the corners of the quad be:
	 * 
	 * <pre>
	 * (-1, nw, +1)   (+1, ne, +1)
	 *           +-----+
	 *           |     |
	 *           |     |
	 *           +-----+
	 * (-1, sw, -1)   (+1, se, -1)
	 * </pre>
	 * 
	 * than the equation of quad is
	 * 
	 * <pre>
	 * (x,y,z) = (u, a u + b v + c, v)
	 * 
	 * a = (se - sw) / 2 = (ne - nw) / 2
	 * b = (nw - sw) / 2 = (ne - se) / 2
	 * c = (nw + se) / 2 = (ne + sw) / 2
	 * 
	 * u = -1, ..., +1
	 * v = -1, ..., +1
	 * </pre>
	 * 
	 * </p>
	 * <p>
	 * Create the transformation using an arrays of parameters, each row in the
	 * array is the parameter set of the transformation as follow:
	 * </p>
	 * <ul>
	 * <li>args[0], args[1] the translation values of affine transformation of
	 * u,v coordinates,</li>
	 * <li>args[2], args[3], args[4] the coefficients of plan function in 3D
	 * space respectivly (a, b, c)</li>
	 * </ul>
	 * <p>
	 * The transofmations are built as the map:
	 * 
	 * <pre>
	 * +-------N-------+
	 * |       |       |
	 * |(+1,-1)|(-1,-1)|
	 * |       |       |
	 * W-------O-------E
	 * |       |       |
	 * |(+1,+1)|(-1,+1)|
	 * |       |       |
	 * +-------S-------+
	 * </pre>
	 * <p>
	 * The parameters are:
	 * 
	 * <pre>
	 * +1, +1, (o-w)/2, (o-s)/2, (w+s)/2</li>
	 * +1, -1, (o-w)/2, (n-o)/2, (n+w)/2</li>
	 * -1, +1, (e-o)/2, (o-s)/2, (s+e)/2</li>
	 * -1, -1, (e-o)/2, (n-o)/2, (n+e)/2</li>
	 * </pre>
	 * 
	 * @see org.mmarini.jfract3d.TransformFactory#create()
	 */
	@Override
	public FPList<FractalTransform> create() {
		final double k = 1;
		final double o = center.next() * k;
		final double n = north.next() * k;
		final double s = south.next() * k;
		final double e = east.next() * k;
		final double w = 1;
		// final double o = center.next();
		// final double n = north.next();
		// final double s = south.next();
		// final double e = east.next();
		// final double w = west.next();

		return new FPArrayList<double[]>(new double[][] { { 1, 1, o },
				{ 1, -1, n }, { -1, 1, s }, { -1, -1, e },
				// { 1, -1, (o - w) / 2, (n - o) / 2, (n + w) / 2 },
				// { -1, 1, (e - o) / 2, (o - s) / 2, (s + e) / 2 },
				// { -1, -1, (e - o) / 2, (n - o) / 2, (n + e) / 2 } ,
		}).map(new Functor1<FractalTransform, double[]>() {

			@Override
			public FractalTransform apply(final double[] p) {
				final AffineTransform t = new AffineTransform();
				t.translate(p[0], p[1]);
				t.scale(2, 2);
				return new FractalTransform(t, new Transform3D(new double[] {
						0.0, 0.0, 0.0, 0.0, 0.0, 0.5, 0.0, p[2], 0.0, 0.0, 0.0,
						0.0, 0.0, 0.0, 0.0, 1.0 }));
			}
		});
	}

	// return new FPArrayList<double[]>(new double[][] { { 1, 1, (o - w) / 2,
	// (o - s) / 2, (w + s) / 2 },
	// // { 1, -1, (o - w) / 2, (n - o) / 2, (n + w) / 2 },
	// // { -1, 1, (e - o) / 2, (o - s) / 2, (s + e) / 2 },
	// // { -1, -1, (e - o) / 2, (n - o) / 2, (n + e) / 2 } ,
	// }).map(new Functor1<FractalTransform, double[]>() {
	//
	// @Override
	// public FractalTransform apply(final double[] p) {
	// final AffineTransform t = new AffineTransform();
	// t.translate(p[0], p[1]);
	// t.scale(2, 2);
	// return new FractalTransform(t, new Transform3D(new double[] {
	// 0.0, 0.0, 0.0, 0.0, p[2], 0.0, p[3], p[4], 0.0, 0.0,
	// 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 }));
	// }
	// });
	// }
}
