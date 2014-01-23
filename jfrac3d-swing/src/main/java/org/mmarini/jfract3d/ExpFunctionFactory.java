/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class ExpFunctionFactory implements FunctionFactory {
	private final double sigma;
	private final Randomizer<Double> height;

	/**
	 * @param sigma
	 * @param height
	 */
	public ExpFunctionFactory(final double sigma,
			final Randomizer<Double> height) {
		super();
		this.sigma = sigma;
		this.height = height;
	}

	/**
	 * @see org.mmarini.jfract3d.FunctionFactory#create()
	 */
	@Override
	public Function3D create() {
		final double h = height.next();
		return new Function3D() {

			@Override
			public Double apply(final Double x, final Double z) {
				return Math.exp(-Math.sqrt(x * x + z * z) / sigma) * h;
			}
		};
	}
}
