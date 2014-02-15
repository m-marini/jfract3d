/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.geom.AffineTransform;
import javax.media.j3d.Transform3D;
import org.mmarini.fp.FPList;
import org.mmarini.fp.Functor1;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class FractalSurfaceBuilder {

	private static final FractalTransform IDENTITY = new FractalTransform(
			new AffineTransform(), new Transform3D());

	private final int depth;
	private final TransformFactory factory;

	/**
	 * @param factory
	 * @param depth
	 * @param trans
	 */
	public FractalSurfaceBuilder(final TransformFactory factory, final int depth) {
		this.depth = depth;
		this.factory = factory;
	}

	/**
	 * 
	 * @return
	 */
	public Surface build(final Surface s) {
		return new TransformTree(IDENTITY, createTree(depth)).create(s);
	}

	/**
	 * 
	 * @param d
	 * @return
	 */
	private FPList<TransformTree> createTree(final int d) {
		return d > 0 ? factory.create().map(
				new Functor1<TransformTree, FractalTransform>() {

					@Override
					public TransformTree apply(final FractalTransform f) {
						return new TransformTree(f, createTree(d - 1));
					}
				}) : null;
	}
}
