/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class GaussianFunctionFactory implements FunctionFactory {
	private final double sigma;
	private final Randomizer<Double> height;

	/**
	 * @param sigma
	 * @param height
	 */
	public GaussianFunctionFactory(final double sigma,
			final Randomizer<Double> height) {
		this.sigma = sigma;
		this.height = height;
	}

	/**
	 * @see org.mmarini.jfract3d.FunctionFactory#create()
	 */
	@Override
	public Function3D create() {
		final double h = height.next();
		final double k = -0.5 / sigma / sigma;
		return new Function3D() {

			@Override
			public Double apply(final Double x, final Double z) {
				return Math.exp((x * x + z * z) * k) * h;
			}
		};
	}
}
