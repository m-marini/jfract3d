/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class HemisphereFunctionFactory implements FunctionFactory {
	private final double radius;
	private final Randomizer<Double> height;

	/**
	 * @param radius
	 * @param height
	 */
	public HemisphereFunctionFactory(final double radius,
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
				final double s = radius * radius - x * x - z * z;
				return s > 0 ? Math.sqrt(s) * h / radius : 0.;
			}
		};
	}
}
