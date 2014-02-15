/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.geom.Point2D;

import javax.media.j3d.Geometry;
import javax.vecmath.Point3d;

import org.mmarini.fp.FPArrayList;
import org.mmarini.fp.FPList;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class SurfaceGeometryBuilder implements GeometryBuilder {

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
	public static SurfaceGeometryBuilder createByRange(final int uCount,
			final int vCount, final double uMin, final double uMax,
			final double vMin, final double vMax, final Surface s) {
		return new SurfaceGeometryBuilder(uCount, uMin, (uMax - uMin)
				/ (uCount - 1), vCount, vMin, (vMax - vMin) / (vCount - 1), s);
	}

	/**
	 * @param uCount
	 * @param uMin
	 * @param uStep
	 * @param vCount
	 * @param vMin
	 * @param vStep
	 * @param s
	 */
	private SurfaceGeometryBuilder(final int uCount, final double uMin,
			final double uStep, final int vCount, final double vMin,
			final double vStep, final Surface s) {
		this.uCount = uCount;
		this.uMin = uMin;
		this.uStep = uStep;
		this.vCount = vCount;
		this.vMin = vMin;
		this.vStep = vStep;
		this.s = s;
	}

	private final int uCount;
	private final double uMin;
	private final double uStep;
	private final int vCount;
	private final double vMin;
	private final double vStep;
	private final Surface s;

	/**
	 * 
	 */
	@Override
	public Geometry build() {
		final Point3d[][] coords = createSubjectCoords();

		// Compute quad array
		final FPList<Point3d> l = new FPArrayList<>();
		final int n = coords.length;
		final int m = coords[0].length;
		for (int i = 0; i < n - 1; ++i) {
			for (int j = 0; j < m - 1; ++j) {
				final FPList<Point3d> c = new FPArrayList<>(coords[i][j],
						coords[i][j + 1], coords[i + 1][j + 1],
						coords[i + 1][j]);
				if (!c.contains((Point3d) null))
					l.addAll(c);
			}
		}

		// Compute geometry
		final GeometryInfo info = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
		info.setCoordinates(l.toArray(new Point3d[0]));
		new NormalGenerator().generateNormals(info);
		new Stripifier().stripify(info);
		return info.getGeometryArray();
	}

	/**
	 * @return
	 */
	private Point3d[][] createSubjectCoords() {
		final Point3d[][] c = new Point3d[uCount][vCount];
		for (int i = 0; i < vCount; ++i)
			for (int j = 0; j < uCount; ++j)
				c[j][i] = s.apply(new Point2D.Double(j * uStep + uMin, i
						* vStep + vMin));
		return c;
	}
}
