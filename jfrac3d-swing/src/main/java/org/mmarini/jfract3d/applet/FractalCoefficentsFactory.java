/*
 * FractalCoefficentsFactory.java
 *
 * $Id$
 *
 * 13/set/07
 *
 * Copyright notice
 */
package org.mmarini.jfract3d.applet;

/**
 * @author marco.marini@mmarini.org
 * @version $Id$
 * 
 */
public class FractalCoefficentsFactory {
	private int iterCount = 2;

	private double a = 0.5;

	private double b = 2;

	private double c = 1;

	private double d = 2;

	private double e = 1;

	/**
	 * 
	 * @return
	 */
	public FractalCoefficents[] create() {
		int n = getIterCount();
		FractalCoefficents[] coef = new FractalCoefficents[n];
		double a = getA();
		double b = getB();
		double c = getC();
		double d = getD();
		double e = getE();
		double ai = 1;
		double bi = 1;
		double di = 1;
		for (int i = 0; i < n; ++i) {
			FractalCoefficents k = new FractalCoefficents();
			k.setA(ai * Math.random());
			k.setB(bi * Math.random());
			k.setC(c * Math.random());
			k.setD(di * Math.random());
			k.setE(e * Math.random());
			coef[i] = k;
			ai *= a;
			bi *= b;
			di *= d;
		}
		return coef;
	}

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
	 * @return the iterCount
	 */
	public int getIterCount() {
		return iterCount;
	}

	/**
	 * @param a
	 *            the a to set
	 */
	public void setA(double a) {
		this.a = a;
	}

	/**
	 * @param b
	 *            the b to set
	 */
	public void setB(double b) {
		this.b = b;
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

	/**
	 * @param iterCount
	 *            the iterCount to set
	 */
	public void setIterCount(int iterCount) {
		this.iterCount = iterCount;
	}

}
