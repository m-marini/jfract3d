/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import org.mmarini.jfract3d.FunctionDialog.Function;
import org.mmarini.swing.ActionBuilder;
import org.mmarini.swing.GridLayoutHelper;
import org.mmarini.swing.SwingOptions;
import org.mmarini.swing.SwingTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * The entry point class for JFract3D.
 * 
 * @author marco.marini@mmarini.org
 * 
 */
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static final Bounds DEFAULT_BOUNDS = new BoundingSphere(
			new Point3d(0.0, 0.0, 0.0), 100.0);

	private static final long DEFAULT_SEED = 124l;
	private static final double Y_SCALE = 500e-3;
	private static final double Y_SCALE_STEP = 10e-3;

	/**
	 * 
	 * @param args
	 *            the command line argument. Ignored.
	 */
	public static void main(final String[] args) {
		logger.info("Started ..."); //$NON-NLS-1$
		new Main().run();
	}

	private final JFrame frame;
	private final AbstractAction gridAction;
	private final AbstractAction applyAction;
	private final AbstractAction functionAction;
	private final AbstractAction helpAction;
	private final AbstractAction exitAction;
	private final GridDialog gridDialog;
	private final FunctionDialog functionDialog;
	private final JDialog helpDialog;
	private final SpinnerNumberModel gridCountModel;
	private final SpinnerNumberModel depthModel;
	private final SpinnerNumberModel seedModel;
	private final SpinnerNumberModel yScaleModel;
	private final JComboBox<String> gridSelector;
	private final JComboBox<String> functionSelector;
	private final SwingOptions options;
	private final TransformGroup trans;

	/**
	 * Create the UI.
	 */
	public Main() {
		options = new SwingOptions(Main.class);
		frame = new JFrame(Messages.getString("Main.title")); //$NON-NLS-1$
		trans = new TransformGroup();
		gridCountModel = new SpinnerNumberModel(65, 3, 129, 1);
		depthModel = new SpinnerNumberModel(4, 0, 6, 1);
		seedModel = new SpinnerNumberModel(DEFAULT_SEED, null, null, 1l);
		yScaleModel = new SpinnerNumberModel(Y_SCALE, null, null, Y_SCALE_STEP);
		gridDialog = new GridDialog(frame, -1, 1, -1, 1);
		functionDialog = new FunctionDialog(frame, Function.GAUSSIAN);
		helpDialog = SwingTools.createBrowserDialog(frame,
				Messages.getString("Main.help.url")); //$NON-NLS-1$
		gridAction = new AbstractAction() {
			private static final long serialVersionUID = 1144447490677895560L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (gridDialog.showDialog())
					trans.setChild(createSubjectShape(), 0);
			}

		};
		helpAction = new AbstractAction() {
			private static final long serialVersionUID = 178909437966653741L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				final Dimension ss = Toolkit.getDefaultToolkit()
						.getScreenSize();
				final Dimension fs = frame.getSize();
				final Point fl = frame.getLocation();
				final int hw = 400;
				final int hx = Math.min(fl.x + fs.width, ss.width - hw);
				helpDialog.setLocation(hx, fl.y);
				helpDialog.setSize(hw, fs.height);
				helpDialog.setVisible(true);
			}

		};
		applyAction = new AbstractAction() {
			private static final long serialVersionUID = -8583063658609831086L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				// Set the selected function index
				functionDialog.setFunction(Function.values()[functionSelector
						.getSelectedIndex()]);
				trans.setChild(createSubjectShape(), 0);
			}

		};
		functionAction = new AbstractAction() {
			private static final long serialVersionUID = 4554447120464611067L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (functionDialog
						.showDialog(Function.values()[functionSelector
								.getSelectedIndex()]))
					trans.setChild(createSubjectShape(), 0);
			}

		};
		exitAction = new AbstractAction() {
			private static final long serialVersionUID = 4554447120464611067L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);
			}

		};
		gridSelector = new JComboBox<String>(Messages.getString(
				"Main.gridType.names").split(",")); //$NON-NLS-1$ //$NON-NLS-2$
		functionSelector = new JComboBox<String>(Messages.getString(
				"Main.function.names").split(",")); //$NON-NLS-1$ //$NON-NLS-2$

		final ActionBuilder builder = ActionBuilder.create(
				Messages.RESOURCE_BUNDLE, options, frame, gridDialog,
				functionDialog);

		builder.setUp(gridAction, "gridPane"); //$NON-NLS-1$
		builder.setUp(applyAction, "apply"); //$NON-NLS-1$
		builder.setUp(functionAction, "functionPane"); //$NON-NLS-1$
		builder.setUp(helpAction, "helpContent"); //$NON-NLS-1$
		builder.setUp(exitAction, "exit"); //$NON-NLS-1$

		gridSelector.setSelectedIndex(0);
		functionSelector.setSelectedIndex(0);

		helpDialog.setTitle(Messages.getString("Main.help.title")); //$NON-NLS-1$

		frame.setLayout(new BorderLayout());
		frame.setJMenuBar(builder.createMenuBar(
				"file", exitAction, "options", "lookAndFeel", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"help", helpAction)); //$NON-NLS-1$
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(createCanvas(), BorderLayout.CENTER);
		c.add(createControlPane(), BorderLayout.SOUTH);
		SwingTools.centerOnScreen(frame);
	}

	/**
	 * Add the viewing platform and the scene into the universe
	 * 
	 * @param u
	 *            the universe
	 */
	private void addBranches(final SimpleUniverse u) {
		// Setting viewing plattform
		setViewingPlatform(u.getViewingPlatform());
		u.addBranchGraph(createScene());
	}

	/**
	 * Create the 3D canvas.
	 * 
	 * @return the canvas
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
	 * Create the control panel.
	 * 
	 * @return the control panel
	 */
	private Component createControlPane() {
		final JPanel c = new JPanel();

		final JPanel gc = new GridLayoutHelper<>(Messages.RESOURCE_BUNDLE,
				new JPanel())
				.modify("insets,2 w") //$NON-NLS-1$
				.add("Main.points.text", //$NON-NLS-1$
						"+w hspan", //$NON-NLS-1$
						SwingTools.createNumberSpinner(gridCountModel, "#0", 3), //$NON-NLS-1$
						"Main.type.text", gridSelector, gridAction) //$NON-NLS-1$
				.getContainer();
		gc.setBorder(BorderFactory.createTitledBorder(Messages
				.getString("Main.grid.title"))); //$NON-NLS-1$

		c.add(gc);

		final JPanel fc = new GridLayoutHelper<>(Messages.RESOURCE_BUNDLE,
				new JPanel()).modify("insets,2 w") //$NON-NLS-1$
				.add("Main.depth.text", //$NON-NLS-1$
						SwingTools.createNumberSpinner(depthModel, "#,##0", 2), //$NON-NLS-1$
						"Main.function.text", //$NON-NLS-1$
						functionSelector,
						"+hspan", //$NON-NLS-1$
						functionAction,
						"Main.yScale.text", //$NON-NLS-1$
						SwingTools.createNumberSpinner(yScaleModel,
								"#,##0.000", 6), "Main.seed.text", "+hspan", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						SwingTools.createNumberSpinner(seedModel, "#0", 6)) //$NON-NLS-1$
				.getContainer();
		fc.setBorder(BorderFactory.createTitledBorder(Messages
				.getString("Main.fractal.text"))); //$NON-NLS-1$

		c.add(fc);

		c.add(new JButton(applyAction));
		return c;
	}

	/**
	 * Create the seed function factory.
	 * <p>
	 * The factory is used to create the fractal resulting function.
	 * </p>
	 * 
	 * @return the factory
	 */
	private FunctionFactory createFunctionFactory() {
		final long s = seedModel.getNumber().longValue();
		return functionDialog.createFunctionFactory(s == 0 ? new Random()
				: new Random(s));
	}

	/**
	 * Create the scene nodes.
	 * <p>
	 * The scene is composed as:
	 * </p>
	 * 
	 * <pre>
	 *               root
	 *              /    \     
	 *     transform ____ rotator 
	 *         |
	 *  fractal surface
	 * 
	 * </pre>
	 * 
	 * @return the scene
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
	 * Create the appearance of fractal surface
	 * 
	 * @return the appearance
	 */
	private Appearance createSubjectAppearance() {
		final Appearance a = new Appearance();
		a.setMaterial(new Material());
		return a;
	}

	/**
	 * Create the fractal surface geometry
	 * 
	 * @return the geometry
	 */
	private Geometry createSubjectGeometry() {
		final FunctionFactory f = createFunctionFactory();
		final double yScale = yScaleModel.getNumber().doubleValue();
		final FractalTransform[] s = {
				FractalTransform.create(2, 2, yScale, 0, -0.5, -0.5, 0),
				FractalTransform.create(2, 2, yScale, 0, -0.5, 0.5, 0),
				FractalTransform.create(2, 2, yScale, 0, 0.5, -0.5, 0),
				FractalTransform.create(2, 2, yScale, 0, 0.5, 0.5, 0) };
		final GeometryBuilder b = gridSelector.getSelectedIndex() == 0 ? IsoGeometryBuilder
				.create(gridCountModel.getNumber().intValue(), gridDialog
						.getxMin(), gridDialog.getxMax(), gridDialog.getzMin(),
						gridDialog.getzMax(),
						depthModel.getNumber().intValue(), f, s)
				: QuadGeometryBuilder.create(gridCountModel.getNumber()
						.intValue(), gridDialog.getxMin(),
						gridDialog.getxMax(), gridDialog.getzMin(), gridDialog
								.getzMax(), depthModel.getNumber().intValue(),
						f, s);
		return b.build();
	}

	/**
	 * Create the fractal surface
	 * 
	 * @return the surface
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
	 * Start the applicaton.
	 */
	private void run() {
		frame.setVisible(true);
	}

	/**
	 * Set the viewing platform
	 * <p>
	 * The viewing platforms is composed by:
	 * <ul>
	 * <li>a soft white ambient light,</li>
	 * <li>a soft directional light coming from up, backword, left</li>
	 * <li>a shining white light coming from up, backward, right.</li></a>
	 * </p>
	 * 
	 * @param vp
	 *            the viewing platform
	 */
	private void setViewingPlatform(final ViewingPlatform vp) {

		final PlatformGeometry pg = new PlatformGeometry();

		// Set up the ambient light
		final AmbientLight al = new AmbientLight(new Color3f(0.1f, 0.1f, 0.1f));
		al.setInfluencingBounds(DEFAULT_BOUNDS);
		pg.addChild(al);

		// Set up the directional light 1
		final DirectionalLight l1 = new DirectionalLight(new Color3f(0.2f,
				0.15f, 0.1f), new Vector3f(1.0f, -0.5f, -1.0f));
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
