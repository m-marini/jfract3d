/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author US00852
 * 
 */
public class PiramidFunctionFactory implements FunctionFactory {
	private final double width;
	private final Randomizer<Double> height;

	/**
	 * @param width
	 *            TODO
	 * @param height
	 */
	public PiramidFunctionFactory(final double width,
			final Randomizer<Double> height) {
		this.width = width;
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
				final double f1 = Math.min(x + 1, 1 - x);
				final double f2 = Math.min(z + 1, 1 - z);
				return Math.max(Math.min(f1, f2), 0) * h;
			}
		};
	}
}
