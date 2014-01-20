/**
 * 
 */
package org.mmarini.jfract3d.swing;

import java.util.Random;

/**
 * @author US00852
 * 
 */
public class GaussRandomizer implements Randomizer<Double> {
	private final Random random;
	private final double average;
	private final double width;
	private final double positive;

	/**
	 * @param random
	 * @param average
	 * @param width
	 * @param positive
	 */
	public GaussRandomizer(final Random random, final double average,
			final double width, final double positive) {
		super();
		this.random = random;
		this.average = average;
		this.width = width;
		this.positive = positive;
	}

	/**
	 * @see org.mmarini.jfract3d.swing.Randomizer#nextDouble()
	 */
	@Override
	public Double next() {
		final double s = random.nextGaussian() * width + average;
		return random.nextDouble() < positive ? s : -s;
	}

}
