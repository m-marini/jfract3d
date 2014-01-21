/**
 * 
 */
package org.mmarini.jfract3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author US00852
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
	 * @param trans
	 * @return
	 */
	private Function3D compose(final List<Function3D> trans) {
		return new Function3D() {

			@Override
			public Double apply(final Double x, final Double y) {
				double z = 0;
				for (final Function3D t : trans)
					z += t.apply(x, y);
				return z;
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public Function3D create() {
		final List<TransformedFunction> l = new ArrayList<>();
		final Function3D s = factory.create();
		for (final FractalTransform t : trans)
			l.add(new TransformedFunction(s, t));
		final List<Function3D> l2 = new ArrayList<Function3D>(
				createFractalTransforms(l, depth));
		l2.add(s);
		return compose(l2);
	}

	/**
	 * @param l
	 * @param d
	 * @return
	 */
	private List<TransformedFunction> createFractalTransforms(
			final List<TransformedFunction> l, final int d) {
		if (d == 0)
			return Collections.emptyList();
		if (d == 1)
			return l;
		final List<TransformedFunction> l1 = new ArrayList<>();
		for (final TransformedFunction t1 : l)
			for (final FractalTransform t2 : trans)
				l1.add(t1.apply(factory.create()).apply(t2));
		final List<TransformedFunction> l2 = new ArrayList<>(l);
		l2.addAll(createFractalTransforms(l1, d - 1));
		return l2;
	}
}
