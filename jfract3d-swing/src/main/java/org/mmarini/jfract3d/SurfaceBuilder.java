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

/**
 * @author US00852
 * 
 */
public class SurfaceBuilder {
	private final int depth;
	private final Randomizer<Double> randomizer;
	private final double[][] grid;
	private static final Matrix3d ADE = new Matrix3d(2, 0, 1, 0, 2, 1, 0, 0, 1);
	private static final Matrix3d BFD = new Matrix3d(2, 0, -1, 0, 2, 1, 0, 0, 1);
	private static final Matrix3d EFC = new Matrix3d(2, 0, 1, 0, 2, -1, 0, 0, 1);
	private static final Matrix3d DFE = new Matrix3d(-2, 0, -1, 0, -2, -1, 0,
			0, 1);

	/**
	 * @param depth
	 * @param randomizer
	 */
	public SurfaceBuilder(final int depth, final Randomizer<Double> randomizer) {
		this.depth = depth;
		this.randomizer = randomizer;
		final int n = (1 << depth) + 1;
		grid = new double[n][];
		for (int i = 0; i < n; ++i)
			grid[i] = new double[i + 1];
		init();
	}

	/**
	 * 
	 * @return
	 */
	public Surface build() {
		final FPList<Surface> l = createSurfaces();
		final Matrix3d i = new Matrix3d();
		i.setIdentity();
		final Surface b = TriangularSurface.create(i, 0.0, 0.0, 0.0);
		return new Surface() {

			@Override
			public Point3d apply(final Point2D p) {
				final Point3d v = b.apply(p);

				return v != null ? l.fold(v,
						new Functor2<Point3d, Point3d, Surface>() {

							@Override
							public Point3d apply(final Point3d p1,
									final Surface s) {
								final Point3d p2 = s.apply(p);
								if (p2 != null)
									p1.add(p2);
								return p1;
							}
						}) : null;
			}

		};
	}

	/**
	 * @return
	 */
	private FPList<Surface> createSurfaces() {
		final FPArrayList<Surface> l = new FPArrayList<>();
		final double a = 0;
		final double b = 0.1;
		final double c = 0.2;
		final double d = 0.3;
		final double e = 0.4;
		final double f = 0.5;
		/**
		 * <pre>
		 * b
		 * |\
		 * d-f
		 * |\|\
		 * a-e-c
		 * 
		 *     b
		 *    / \
		 *   d---f
		 *  / \ / \
		 * a---e---c
		 * 
		 * u'=-v
		 * ade
		 * dfe
		 * bfd
		 * efc
		 * </pre>
		 */
		l.add(TriangularSurface.createHeight(ADE, a, d, e));
		// l.add(TriangularSurface.createHeight(DFE, d, f, e));
		l.add(TriangularSurface.createHeight(BFD, b, f, d));
		// l.add(TriangularSurface.createHeight(EFC, e, f, c));
		return l;
	}

	/**
	 * @return
	 */
	private FPList<Surface> createSurfaces1() {
		final FPArrayList<Surface> l = new FPArrayList<>();
		final int n = grid.length;
		final Matrix3d s = new Matrix3d();
		s.setIdentity();

		for (int k = n - 1; k > 1; k /= 2) {
			final int h = k / 2;
			for (int i = 0; i < n - 1; i += k) {
				for (int j = 0; j <= i; j += k) {
					/**
					 * <pre>
					 * b
					 * |\
					 * d-f
					 * |\|\
					 * a-e-c
					 * 
					 *     b
					 *    / \
					 *   d---f
					 *  / \ / \
					 * a---e---c
					 * 
					 * ade
					 * fed
					 * fdb
					 * fce
					 * </pre>
					 */
					final double b = grid[i][j];
					final double a = grid[i + k][j];
					final double c = grid[i + k][j + k];
					final double d = grid[i + h][j];
					final double f = grid[i + h][j + h];
					final double e = grid[i + k][j + h];

				}
			}
		}
		return l;
	}

	/**
	 * Initialize the grid as
	 * 
	 * <pre>
	 * 0
	 * 3 3
	 * 2 3 2
	 * 3 3 3 3
	 * 1 3 2 3 1
	 * 3 3 3 3 3 3
	 * 2 3 2 3 2 3 2
	 * 3 3 3 3 3 3 3 3
	 * 0 3 2 3 1 3 2 3 0
	 * </pre>
	 */
	private void init() {
		final int n = grid.length;
		double s = 1.0;
		for (int k = n - 1; k > 1; k /= 2) {
			final int h = k / 2;
			for (int i = h; i < n - 1; i += k) {
				for (int j = 0; j < i; j += k) {
					grid[i][j] = randomizer.next() * s;
					grid[i][j + h] = randomizer.next() * s;
					grid[i + h][j + h] = randomizer.next() * s;
				}
			}
			s /= 2.0;
		}
	}
}
