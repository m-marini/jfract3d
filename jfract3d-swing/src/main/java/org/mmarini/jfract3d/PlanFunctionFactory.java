/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class PlanFunctionFactory implements FunctionFactory {
	private final Randomizer<Double> xRandomizer;
	private final Randomizer<Double> zRandomizer;

	/**
	 * @param xRandomizer
	 * @param zRandomizer
	 */
	public PlanFunctionFactory(final Randomizer<Double> xRandomizer,
			final Randomizer<Double> zRandomizer) {
		this.xRandomizer = xRandomizer;
		this.zRandomizer = zRandomizer;
	}

	/**
	 * @see org.mmarini.jfract3d.FunctionFactory#create()
	 */
	@Override
	public Function3D create() {
		final double a = xRandomizer.next();
		final double b = zRandomizer.next();
		return new Function3D() {

			@Override
			public Double apply(final Double x, final Double z) {
				final double xx = Math.max(-1, Math.min(x, 1));
				final double zz = Math.max(-1, Math.min(z, 1));
				return a * xx + b * zz;
			}
		};
	}

}
