/**
 * 
 */
package org.mmarini.jfract3d.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
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
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * @author US00852
 * 
 */
public class Main {

	private static final Function3D[] FUNCTIONS = { new Function3D() {

		@Override
		public Double apply(final Double x, final Double y) {
			// Gauss function
			return Math.exp(-(x * x + y * y) / S) * HEIGHT;
		}
	}, new Function3D() {

		@Override
		public Double apply(final Double x, final Double y) {
			// Piramid function
			final double f1 = Math.min(x + 1, 1 - x);
			final double f2 = Math.min(y + 1, 1 - y);
			return Math.max(Math.min(f1, f2), 0) * HEIGHT;
		}
	}, new Function3D() {

		@Override
		public Double apply(final Double x, final Double y) {
			// Exponential function
			return Math.exp(-Math.sqrt((x * x + y * y) / S)) * HEIGHT;
		}
	}, new Function3D() {

		@Override
		public Double apply(final Double x, final Double y) {
			// Box
			return (x > -0.5 && x < 0.5 && y > -0.5 && y < 0.5) ? HEIGHT : 0.;
		}
	}, new Function3D() {

		@Override
		public Double apply(final Double x, final Double y) {
			// Cylinder
			final double d = x * x + y * y;
			return d <= 0.25 ? HEIGHT : 0.;
		}
	}, new Function3D() {

		@Override
		public Double apply(final Double x, final Double y) {
			// Cone
			final double d = 1 - Math.sqrt(x * x + y * y);
			return d > 0 ? d * HEIGHT : 0.;
		}
	}, new Function3D() {

		@Override
		public Double apply(final Double x, final Double y) {
			// Semisphere
			final double s = 1 - x * x - y * y;
			return s > 0 ? Math.sqrt(s) * HEIGHT : 0.;
		}
	}, new Function3D() {

		@Override
		public Double apply(final Double x, final Double y) {
			// Sync function
			final double s = Math.sqrt(x * x + y * y) * Math.PI;
			final double s1 = (s <= 1e-10) ? 1 : Math.sin(s) / s;
			return s1 * HEIGHT;
		}
	} };

	private static final Bounds DEFAULT_BOUNDS = new BoundingSphere(
			new Point3d(0.0, 0.0, 0.0), 100.0);

	private static final double X_MIN = -1;
	private static final double X_MAX = 1;
	private static final double Z_MIN = -1;
	private static final double Z_MAX = 1;
	private static final double HEIGHT = 500e-3;
	private static final double S = 200e-3;
	private static final double Z_SCALE = 0.5;
	private static final double Z0 = 0;

	/**
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		new Main().run();
	}

	private final JFrame frame;
	private final TransformGroup trans;
	private final SpinnerNumberModel gridCountModel;
	private final SpinnerNumberModel depthModel;
	private final AbstractAction gridAction;
	private final AbstractAction applyAction;
	private final AbstractAction functionAction;
	private final AbstractAction randomizerAction;
	private final AbstractAction helpAction;
	private final Component gridPane;
	private final JComboBox<String> gridSelector;
	private final JComboBox<String> functionSelector;
	private final JComboBox<String> randomizerSelector;

	/**
	 * 
	 */
	public Main() {
		frame = new JFrame(Messages.getString("Main.title")); //$NON-NLS-1$
		trans = new TransformGroup();
		gridCountModel = new SpinnerNumberModel(65, 3, 129, 1);
		depthModel = new SpinnerNumberModel(4, 0, 6, 1);
		gridAction = new AbstractAction() {
			private static final long serialVersionUID = 1144447490677895560L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				// TODO
			}

		};
		helpAction = new AbstractAction() {
			private static final long serialVersionUID = 178909437966653741L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				// TODO
			}

		};
		applyAction = new AbstractAction() {
			private static final long serialVersionUID = -8583063658609831086L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				trans.setChild(createSubjectShape(), 0);
			}

		};
		functionAction = new AbstractAction() {
			private static final long serialVersionUID = 4554447120464611067L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				// TODO
			}

		};
		randomizerAction = new AbstractAction() {
			private static final long serialVersionUID = -2596155533532422674L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				// TODO
			}

		};
		gridSelector = new JComboBox<String>(Messages.getString(
				"Main.gridType.names").split(",")); //$NON-NLS-1$
		functionSelector = new JComboBox<String>(Messages.getString(
				"Main.function.names").split(",")); //$NON-NLS-1$
		randomizerSelector = new JComboBox<String>(Messages.getString(
				"Main.randomizer.names").split(",")); //$NON-NLS-1$
		gridPane = createGridPane();

		final ActionBuilder builder = new ActionBuilder(new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {
				SwingUtilities.updateComponentTreeUI(frame);
			}
		});

		builder.setUp(gridAction, "gridPane"); //$NON-NLS-1$
		builder.setUp(applyAction, "apply"); //$NON-NLS-1$
		builder.setUp(functionAction, "functionPane"); //$NON-NLS-1$
		builder.setUp(randomizerAction, "randomizerPane"); //$NON-NLS-1$
		builder.setUp(helpAction, "helpContent"); //$NON-NLS-1$

		gridSelector.setSelectedIndex(0);
		functionSelector.setSelectedIndex(0);
		randomizerSelector.setSelectedIndex(0);

		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(createCanvas(), BorderLayout.CENTER);
		c.add(createControlPane(), BorderLayout.SOUTH);
		frame.setJMenuBar(builder.createMenuBar("options", "lookAndFeel", //$NON-NLS-1$ //$NON-NLS-2$
				"help", helpAction)); //$NON-NLS-1$
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
	 * 
	 * @return
	 */
	private Component createControlPane() {
		final JPanel c = new JPanel();

		final JPanel gc = new JPanel();
		gc.add(new JLabel(Messages.getString("Main.points.text"))); //$NON-NLS-1$
		gc.add(new JSpinner(gridCountModel));
		gc.add(new JLabel(Messages.getString("Main.type.text"))); //$NON-NLS-1$
		gc.add(gridSelector);
		// gc.add(new JButton(gridAction));
		gc.setBorder(BorderFactory.createTitledBorder(Messages
				.getString("Main.grid.title"))); //$NON-NLS-1$

		c.add(gc);

		final JPanel fc = new JPanel();
		fc.add(new JLabel(Messages.getString("Main.depth.text"))); //$NON-NLS-1$
		fc.add(new JSpinner(depthModel));
		fc.add(new JLabel(Messages.getString("Main.function.text"))); //$NON-NLS-1$
		fc.add(functionSelector);
		// fc.add(new JButton(functionAction));
		fc.add(new JLabel(Messages.getString("Main.randomizer.text"))); //$NON-NLS-1$
		fc.add(randomizerSelector);
		// fc.add(new JButton(randomizerAction));
		fc.setBorder(BorderFactory.createTitledBorder(Messages
				.getString("Main.fractal.text"))); //$NON-NLS-1$

		c.add(fc);

		c.add(new JButton(applyAction));
		return c;
	}

	/**
	 * s
	 * 
	 * @return
	 */
	private Component createFractalPane() {
		final Box c = Box.createVerticalBox();
		c.add(new JLabel("Depth")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.add(new JLabel("Y Scale")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.add(new JLabel("Seed Function")); //$NON-NLS-1$
		c.add(new JComboBox<String>(new String[] { "Gauss", "Piramid", //$NON-NLS-1$ //$NON-NLS-2$
				"Exponetial", "Sinc" })); //$NON-NLS-1$ //$NON-NLS-2$
		c.add(new JLabel("Height")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.add(new JLabel("Width")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.add(new JLabel("Randomizer")); //$NON-NLS-1$
		c.add(new JComboBox<String>(
				new String[] { "Gaussian", "Linear", "None" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		c.add(new JLabel("Seed")); //$NON-NLS-1$
		c.add(new JTextField(6));
		c.add(new JLabel("P(positive)")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.add(new JLabel("Average")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.add(new JLabel("Width")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.setBorder(BorderFactory.createTitledBorder("Fractal")); //$NON-NLS-1$
		return c;
	}

	/**
	 * 
	 * @return
	 */
	private Component createGridPane() {
		final Box c = Box.createVerticalBox();

		c.add(new JLabel("XMin")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.add(new JLabel("Xmax")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.add(new JLabel("ZMin")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.add(new JLabel("Zmax")); //$NON-NLS-1$
		c.add(new JSpinner());
		c.setBorder(BorderFactory.createTitledBorder("Grid")); //$NON-NLS-1$
		return c;
	}

	/**
	 * 
	 * @return
	 */
	private Randomizer createRandomizer() {
		final Randomizer r;
		switch (randomizerSelector.getSelectedIndex()) {
		case 0:
			r = new GaussRandomizer(new Random(123), 1, 1. / 7, 0.5);
			break;
		case 1:
			r = new LinearRandomizer(new Random(123), 1, 1. / 7, 0.5);
			break;
		default:
			r = new Randomizer() {

				@Override
				public double randomize() {
					return 1;
				}
			};
			break;
		}
		return r;
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
		trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		trans.setCapability(Group.ALLOW_CHILDREN_WRITE);

		// Create a simple Shape3D node; add it to the scene graph.
		trans.addChild(createSubjectShape());

		root.addChild(trans);

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
	private Geometry createSubjectGeometry() {
		final Randomizer r = createRandomizer();
		final FunctionTransformation[] transSet = {
				FunctionTransformation.create(2, 2, 0.5, 0, -0.5, -0.5, 0)
						.scale(1, 1, r.randomize()),
				FunctionTransformation.create(2, 2, 0.5, 0, -0.5, 0.5, 0)
						.scale(1, 1, r.randomize()),
				FunctionTransformation.create(2, 2, 0.5, 0, 0.5, -0.5, 0)
						.scale(1, 1, r.randomize()),
				FunctionTransformation.create(2, 2, 0.5, 0, 0.5, 0.5, 0).scale(
						1, 1, r.randomize()) };
		final Function3D f = FUNCTIONS[functionSelector.getSelectedIndex()];
		final GeometryBuilder b = gridSelector.getSelectedIndex() == 0 ? IsoGeometryBuilder
				.create(gridCountModel.getNumber().intValue(), X_MIN, X_MAX,
						Z_MIN, Z_MAX, depthModel.getNumber().intValue(),
						transSet, f, r) : QuadGeometryBuilder.create(
				gridCountModel.getNumber().intValue(), X_MIN, X_MAX, Z_MIN,
				Z_MAX, depthModel.getNumber().intValue(), transSet, f, r);
		return b.build();
	}

	/**
	 * 
	 * @return
	 */
	private Node createSubjectShape() {
		final BranchGroup bg = new BranchGroup();
		final Shape3D shape = new Shape3D(createSubjectGeometry(),
				createSubjectAppearance());
		bg.addChild(shape);
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.compile();
		return bg;
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
