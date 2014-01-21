/**
 * 
 */
package org.mmarini.jfract3d;

import java.util.Random;

/**
 * @author US00852
 * 
 */
public class LinearRandomizer implements Randomizer<Double> {
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
	public LinearRandomizer(final Random random, final double average,
			final double width, final double positive) {
		this.random = random;
		this.average = average;
		this.width = width;
		this.positive = positive;
	}

	/**
	 * @see org.mmarini.jfract3d.Randomizer#nextDouble()
	 */
	@Override
	public Double next() {
		final double s = random.nextDouble() * width * 2 + average - width;
		return random.nextDouble() < positive ? s : -s;
	}

}
