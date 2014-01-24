/**
 * 
 */
package org.mmarini.jfract3d;

import java.util.Random;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class GaussRandomizer implements Randomizer<Double> {
	private final Random random;
	private final double average;
	private final double width;

	/**
	 * @param random
	 * @param average
	 * @param sigma
	 * @param positive
	 */
	public GaussRandomizer(final Random random, final double average,
			final double sigma) {
		super();
		this.random = random;
		this.average = average;
		this.width = sigma;
	}

	/**
	 * @see org.mmarini.jfract3d.Randomizer#nextDouble()
	 */
	@Override
	public Double next() {
		return random.nextGaussian() * width + average;
	}

}
