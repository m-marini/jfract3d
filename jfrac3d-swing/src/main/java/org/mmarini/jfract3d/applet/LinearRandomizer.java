/**
 * 
 */
package org.mmarini.jfract3d.applet;

import java.util.Random;

/**
 * @author US00852
 * 
 */
public class LinearRandomizer implements Randomizer {
	private final Random random;
	private final double offset;
	private final double scale;

	/**
	 * 
	 * @param min
	 * @param max
	 */
	public LinearRandomizer(final double min, final double max) {
		this(new Random(), min, max);
	}

	/**
	 * 
	 * @param random
	 * @param min
	 * @param max
	 */
	public LinearRandomizer(final Random random, final double min,
			final double max) {
		this.random = random;
		this.offset = min;
		this.scale = max - min;
	}

	/**
	 * @see org.mmarini.jfract3d.applet.Randomizer#randomize()
	 */
	@Override
	public double randomize() {
		return random.nextDouble() * scale + offset;
	}

}
