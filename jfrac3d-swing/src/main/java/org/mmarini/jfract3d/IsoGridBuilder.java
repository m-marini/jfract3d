/**
 * 
 */
package org.mmarini.jfract3d;

import javax.vecmath.Point3d;

/**
 * @author US00852
 * 
 */
public class IsoGridBuilder implements GridBuilder {

	private final double xMin;
	private final double zMin;
	private final double xDistance;
	private final double zDistance;
	private final Function3D f;

	/**
	 * @param xDistance
	 * @param zDistance
	 * @param xMin
	 * @param zMin
	 * @param f
	 */
	public IsoGridBuilder(final double xDistance, final double zDistance,
			final double xMin, final double zMin, final Function3D f) {
		this.xMin = xMin;
		this.zMin = zMin;
		this.xDistance = xDistance;
		this.zDistance = zDistance;
		this.f = f;
	}

	/**
	 * @see org.mmarini.jfract3d.Functor2#apply(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Point3d apply(final Integer i, final Integer j) {
		final double x = j * xDistance + xMin
				+ ((i % 2) != 0 ? xDistance * 0.2 : 0);
		final double z = i * zDistance + zMin;
		return new Point3d(x, f.apply(x, z), z);
	}

}
