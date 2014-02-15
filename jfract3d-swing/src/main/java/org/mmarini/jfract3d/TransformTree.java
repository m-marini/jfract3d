/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.geom.Point2D;

import javax.vecmath.Point3d;

import org.mmarini.fp.FPArrayList;
import org.mmarini.fp.FPList;
import org.mmarini.fp.Functor1;
import org.mmarini.fp.Functor2;

/**
 * @author marco.marini@mmarini.org
 * 
 */
public class TransformTree {
	private final FPList<TransformTree> children;
	private final FractalTransform node;

	/**
	 * @param children
	 */
	public TransformTree(final FractalTransform node,
			final FPList<TransformTree> children) {
		this.node = node;
		this.children = children;
	}

	/**
	 * @return the children
	 */
	public FPList<TransformTree> getChildren() {
		return children;
	}

	/**
	 * @return the node
	 */
	public FractalTransform getNode() {
		return node;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Tree [node=").append(node).append(", children=")
		.append(children != null ? children.size() : 0).append("]");
		return builder.toString();
	}

	/**
	 * @param s
	 * @return
	 */
	public Surface create1(final Surface s) {
		if (children == null)
			return node.createSurface(s);
		else {
			final FPList<Surface> ls = children
					.map(new Functor1<Surface, TransformTree>() {

						@Override
						public Surface apply(final TransformTree t) {
							return t.create(s);
						}
					});
			return new Surface() {

				@Override
				public Point3d apply(final Point2D p) {
					final Point2D p1 = node.transform(p);
					final Point3d p3 = s.apply(p1);
					if (p3 != null)
						for (final Surface s1 : ls) {
							final Point3d p2 = s1.apply(p1);
							if (p2 != null)
								p3.add(p2);
						}
					return p3 != null ? node.transform(p3) : null;
				}

			};
		}
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public Surface create(final Surface s) {
		final FPList<Surface> sl = createSurfaces(new FPArrayList<Surface>(),
				FractalTransform.IDENTITY, s);
		return new Surface() {

			@Override
			public Point3d apply(final Point2D p) {
				return sl.fold(null, new Functor2<Point3d, Point3d, Surface>() {

					@Override
					public Point3d apply(final Point3d p1, final Surface s1) {
						final Point3d p2 = s1.apply(p);
						if (p1 != null && p2 != null)
							p1.add(p2);
						return p1 != null ? p1 : p2;
					}
				});
			}
		};
	}

	/**
	 * 
	 * @param l
	 * @param identity
	 * @param s
	 * @return
	 */
	private FPList<Surface> createSurfaces(final FPList<Surface> l,
			final FractalTransform t, final Surface s) {
		final FractalTransform f1 = t.apply(node);
		l.add(f1.createSurface(s));
		if (children != null)
			for (final TransformTree t1 : children)
				t1.createSurfaces(l, f1, s);
		return l;
	}
}
