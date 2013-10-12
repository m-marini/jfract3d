package org.mmarini.jfract3d.applet;

/**
 * 
 * @author marco.marini@mmarini.org
 * @version $Id$
 * 
 */
public class FractalCoefficents {
	private double a = 0.5;

	private double b = 2;

	private double c = 0.2;

	private double d = 2;

	private double e = 0.3;

	/**
	 * @return the a
	 */
	public double getA() {
		return a;
	}

	/**
	 * @return the b
	 */
	public double getB() {
		return b;
	}

	/**
	 * @return the c
	 */
	public double getC() {
		return c;
	}

	/**
	 * @return the d
	 */
	public double getD() {
		return d;
	}

	/**
	 * @return the e
	 */
	public double getE() {
		return e;
	}

	/**
	 * @param a
	 *            the a to set
	 */
	public void setA(double scaleX) {
		this.a = scaleX;
	}

	/**
	 * @param b
	 *            the b to set
	 */
	public void setB(double scaleZ) {
		this.b = scaleZ;
	}

	/**
	 * @param c
	 *            the c to set
	 */
	public void setC(double c) {
		this.c = c;
	}

	/**
	 * @param d
	 *            the d to set
	 */
	public void setD(double d) {
		this.d = d;
	}

	/**
	 * @param e
	 *            the e to set
	 */
	public void setE(double e) {
		this.e = e;
	}
}
