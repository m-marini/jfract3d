/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class PyramidFunctionFactory implements FunctionFactory {
	private final double width;
	private final Randomizer<Double> height;

	/**
	 * @param width
	 *            TODO
	 * @param height
	 */
	public PyramidFunctionFactory(final double width,
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
				final double f1 = Math.min(x + width, width - x);
				final double f2 = Math.min(z + width, width - z);
				return Math.max(Math.min(f1, f2), 0) * h / width;
			}
		};
	}
}
