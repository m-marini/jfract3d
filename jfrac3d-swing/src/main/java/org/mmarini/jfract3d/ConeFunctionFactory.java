/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class ConeFunctionFactory implements FunctionFactory {
	private final double radius;
	private final Randomizer<Double> height;

	/**
	 * @param radius
	 * @param height
	 */
	public ConeFunctionFactory(final double radius,
			final Randomizer<Double> height) {
		super();
		this.radius = radius;
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
				final double d = radius - Math.sqrt(x * x + z * z);
				return d > 0 ? d * h / radius : 0.;
			}
		};
	}
}
