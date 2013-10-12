/*
 * TestSurface.java
 *
 * $Id$
 *
 * 10/set/07
 *
 * Copyright notice
 */
package org.mmarini.jfract3d.applet;

/**
 * @author marco.marini@mmarini.org
 * @version $Id$
 * 
 */
public class TestSurface implements XYSurface {
	/**
         * 
         */
	@Override
	public double calculateZ(double x, double y) {
		// return 0.;
		double r2 = x * x + y * y;
		double r = Math.sqrt(r2);
		return Math.exp(-r * 4.) * Math.cos(r * 20.) * 0.25;
		// return u * v;
	}

}
