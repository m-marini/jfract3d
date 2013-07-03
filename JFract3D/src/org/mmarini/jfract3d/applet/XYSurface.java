package org.mmarini.jfract3d.applet;

/**
 * 
 * @author marco.marini@mmarini.org
 * @version $Id$
 * 
 */
public interface XYSurface {
    /**
     * 
     * @param x
     * @param y
     * @return
     */
    public abstract double calculateZ(double x, double y);
}
