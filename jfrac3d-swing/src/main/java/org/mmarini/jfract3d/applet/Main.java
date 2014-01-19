/**
 * 
 */
package org.mmarini.jfract3d.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Random;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Geometry;
import javax.media.j3d.Material;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * @author US00852
 * 
 */
public class Main {
	private static final Function3D A = new Function3D() {

		@Override
		public double apply(final double x, final double y) {
			final double s = Math.sqrt(x * x + y * y);
			final double s1 = (s <= Double.MIN_VALUE) ? 1 : Math.sin(s) / s;
			return s1 * HEIGHT;
		}
	};
	private static final Function3D SIGMOIDAL = new Function3D() {

		@Override
		public double apply(final double x, final double y) {
			return Math.exp(-(x * x + y * y) / S) * HEIGHT;
		}
	};
	private static final Function3D PEEK = new Function3D() {

		@Override
		public double apply(final double x, final double y) {
			return Math.exp(-Math.sqrt((x * x + y * y) / S)) * HEIGHT;
		}
	};
	private static final Function3D PIRAMID = new Function3D() {

		@Override
		public double apply(final double x, final double y) {
			final double f1 = Math.min(x + 0.5, 0.5 - x);
			final double f2 = Math.min(y + 0.5, 0.5 - y);
			return Math.max(Math.min(f1, f2), 0) * 2 * HEIGHT;
		}
	};

	/**
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		new Main().run();
	}

	private final JFrame frame;
	private final Function3D seedFunction;
	private static final Bounds DEFAULT_BOUNDS = new BoundingSphere(
			new Point3d(0.0, 0.0, 0.0), 100.0);

	private static final int X_COUNT = 100;
	private static final int Z_COUNT = 100;
	private static final double X_MIN = -0.5;
	private static final double X_MAX = 0.5;
	private static final double Z_MIN = -0.5;
	private static final double Z_MAX = 0.5;
	private static final int ITERATION_COUNT = 5;
	private static final double HEIGHT = 500e-3;
	private static final double S = 50e-3;
	private static final double Z_SCALE = 0.4;
	private static final double Z0 = 0;

	/**
	 * 
	 */
	public Main() {
		frame = new JFrame("JFract3D");
		seedFunction = SIGMOIDAL;

		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(createCanvas(), BorderLayout.CENTER);
	}

	/**
	 * @param u
	 */
	private void addBranches(final SimpleUniverse u) {
		// Setting viewing plattform
		setViewingPlatform(u.getViewingPlatform());
		u.addBranchGraph(createScene());
	}

	/**
	 * 
	 * @return
	 */
	private Canvas3D createCanvas() {
		final Canvas3D c = new Canvas3D(
				SimpleUniverse.getPreferredConfiguration());

		// Create and add branches to the universe
		final SimpleUniverse u = new SimpleUniverse(c);
		addBranches(u);

		// Add orbit behavior
		final OrbitBehavior orbit = new OrbitBehavior(c,
				OrbitBehavior.REVERSE_ALL);
		orbit.setSchedulingBounds(DEFAULT_BOUNDS);
		u.getViewingPlatform().setViewPlatformBehavior(orbit);

		return c;
	}

	/**
	 * The scene is composed as:
	 * 
	 * <pre>
	 * 
	 *           root
	 *          /    \     
	 * transform ____ rotator 
	 *     |
	 *  subject
	 * 
	 * </pre>
	 * 
	 * @return
	 */
	private BranchGroup createScene() {
		// Create the root of the branch graph
		final BranchGroup root = new BranchGroup();

		// Create the TransformGroup node and initialize it to the
		// identity. Enable the TRANSFORM_WRITE capability so that
		// our behavior code can modify it at run time. Add it to
		// the root of the subgraph.
		final TransformGroup trans = new TransformGroup();
		trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		root.addChild(trans);

		// Create a simple Shape3D node; add it to the scene graph.
		trans.addChild(new Shape3D(createSubjectGeometry(),
				createSubjectAppearance()));

		// Create a new Behavior object that will perform the
		// desired operation on the specified transform and add
		// it into the scene graph.
		final RotationInterpolator rot = new RotationInterpolator(new Alpha(-1,
				20000), trans, new Transform3D(), 0.0f, (float) Math.PI * 2.0f);
		final BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0,
				0.0), 100.0);
		rot.setSchedulingBounds(bounds);
		root.addChild(rot);

		// Have Java 3D perform optimizations on this scene graph.
		root.compile();

		return root;
	}

	/**
	 * @return
	 */
	private Appearance createSubjectAppearance() {
		final Appearance a = new Appearance();
		a.setMaterial(new Material());
		return a;
	}

	/**
	 * @return
	 */
	private Point3d[][] createSubjectCoords() {
		final Point3d[][] c = new Point3d[Z_COUNT][X_COUNT];
		// final Randomizer r = new LinearRandomizer(1, 1);
		final Randomizer r = new Randomizer() {
			private final Random r = new Random();

			@Override
			public double randomize() {
				final double s = r.nextGaussian() * 1. / 7 + 1;
				return r.nextBoolean() ? -s : s;
			}
		};
		final Function3D f = new FractalFunctionBuilder(r, ITERATION_COUNT,
				new FunctionTransformation(2, 0, -0.5, 0, 2, -0.5, Z_SCALE, Z0)
						.apply(r.randomize()), new FunctionTransformation(2, 0,
						-0.5, 0, 2, 0.5, Z_SCALE, Z0).apply(r.randomize()),
				new FunctionTransformation(2, 0, 0.5, 0, 2, -0.5, Z_SCALE, Z0)
						.apply(r.randomize()), new FunctionTransformation(2, 0,
						0.5, 0, 2, 0.5, Z_SCALE, Z0).apply(r.randomize()))
				.create(seedFunction);
		for (int i = 0; i < Z_COUNT; ++i) {
			final double z = Z_MAX - i * (Z_MAX - Z_MIN) / (Z_COUNT - 1);
			for (int j = 0; j < X_COUNT; ++j) {
				final double x = j * (X_MAX - X_MIN) / (X_COUNT - 1) + X_MIN;
				c[i][j] = new Point3d(x, f.apply(x, z), z);
			}
		}
		return c;
	}

	/**
	 * @return
	 */
	private Geometry createSubjectGeometry() {
		final Point3d[][] coords = createSubjectCoords();

		// Compute quad array
		final int n = coords.length;
		final int m = coords[0].length;
		final Point3d[] q = new Point3d[(n - 1) * (m - 1) * 4];
		int idx = 0;
		for (int i = 0; i < n - 1; ++i) {
			for (int j = 0; j < m - 1; ++j) {
				q[idx++] = coords[i][j];
				q[idx++] = coords[i][j + 1];
				q[idx++] = coords[i + 1][j + 1];
				q[idx++] = coords[i + 1][j];
			}
		}

		// Compute geometry
		final GeometryInfo info = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
		info.setCoordinates(q);
		new NormalGenerator().generateNormals(info);
		new Stripifier().stripify(info);
		return info.getGeometryArray();
	}

	/**
	 * 
	 */
	private void run() {
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	/**
	 * @param vp
	 */
	private void setViewingPlatform(final ViewingPlatform vp) {

		final PlatformGeometry pg = new PlatformGeometry();

		// Set up the ambient light
		final AmbientLight al = new AmbientLight(new Color3f(0.1f, 0.1f, 0.1f));
		al.setInfluencingBounds(DEFAULT_BOUNDS);
		pg.addChild(al);

		// Set up the directional light 1
		final DirectionalLight l1 = new DirectionalLight(new Color3f(1.0f,
				1.0f, 0.9f), new Vector3f(1.0f, 1.0f, 1.0f));
		l1.setInfluencingBounds(DEFAULT_BOUNDS);
		pg.addChild(l1);

		// Set up the directional light 2
		final DirectionalLight l2 = new DirectionalLight(new Color3f(1.0f,
				1.0f, 1.0f), new Vector3f(-1.0f, -1.0f, -1.0f));
		l2.setInfluencingBounds(DEFAULT_BOUNDS);
		pg.addChild(l2);

		vp.setPlatformGeometry(pg);

		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		vp.setNominalViewingTransform();
	}
}
