package org.mmarini.jfract3d.applet;

/**
 * 
 * @author marco.marini@mmarini.org
 * @version $Id$
 * 
 */
public class FractalMount implements XYSurface {
    private double alpha = 1;

    private int iter = 1;

    private FractalCoefficents[] coefficents;

    /**
         * 
         */
    public double calculateZ(double x, double y) {
	x += 0.5;
	y += 0.5;
	double z = 0;
	FractalCoefficents[] coef = getCoefficents();
	for (int i = 0; i < getIter(); ++i) {
	    FractalCoefficents k = coef[i];
	    // z += sz * randomize(1, i, ii, jj);
	    z += k.getA()
		    * calculateF(k.getB() * x + k.getC(), k.getD() * y
			    + k.getE());
	}
	return z;
    }

    public double calculateF(double x) {
	// return sew(x);
	return (1 - Math.cos(x * 2 * Math.PI)) * 0.25;
    }

    /**
         * @param x
         * @return
         */
    private double sew(double x) {
	double alpha = getAlpha();
	double r = x / alpha;
	double x1 = r - Math.floor(r);
	if (x1 < 0.5)
	    return x1;
	return 1 - x1;
    }

    public double calculateF(double x, double y) {
	double alpha = getAlpha();
	return calculateF(x / alpha) * calculateF(y / alpha);
    }

    /**
         * @return the alpha
         */
    private double getAlpha() {
	return alpha;
    }

    /**
         * @param alpha
         *                the alpha to set
         */
    public void setAlpha(double alpha) {
	this.alpha = alpha;
    }

    /**
         * @return the iter
         */
    private int getIter() {
	return iter;
    }

    /**
         * @param iter
         *                the iter to set
         */
    public void setIter(int iter) {
	this.iter = iter;
    }

    /**
         * @return the coefficents
         */
    public FractalCoefficents[] getCoefficents() {
	return coefficents;
    }

    /**
         * @param coefficents
         *                the coefficents to set
         */
    public void setCoefficents(FractalCoefficents[] coefficents) {
	this.coefficents = coefficents;
    }
}
