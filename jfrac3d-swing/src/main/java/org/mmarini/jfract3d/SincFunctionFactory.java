/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class SincFunctionFactory implements FunctionFactory {
	private final double radius;
	private final Randomizer<Double> height;

	/**
	 * @param radius
	 * @param height
	 */
	public SincFunctionFactory(final double radius,
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
				final double s = Math.sqrt(x * x + z * z) * Math.PI / radius;
				final double s1 = (s <= 1e-10) ? 1 : Math.sin(s) / (s / radius);
				return s1 * h;
			}
		};
	}
}
