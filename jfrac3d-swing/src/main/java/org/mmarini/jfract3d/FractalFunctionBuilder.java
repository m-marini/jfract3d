/**
 * 
 */
package org.mmarini.jfract3d;

import java.util.Collections;

import org.mmarini.fp.FPArrayList;
import org.mmarini.fp.FPList;
import org.mmarini.fp.Functor2;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class FractalFunctionBuilder {
	private final FunctionFactory factory;
	private final int depth;
	private final FractalTransform[] trans;

	/**
	 * @param factory
	 * @param depth
	 * @param trans
	 */
	public FractalFunctionBuilder(final FunctionFactory factory,
			final int depth, final FractalTransform... trans) {
		this.factory = factory;
		this.depth = depth;
		this.trans = trans;
	}

	/**
	 * 
	 * @return
	 */
	public Function3D create() {

		// Generate each fractal function
		final FPList<TransformedFunction> l = new FPArrayList<>();
		final Function3D s = factory.create();
		for (final FractalTransform t : trans)
			l.add(new TransformedFunction(s, t));
		final FPList<Function3D> l2 = new FPArrayList<Function3D>(
				createFractalTransforms(l, depth));
		l2.add(s);

		// Compose the generated functions
		return new Function3D() {

			@Override
			public Double apply(final Double x, final Double y) {
				return l2.fold(0.0, new Functor2<Double, Double, Function3D>() {
					@Override
					public Double apply(final Double s, final Function3D f) {
						return s + f.apply(x, y);
					}
				});
			}
		};
	}

	/**
	 * @param l
	 * @param d
	 * @return
	 */
	private FPList<TransformedFunction> createFractalTransforms(
			final FPList<TransformedFunction> l, final int d) {
		if (d == 0)
			return new FPArrayList<>(
					Collections.<TransformedFunction> emptyList());
		if (d == 1)
			return l;
		final FPList<TransformedFunction> l1 = new FPArrayList<>();
		for (final TransformedFunction t1 : l)
			for (final FractalTransform t2 : trans)
				l1.add(t1.apply(factory.create()).apply(t2));
		final FPList<TransformedFunction> l2 = new FPArrayList<>(l);
		l2.addAll(createFractalTransforms(l1, d - 1));
		return l2;
	}
}
