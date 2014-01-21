/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author US00852
 * 
 */
public class PlaneFunctionFactory implements FunctionFactory {
	private final Randomizer<Double> xRandomizer;
	private final Randomizer<Double> zRandomizer;

	/**
	 * @param xRandomizer
	 * @param zRandomizer
	 */
	public PlaneFunctionFactory(final Randomizer<Double> xRandomizer,
			final Randomizer<Double> zRandomizer) {
		this.xRandomizer = xRandomizer;
		this.zRandomizer = zRandomizer;
	}

	/**
	 * @see org.mmarini.jfract3d.FunctionFactory#create()
	 */
	@Override
	public Function3D create() {
		final double a = xRandomizer.next() / 8;
		final double b = zRandomizer.next() / 8;
		return new Function3D() {

			@Override
			public Double apply(final Double x, final Double z) {
				final double xx = Math.max(-0.5, Math.min(x, 0.5));
				final double zz = Math.max(-0.5, Math.min(z, 0.5));
				return a * xx + b * zz;
			}
		};
	}

}
