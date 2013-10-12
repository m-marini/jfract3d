/*
 * JFract3D.java
 *
 * $Id$
 *
 * 10/set/07
 *
 * Copyright notice
 */
package org.mmarini.jfract3d.applet;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

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
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * @author marco.marini@mmarini.org
 * @version $Id$
 * 
 */
public class JFract3D extends Applet {
	private static final Bounds DEFAULT_BOUNDS = new BoundingSphere(
			new Point3d(0.0, 0.0, 0.0), 100.0);

	private static final long serialVersionUID = 1L;

	/**
	 * @param arg
	 * @throws Throwable
	 */
	public static void main(String[] arg) throws Throwable {
		new MainFrame(new JFract3D(), 700, 700);
	}

	private SurfaceFunction function;

	/**
	 * @return
	 */
	private Appearance createAppearance() {
		Appearance a = new Appearance();
		Material m = new Material();
		a.setMaterial(m);
		return a;
	}

	/**
	 * @return
	 */
	private SurfaceFunction createFunction() {
		FractalCoefficentsFactory factory = new FractalCoefficentsFactory();
		factory.setIterCount(7);
		FractalMount fm = new FractalMount();
		fm.setIter(factory.getIterCount());
		fm.setCoefficents(factory.create());
		XYSurfaceAdapter adp = new XYSurfaceAdapter(fm);
		return adp;
	}

	/**
	 * 
	 * @return
	 */
	private BranchGroup createSceneGraph() {
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup();

		// Create the TransformGroup node and initialize it to the
		// identity. Enable the TRANSFORM_WRITE capability so that
		// our behavior code can modify it at run time. Add it to
		// the root of the subgraph.
		TransformGroup objTrans = new TransformGroup();
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objRoot.addChild(objTrans);

		// Create a simple Shape3D node; add it to the scene graph.
		objTrans.addChild(createSubject());

		// Create a new Behavior object that will perform the
		// desired operation on the specified transform and add
		// it into the scene graph.
		Transform3D yAxis = new Transform3D();
		Alpha rotationAlpha = new Alpha(-1, 20000);

		RotationInterpolator rotator = new RotationInterpolator(rotationAlpha,
				objTrans, yAxis, 0.0f, (float) Math.PI * 2.0f);
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);
		rotator.setSchedulingBounds(bounds);
		objRoot.addChild(rotator);

		// Have Java 3D perform optimizations on this scene graph.
		objRoot.compile();

		return objRoot;
	}

	/**
	 * @return
	 */
	private Shape3D createSubject() {
		SurfaceGemoetryFactory factory = new SurfaceGemoetryFactory();
		factory.setUMin(-0.5);
		factory.setVMin(-0.5);
		factory.setUMax(0.5);
		factory.setVMax(0.5);
		factory.setUPointCount(129);
		factory.setVPointCount(129);
		Geometry g = factory.createGeometry(getFunction());
		Appearance a = createAppearance();
		Shape3D shape3D = new Shape3D(g, a);
		return shape3D;
	}

	/**
	 * @return the function
	 */
	private SurfaceFunction getFunction() {
		return function;
	}

	/**
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		SurfaceFunction adp = createFunction();
		setFunction(adp);

		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();

		Canvas3D c = new Canvas3D(config);
		add("Center", c);

		// Create a simple scene and attach it to the virtual universe
		BranchGroup scene = createSceneGraph();
		SimpleUniverse u = new SimpleUniverse(c);

		// add mouse behaviors to the ViewingPlatform
		ViewingPlatform viewingPlatform = u.getViewingPlatform();

		PlatformGeometry pg = new PlatformGeometry();

		// Set up the ambient light
		Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);
		AmbientLight ambientLightNode = new AmbientLight(ambientColor);
		ambientLightNode.setInfluencingBounds(DEFAULT_BOUNDS);
		pg.addChild(ambientLightNode);

		// Set up the directional lights
		Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
		Vector3f light1Direction = new Vector3f(1.0f, 1.0f, 1.0f);
		Color3f light2Color = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f light2Direction = new Vector3f(-1.0f, -1.0f, -1.0f);

		DirectionalLight light1 = new DirectionalLight(light1Color,
				light1Direction);
		light1.setInfluencingBounds(DEFAULT_BOUNDS);
		pg.addChild(light1);

		DirectionalLight light2 = new DirectionalLight(light2Color,
				light2Direction);
		light2.setInfluencingBounds(DEFAULT_BOUNDS);
		pg.addChild(light2);

		viewingPlatform.setPlatformGeometry(pg);

		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		viewingPlatform.setNominalViewingTransform();

		OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
		orbit.setSchedulingBounds(DEFAULT_BOUNDS);
		viewingPlatform.setViewPlatformBehavior(orbit);

		u.addBranchGraph(scene);
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(SurfaceFunction function) {
		this.function = function;
	}
}
