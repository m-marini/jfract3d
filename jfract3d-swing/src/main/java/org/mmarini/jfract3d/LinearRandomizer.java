/**
 * 
 */
package org.mmarini.jfract3d;

import java.util.Random;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class LinearRandomizer implements Randomizer<Double> {
	private final Random random;
	private final double average;
	private final double range;

	/**
	 * @param random
	 * @param average
	 * @param range
	 * @param positive
	 */
	public LinearRandomizer(final Random random, final double average,
			final double range) {
		this.random = random;
		this.average = average;
		this.range = range;
	}

	/**
	 * @see org.mmarini.jfract3d.Randomizer#nextDouble()
	 */
	@Override
	public Double next() {
		return random.nextDouble() * range + average - range / 2;
	}

}
