/*
 * SurfaceFunction.java
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
public interface SurfaceFunction {
    /**
         * 
         * @param u
         * @param v
         * @return
         */
    public abstract double calculateX(double u, double v);

    /**
         * 
         * @param u
         * @param v
         * @return
         */
    public abstract double calculateY(double u, double v);

    /**
         * 
         * @param u
         * @param v
         * @return
         */
    public abstract double calculateZ(double u, double v);
}
