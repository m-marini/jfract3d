/**
 * 
 */
package org.mmarini.jfract3d;

import java.util.Random;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class ComposedRandomizer<T> implements Randomizer<T> {
	public class WieghtRandomizer {
		private final double weight;
		private final Randomizer<T> randomizer;

		/**
		 * @param weight
		 * @param randomizer
		 */
		public WieghtRandomizer(final double weight,
				final Randomizer<T> randomizer) {
			this.weight = weight;
			this.randomizer = randomizer;
		}

		/**
		 * @return the randomizer
		 */
		public Randomizer<T> getRandomizer() {
			return randomizer;
		}

		/**
		 * @return the weight
		 */
		public double getWeight() {
			return weight;
		}
	}

	private final Random random;

	private final WieghtRandomizer[] randomizers;
	private final double weight;

	/**
	 * @param random
	 * @param average
	 * @param range
	 * @param positive
	 */
	@SuppressWarnings("unchecked")
	public ComposedRandomizer(final Random random,
			final WieghtRandomizer... randomizers) {
		this.random = random;
		this.randomizers = randomizers;
		double w = 0.0;
		for (final WieghtRandomizer r : randomizers)
			w += r.getWeight();
		this.weight = w;
	}

	/**
	 * @see org.mmarini.jfract3d.Randomizer#nextDouble()
	 */
	@Override
	public T next() {
		final double s = random.nextDouble() * weight;
		for (final WieghtRandomizer r : randomizers)
			if (s < r.getWeight())
				return r.getRandomizer().next();
		return null;
	}
}
