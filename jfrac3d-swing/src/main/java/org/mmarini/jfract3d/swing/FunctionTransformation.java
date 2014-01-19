/**
 * 
 */
package org.mmarini.jfract3d.swing;

/**
 * @author US00852
 */
public class FunctionTransformation {
	/**
	 * 
	 * @param xScale
	 * @param yScale
	 * @param rot
	 * @param dx
	 * @param dy
	 * @param zScale
	 * @param dz
	 * @return
	 */
	public static FunctionTransformation create(final double xScale,
			final double yScale, final double rot, final double dx,
			final double dy, final double zScale, final double dz) {
		final double t00 = 0;
		final double t01 = 0;
		final double t02 = 0;
		final double t10 = 0;
		final double t11 = 0;
		final double t12 = 0;
		final double zz = zScale;
		final double z0 = dz;
		return new FunctionTransformation(t00, t01, t02, t10, t11, t12, zz, z0);
	}

	/**
	 * 
	 * @param t00
	 * @param t01
	 * @param t02
	 * @param t10
	 * @param t11
	 * @param t12
	 * @param zz
	 * @param z0
	 * @return
	 */
	public static FunctionTransformation create(final double t00,
			final double t01, final double t02, final double t10,
			final double t11, final double t12, final double zz, final double z0) {
		return new FunctionTransformation(t00, t01, t02, t10, t11, t12, zz, z0);
	}

	private final double t00;
	private final double t01;
	private final double t02;
	private final double t10;
	private final double t11;
	private final double t12;
	private final double zz;
	private final double z0;

	/**
	 * @param t00
	 * @param t01
	 * @param t02
	 * @param t10
	 * @param t11
	 * @param t12
	 * @param zz
	 * @param z0
	 */
	private FunctionTransformation(final double t00, final double t01,
			final double t02, final double t10, final double t11,
			final double t12, final double zz, final double z0) {
		this.t00 = t00;
		this.t01 = t01;
		this.t02 = t02;
		this.t10 = t10;
		this.t11 = t11;
		this.t12 = t12;
		this.zz = zz;
		this.z0 = z0;
	}

	/**
	 * 
	 * @param w
	 * @return
	 */
	public FunctionTransformation apply(final double w) {
		return new FunctionTransformation(t00, t01, t02, t10, t11, t12, zz * w,
				z0);
	}

	/**
	 * 
	 * @param f
	 * @return
	 */
	public Function3D apply(final Function3D f) {
		return new Function3D() {

			@Override
			public Double apply(final Double x, final Double y) {
				return f.apply(x * t00 + y * t01 + t02, x * t10 + y * t11 + t12)
						* zz + z0;
			}
		};
	}

	/**
	 * (x,y) Txy T'xy F T'z Tz
	 * 
	 * @param f
	 * @return
	 */
	public FunctionTransformation apply(final FunctionTransformation f) {
		final double r00 = f.t00 * t00 + f.t01 * t10;
		final double r01 = f.t00 * t01 + f.t01 * t11;
		final double r02 = f.t00 * t02 + f.t01 * t12 + f.t02;

		final double r10 = f.t10 * t00 + f.t11 * t10;
		final double r11 = f.t10 * t01 + f.t11 * t11;
		final double r12 = f.t10 * t02 + f.t11 * t12 + f.t12;

		final double rzz = f.zz * zz;
		final double rz0 = f.z0 * zz + z0;
		return new FunctionTransformation(r00, r01, r02, r10, r11, r12, rzz,
				rz0);
	}
}
