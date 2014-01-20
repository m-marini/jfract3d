/**
 * 
 */
package org.mmarini.jfract3d.swing;

/**
 * @author US00852
 * 
 */
public class CylinderFunctionFactory implements FunctionFactory {
	private final double radius2;
	private final Randomizer<Double> height;

	/**
	 * @param sigma
	 * @param height
	 */
	public CylinderFunctionFactory(final double sigma,
			final Randomizer<Double> height) {
		super();
		this.radius2 = sigma * sigma;
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
				final double d = x * x + z * z;
				return d <= radius2 ? h : 0.;
			}
		};
	}
}
