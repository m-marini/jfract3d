/**
 * 
 */
package org.mmarini.jfract3d;

import javax.media.j3d.Geometry;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class IsoGeometryBuilder implements GeometryBuilder {
	private static final double Z_FACTOR = Math.sqrt(3) / 2;

	/**
	 * 
	 * @param xCount
	 * @param xMin
	 * @param xMax
	 * @param zMin
	 * @param zMax
	 * @param depth
	 * @param transSet
	 * @param seedFunction
	 * @param randomizer
	 * @return
	 */
	public static IsoGeometryBuilder create(final int xCount,
			final double xMin, final double xMax, final double zMin,
			final double zMax, final int depth, final FunctionFactory factory,
			final FractalTransform... transSet) {
		final double xDistance = (xMax - xMin) / (xCount - 1);
		final double zDistance = xDistance * Z_FACTOR;
		final int zCount = (int) Math.ceil((zMax - zMin) / zDistance + 1);
		return new IsoGeometryBuilder(xCount, zCount, xDistance, zDistance,
				xMin, zMin, depth, factory, transSet);
	}

	/**
	 * 
	 * @param xCount
	 * @param zCount
	 * @param xDistance
	 * @param zDistance
	 * @param xMin
	 * @param zMin
	 * @param depth
	 * @param transSet
	 * @param seedFunction
	 * @param randomizer
	 * @return
	 */
	public static IsoGeometryBuilder create(final int xCount, final int zCount,
			final double xDistance, final double zDistance, final double xMin,
			final double zMin, final int depth, final FunctionFactory factory,
			final FractalTransform... transSet) {
		return new IsoGeometryBuilder(xCount, zCount, xDistance, zDistance,
				xMin, zMin, depth, factory, transSet);
	}

	private final int xCount;
	private final int zCount;
	private final double xDistance;
	private final double zDistance;
	private final double xMin;
	private final double zMin;
	private final int depth;
	private final FunctionFactory factory;

	private final FractalTransform[] transSet;

	/**
	 * @param xCount
	 * @param zCount
	 * @param xDistance
	 * @param zDistance
	 * @param xMin
	 * @param zMin
	 * @param depth
	 * @param factory
	 * @param transSet
	 */
	private IsoGeometryBuilder(final int xCount, final int zCount,
			final double xDistance, final double zDistance, final double xMin,
			final double zMin, final int depth, final FunctionFactory factory,
			final FractalTransform[] transSet) {
		super();
		this.xCount = xCount;
		this.zCount = zCount;
		this.xDistance = xDistance;
		this.zDistance = zDistance;
		this.xMin = xMin;
		this.zMin = zMin;
		this.depth = depth;
		this.factory = factory;
		this.transSet = transSet;
	}

	/**
	 * 
	 */
	@Override
	public Geometry build() {
		final Point3d[][] coords = createSubjectCoords();

		// Compute triangle array
		final int n = coords.length;
		final int m = coords[0].length;
		final Point3d[] q = new Point3d[(n - 1) * (m - 1) * 2 * 3];
		int idx = 0;
		for (int i = 0; i < n - 1; i += 2) {
			for (int j = 0; j < m - 1; ++j) {
				q[idx++] = coords[i][j];
				q[idx++] = coords[i][j + 1];
				q[idx++] = coords[i + 1][j];

				q[idx++] = coords[i + 1][j];
				q[idx++] = coords[i][j + 1];
				q[idx++] = coords[i + 1][j + 1];
			}
		}

		for (int i = 1; i < n - 1; i += 2) {
			for (int j = 0; j < m - 1; ++j) {
				q[idx++] = coords[i][j];
				q[idx++] = coords[i + 1][j + 1];
				q[idx++] = coords[i + 1][j];

				q[idx++] = coords[i][j];
				q[idx++] = coords[i][j + 1];
				q[idx++] = coords[i + 1][j + 1];
			}
		}

		// Compute geometry
		final GeometryInfo info = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
		info.setCoordinates(q);
		new NormalGenerator().generateNormals(info);
		new Stripifier().stripify(info);
		return info.getGeometryArray();
	}

	/**
	 * @return
	 */
	private Point3d[][] createSubjectCoords() {
		final Point3d[][] c = new Point3d[zCount][xCount];
		// final Randomizer r = new LinearRandomizer(1, 1);
		final GridBuilder b = new IsoGridBuilder(xDistance, zDistance, xMin,
				zMin,
				new FractalFunctionBuilder(factory, depth, transSet).create());
		for (int i = 0; i < zCount; ++i)
			for (int j = 0; j < xCount; ++j)
				c[i][j] = b.apply(zCount - 1 - i, j);
		return c;
	}
}
