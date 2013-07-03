/*
 * SurfaceGemoetryFactory.java
 *
 * $Id$
 *
 * 10/set/07
 *
 * Copyright notice
 */
package org.mmarini.jfract3d.applet;

import javax.media.j3d.Geometry;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;

/**
 * @author marco.marini@mmarini.org
 * @version $Id$
 * 
 */
public class SurfaceGemoetryFactory {
    private GeometryInfo info = new GeometryInfo(GeometryInfo.QUAD_ARRAY);

    private int uPointCount = 129;

    private int vPointCount = 129;

    private double uMin = -1.;

    private double uMax = 1.;

    private double vMin = -1.;

    private double vMax = 1.;

    /**
         * 
         * @param function
         * @return
         */
    public Geometry createGeometry(SurfaceFunction function) {
	Point3d[][] coords = calculateSurfaceCoordinates(function);
	Point3d[] array = calculateQuadArray(coords);
	GeometryInfo info = getInfo();
	info.setCoordinates(array);
	NormalGenerator ng = new NormalGenerator();
	ng.generateNormals(info);
	Stripifier stripifier = new Stripifier();
	stripifier.stripify(info);
	return info.getGeometryArray();
    }

    /**
         * 
         * @param coords
         * @return
         */
    private Point3d[] calculateQuadArray(Point3d[][] coords) {
	int nu = getUPointCount();
	int nv = getVPointCount();
	Point3d[] array = new Point3d[(nu - 1) * (nv - 1) * 4];
	int idx = 0;
	for (int i = 0; i < nu - 1; ++i) {
	    for (int j = 0; j < nv - 1; ++j) {
		array[idx++] = coords[i][j];
		array[idx++] = coords[i][j + 1];
		array[idx++] = coords[i + 1][j + 1];
		array[idx++] = coords[i + 1][j];
	    }
	}
	return array;
    }

    /**
         * 
         * @param function
         * @return
         */
    private Point3d[][] calculateSurfaceCoordinates(SurfaceFunction function) {
	int nu = getUPointCount();
	int nv = getVPointCount();
	double u0 = getUMin();
	double us = (getUMax() - u0) / (nu - 1);
	double v0 = getVMin();
	double vs = (getVMax() - v0) / (nv - 1);
	Point3d[][] coord = new Point3d[nu][nv];
	for (int i = 0; i < nu; ++i) {
	    double u = i * us + u0;
	    for (int j = 0; j < nv; ++j) {
		double v = j * vs + v0;
		double x = function.calculateX(u, v);
		double y = function.calculateY(u, v);
		double z = function.calculateZ(u, v);
		Point3d point3d = new Point3d(x, z, y);
		// point3d.scale(0.01);
		coord[i][j] = point3d;
	    }
	}
	return coord;
    }

    /**
         * @return the info
         */
    private GeometryInfo getInfo() {
	return info;
    }

    /**
         * @return the uPointCount
         */
    private int getUPointCount() {
	return uPointCount;
    }

    /**
         * @param pointCount
         *                the uPointCount to set
         */
    public void setUPointCount(int pointCount) {
	uPointCount = pointCount;
    }

    /**
         * @return the vPointCount
         */
    private int getVPointCount() {
	return vPointCount;
    }

    /**
         * @param pointCount
         *                the vPointCount to set
         */
    public void setVPointCount(int pointCount) {
	vPointCount = pointCount;
    }

    /**
         * @return the uMax
         */
    private double getUMax() {
	return uMax;
    }

    /**
         * @param max
         *                the uMax to set
         */
    public void setUMax(double max) {
	uMax = max;
    }

    /**
         * @return the uMin
         */
    private double getUMin() {
	return uMin;
    }

    /**
         * @param min
         *                the uMin to set
         */
    public void setUMin(double min) {
	uMin = min;
    }

    /**
         * @return the vMax
         */
    private double getVMax() {
	return vMax;
    }

    /**
         * @param max
         *                the vMax to set
         */
    public void setVMax(double max) {
	vMax = max;
    }

    /**
         * @return the vMin
         */
    private double getVMin() {
	return vMin;
    }

    /**
         * @param min
         *                the vMin to set
         */
    public void setVMin(double min) {
	vMin = min;
    }
}
