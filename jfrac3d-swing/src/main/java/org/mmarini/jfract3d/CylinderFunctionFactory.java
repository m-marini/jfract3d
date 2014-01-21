/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author US00852
 * 
 */
public class CylinderFunctionFactory implements FunctionFactory {
	private final double radius2;
	private final Randomizer<Double> height;

	/**
	 * @param radius
	 * @param height
	 */
	public CylinderFunctionFactory(final double radius,
			final Randomizer<Double> height) {
		this.radius2 = radius * radius;
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
				final double d = x * x + z * z;
				return d <= radius2 ? h : 0.;
			}
		};
	}
}
