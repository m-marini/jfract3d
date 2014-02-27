/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.geom.Point2D;

import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;

import org.mmarini.fp.FPArrayList;
import org.mmarini.fp.FPList;
import org.mmarini.fp.Functor2;
import org.mmarini.jfract3d.TriangularSurface.Type;

/**
 * @author US00852
 * 
 */
public class SurfaceBuilder {
	private final Randomizer<Double> randomizer;
	private final double[][] grid;
	private final double scale;

	/**
	 * @param depth
	 * @param scale
	 *            TODO
	 * @param randomizer
	 */
	public SurfaceBuilder(final int depth, double scale,
			final Randomizer<Double> randomizer) {
		this.scale = scale;
		this.randomizer = randomizer;
		final int n = (1 << depth) + 1;
		grid = new double[n][];
		for (int i = 0; i < n; ++i)
			grid[i] = new double[n - i];
		init();
	}

	/**
	 * 
	 * @return
	 */
	public Surface build() {
		final Matrix3d i = new Matrix3d();
		i.setIdentity();
		final FPList<Surface> l = createSurfaces();
		l.add(TriangularSurface.create(Type.FULL_CLOSED, i, 0.0, 0.0, 0.0));
		return new Surface() {

			@Override
			public Point3d apply(final Point2D p) {
				return l.fold(null, new Functor2<Point3d, Point3d, Surface>() {

					@Override
					public Point3d apply(final Point3d p1, final Surface s) {
						final Point3d p2 = s.apply(p);
						if (p2 != null && p1 != null)
							p1.add(p2);
						return p1 != null ? p1 : p2;
					}
				});
			}

		};
	}

	/**
	 * <pre>
	 * 
	 * A---B b  b---C
	 *  \ / / \  \ /
	 *   d d---e  e
	 *     D---E
	 *      \ /
	 *       F
	 * 
	 * ABd
	 * bCe
	 * DEF
	 * edb
	 * 
	 * 0 3 2 3 1 3 2 3 0
	 * 3 3 3 3 3 3 3 3
	 * 2 3 2 3 2 3 2
	 * 3 3 3 3 3 3
	 * 1 3 2 3 1
	 * 3 3 3 3
	 * 2 3 2
	 * 3 3
	 * 0
	 * </pre>
	 * 
	 * @return
	 */
	private FPList<Surface> createSurfaces() {
		final FPList<Surface> l = new FPArrayList<>();
		final int n = grid.length;
		int s = 2;
		for (int k = (n - 1) / 2; k > 0; k /= 2) {
			for (int i = 0; i < n - 1; i += k) {
				for (int j = 0; j < n - i - 1; j += k) {
					double a = grid[i][j];
					double b = grid[i][j + k];
					double c = grid[i + k][j];

					if (i == n - k - 1)
						// DEF
						l.add(TriangularSurface
								.createHeight(Type.FULL_CLOSED, new Matrix3d(s,
										0, s - 1, 0, s, 1 - s, 0, 0, 1), a, b,
										c));
					else {
						if (j == 0)
							// ABd
							l.add(TriangularSurface.createHeight(Type.CLOSED2,
									new Matrix3d(s, 0, s - 1, 0, s, s - 1 - 2
											* i / k, 0, 0, 1), a, b, c));
						else {
							// bCe + edb
							l.add(TriangularSurface.createHeight(Type.CLOSED1,
									new Matrix3d(s, 0, s - 1 - 2 * j / k, 0, s,
											s - 1 - 2 * i / k, 0, 0, 1), a, b,
									c));
						}
						if (i + j + 2 * k < n)
							l.add(TriangularSurface.createHeight(
									Type.FULL_OPEN, new Matrix3d(-s, 0, 1 - s
											+ 2 * j / k, 0, -s, 1 - s + 2 * i
											/ k, 0, 0, 1), grid[i + k][j + k],
									c, b));
					}
				}
			}
			s *= 2;
		}
		return l;
	}

	/**
	 * Initialize the grid as
	 * 
	 * <pre>
	 * 0 3 2 3 1 3 2 3 0
	 * 3 3 3 3 3 3 3 3
	 * 2 3 2 3 2 3 2
	 * 3 3 3 3 3 3
	 * 1 3 2 3 1
	 * 3 3 3 3
	 * 2 3 2
	 * 3 3
	 * 0
	 * </pre>
	 */
	private void init() {
		final int n = grid.length;
		double s = scale;
		for (int k = n - 1; k > 1; k /= 2) {
			final int h = k / 2;
			for (int i = 0; i < n - 1; i += k) {
				for (int j = 0; j < n - i - 1; j += k) {
					grid[i][j + h] = randomizer.next() * s;
					grid[i + h][j] = randomizer.next() * s;
					grid[i + h][j + h] = randomizer.next() * s;
				}
			}
			s *= scale;
		}
	}
}
