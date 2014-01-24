/**
 * 
 */
package org.mmarini.jfract3d;

/**
 * @author marco.marini@mmarini.org
 */
public class TransformedFunction implements Function3D {

	private final FractalTransform tr;
	private final Function3D f;

	/**
	 * @param f
	 * @param tr
	 */
	public TransformedFunction(final Function3D f, final FractalTransform tr) {
		this.tr = tr;
		this.f = f;
	}

	/**
	 * @see org.mmarini.jfract3d.Functor2#apply(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	public Double apply(final Double x, final Double y) {
		return tr.apply(x, y, f);
	}

	/**
	 * (x,y) Txy T'xy F T'z Tz
	 * 
	 * @param t2
	 * @return
	 */
	public TransformedFunction apply(final FractalTransform t2) {
		return new TransformedFunction(f, tr.apply(t2));
	}

	/**
	 * @param f
	 * @return
	 */
	public TransformedFunction apply(final Function3D f) {
		return new TransformedFunction(f, tr);
	}

	/**
	 * 
	 * @param theta
	 * @return
	 */
	public TransformedFunction rotate(final double theta) {
		return new TransformedFunction(f, tr.rotate(theta));
	}

	/**
	 * 
	 * @param sx
	 * @param sy
	 * @param sz
	 * @return
	 */
	public TransformedFunction scale(final double sx, final double sy,
			final double sz) {
		return new TransformedFunction(f, tr.scale(sx, sy, sz));
	}

	/**
	 * 
	 * @param theta
	 * @return
	 */
	public TransformedFunction translate(final double dx, final double dy,
			final double dz) {
		return new TransformedFunction(f, tr.translate(dx, dy, dz));
	}
}
