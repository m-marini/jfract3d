/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.geom.Point2D;

import javax.vecmath.Point3d;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class Quad implements Surface {

	/**
	 * 
	 */
	public Quad() {
	}

	/**
	 * @see org.mmarini.fp.Functor1#apply(java.lang.Object)
	 */
	@Override
	public Point3d apply(final Point2D p) {
		final double x = p.getX();
		final double y = p.getY();
		return x >= -1 && x < 1 && y >= -1 && y < 1 ? new Point3d(x, 0.0, y)
				: null;
	}

}
