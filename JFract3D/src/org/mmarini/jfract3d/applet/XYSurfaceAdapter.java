/*
 * XYSurfaceAdapter.java
 *
 * $Id$
 *
 * 11/set/07
 *
 * Copyright notice
 */
package org.mmarini.jfract3d.applet;

/**
 * @author marco.marini@mmarini.org
 * @version $Id$
 * 
 */
public class XYSurfaceAdapter extends AbstractXYSurface {
    private XYSurface function;

    /**
         * 
         */
    public XYSurfaceAdapter() {
    }

    /**
         * @param function
         */
    public XYSurfaceAdapter(XYSurface function) {
	this.function = function;
    }

    /**
         * @return the function
         */
    XYSurface getFunction() {
	return function;
    }

    /**
         * @param function
         *                the function to set
         */
    public void setFunction(XYSurface function) {
	this.function = function;
    }

    /**
         * @see org.mmarini.jfract3d.applet.SurfaceFunction#calculateZ(double,
         *      double)
         */
    public double calculateZ(double u, double v) {
	double x = calculateX(u, v);
	double y = calculateY(u, v);
	return getFunction().calculateZ(x, y);
    }

}
