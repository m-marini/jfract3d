/**
 * 
 */
package org.mmarini.jfract3d.applet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author US00852
 * 
 */
public class FractalFunctionBuilder {
	private final Randomizer randomizer;
	private final int deep;
	private final FunctionTransformation[] trans;

	/**
	 * 
	 * @param deep
	 * @param trans
	 */
	public FractalFunctionBuilder(final int deep,
			final FunctionTransformation... trans) {
		this(new Randomizer() {

			@Override
			public double randomize() {
				return 1;
			}
		}, deep, trans);
	}

	/**
	 * 
	 * @param randomizer
	 * @param deep
	 * @param trans
	 */
	public FractalFunctionBuilder(final Randomizer randomizer, final int deep,
			final FunctionTransformation... trans) {
		this.trans = trans;
		this.deep = deep;
		this.randomizer = randomizer;
	}

	/**
	 * 
	 * @param trans
	 * @return
	 */
	private Function3D compose(final Function3D seed,
			final List<FunctionTransformation> trans) {
		return new Function3D() {

			@Override
			public double apply(final double x, final double y) {
				double z = seed.apply(x, y);
				for (final FunctionTransformation t : trans)
					z += t.apply(seed).apply(x, y);
				return z;
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public Function3D create(final Function3D seed) {
		return compose(seed,
				createFractalTransforms(Arrays.asList(trans), deep));
	}

	/**
	 * @param asl
	 * @param d
	 * @return
	 */
	private List<FunctionTransformation> createFractalTransforms(
			final List<FunctionTransformation> l, final int d) {
		if (d == 0)
			return Collections.emptyList();
		if (d == 1)
			return l;
		final List<FunctionTransformation> l1 = new ArrayList<>();
		for (final FunctionTransformation t1 : l)
			for (final FunctionTransformation t2 : trans)
				l1.add(t1.apply(t2).apply(randomizer.randomize()));
		final List<FunctionTransformation> l2 = new ArrayList<>(l);
		l2.addAll(createFractalTransforms(l1, d - 1));
		return l2;
	}
}
