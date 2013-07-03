/*
 * AbstractXYSurface.java
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
public abstract class AbstractXYSurface implements SurfaceFunction {

    /**
         * @see org.mmarini.jfract3d.applet.SurfaceFunction#calculateX(double,
         *      double)
         */
    public double calculateX(double u, double v) {
	return u;
    }

    /**
         * @see org.mmarini.jfract3d.applet.SurfaceFunction#calculateY(double,
         *      double)
         */
    public double calculateY(double u, double v) {
	return v;
    }
}
