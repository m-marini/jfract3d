/**
 * 
 */
package org.mmarini.jfract3d.swing;

/**
 * @author US00852
 * 
 */
public class BoxFunctionFactory implements FunctionFactory {
	private final double sigma;
	private final Randomizer<Double> height;

	/**
	 * @param sigma
	 * @param height
	 */
	public BoxFunctionFactory(final double sigma,
			final Randomizer<Double> height) {
		super();
		this.sigma = sigma;
		this.height = height;
	}

	/**
	 * @see org.mmarini.jfract3d.swing.FunctionFactory#create()
	 */
	@Override
	public Function3D create() {
		final double h = height.next();
		return new Function3D() {

			@Override
			public Double apply(final Double x, final Double z) {
				return (x > -sigma && x < sigma && z > -sigma && z < sigma) ? h
						: 0.;
			}
		};
	}
}
