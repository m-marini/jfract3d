/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class BoxFunctionFactory implements FunctionFactory {
	private final double width;
	private final Randomizer<Double> height;

	/**
	 * @param width
	 * @param height
	 */
	public BoxFunctionFactory(final double width,
			final Randomizer<Double> height) {
		super();
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
				return (x > -width && x < width && z > -width && z < width) ? h
						: 0.;
			}
		};
	}
}
