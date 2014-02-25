/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import org.mmarini.swing.ActionBuilder;
import org.mmarini.swing.GridLayoutHelper;
import org.mmarini.swing.SwingTools;

/**
 * Dialog to manage function parameters user entry.
 * <p>
 * The Dialog manage two class of function parameters:
 * <ul>
 * <li>Plane function containing 2 randomizers for xSlope and zSlope</li>
 * <li>Surface function containing 1 randomizer for height parameter and a fix
 * value parameter for the width.</li>
 * </p>
 * 
 * @author marco.marini@mmarini.org
 * 
 */
public class FunctionDialog extends JDialog {
	/**
	 * 
	 * @author marco.marini@mmarini.org
	 * 
	 */
	public enum Function {
		GAUSSIAN, PLAN, PIRAMID, EXPONENTIAL, BOX, CYLINDER, CONE, HEMISPHERE, SINC
	}

	private static final long serialVersionUID = 8388879557737014394L;

	private final RandomizerSelector heightRandomizer;
	private final RandomizerSelector xSlopeRandomizer;
	private final RandomizerSelector zSlopeRandomizer;
	private final SpinnerNumberModel widthModel;
	private final JPanel widthPane;
	private final JPanel planPane;
	private boolean validated;
	private double widthParm;
	private Function function;

	/**
	 * @param owner
	 */
	public FunctionDialog(final Frame owner, final Function function) {
		super(owner, true);
		this.function = function;
		widthParm = 0.5;
		heightRandomizer = new RandomizerSelector(
				RandomizerSelector.Type.LINEAR, 0, 0.4);
		xSlopeRandomizer = new RandomizerSelector(
				RandomizerSelector.Type.LINEAR, 0, 0.4);
		zSlopeRandomizer = new RandomizerSelector(
				RandomizerSelector.Type.LINEAR, 0, 0.4);
		widthModel = new SpinnerNumberModel(widthParm, 0.0, null, 0.01);

		final AbstractAction cancelAction = new AbstractAction() {
			private static final long serialVersionUID = -7989009174928372064L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				dispose();
			}

		};
		final AbstractAction restoreAction = new AbstractAction() {
			private static final long serialVersionUID = -7989009174928372064L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				restore();
			}

		};
		final AbstractAction applyAction = new AbstractAction() {
			private static final long serialVersionUID = -7989009174928372064L;

			@Override
			public void actionPerformed(final ActionEvent e) {
				// Validate
				widthParm = widthModel.getNumber().doubleValue();
				heightRandomizer.apply();
				xSlopeRandomizer.apply();
				zSlopeRandomizer.apply();
				validated = true;
				dispose();
			}

		};
		widthPane = createWidthPane();
		planPane = createPlanPane();

		final ActionBuilder b = ActionBuilder.create(Messages.RESOURCE_BUNDLE);
		b.setUp(cancelAction, "cancel"); //$NON-NLS-1$
		b.setUp(restoreAction, "restore"); //$NON-NLS-1$
		b.setUp(applyAction, "apply"); //$NON-NLS-1$

		heightRandomizer.setBorder(BorderFactory.createTitledBorder(Messages
				.getString("FunctionDialog.height.text"))); //$NON-NLS-1$
		xSlopeRandomizer.setBorder(BorderFactory.createTitledBorder(Messages
				.getString("FunctionDialog.xSlope.text"))); //$NON-NLS-1$
		zSlopeRandomizer.setBorder(BorderFactory.createTitledBorder(Messages
				.getString("FunctionDialog.zSlope.text"))); //$NON-NLS-1$

		final Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(widthPane, BorderLayout.CENTER);
		widthPane.doLayout();
		c.add(new GridLayoutHelper<>(Messages.RESOURCE_BUNDLE, new JPanel())
				.modify("insets,5") //$NON-NLS-1$
				.add("+e hw", cancelAction, restoreAction, applyAction) //$NON-NLS-1$
				.getContainer(), BorderLayout.SOUTH);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 310);
		SwingTools.centerOnScreen(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(final WindowEvent e) {
				validated = false;
				doLayout();
			}

			@Override
			public void windowClosed(final WindowEvent e) {
				restore();
			}

		});
	}

	/**
	 * @return
	 */
	// public FunctionFactory createFunctionFactory(final Random r) {
	// final FunctionFactory f;
	// switch (function) {
	// case PLAN:
	// f = new PlanFunctionFactory(xSlopeRandomizer.createRandomizer(r),
	// zSlopeRandomizer.createRandomizer(r));
	// break;
	// case PIRAMID:
	// f = new PyramidFunctionFactory(widthParm,
	// heightRandomizer.createRandomizer(r));
	// break;
	// case EXPONENTIAL:
	// f = new ExpFunctionFactory(widthParm / 2,
	// heightRandomizer.createRandomizer(r));
	// break;
	// case BOX:
	// f = new BoxFunctionFactory(widthParm,
	// heightRandomizer.createRandomizer(r));
	// break;
	// case CYLINDER:
	// f = new CylinderFunctionFactory(widthParm / 2,
	// heightRandomizer.createRandomizer(r));
	// break;
	// case CONE:
	// f = new ConeFunctionFactory(widthParm / 2,
	// heightRandomizer.createRandomizer(r));
	// break;
	// case HEMISPHERE:
	// f = new HemisphereFunctionFactory(widthParm / 2,
	// heightRandomizer.createRandomizer(r));
	// break;
	// case SINC:
	// f = new SincFunctionFactory(widthParm / 2,
	// heightRandomizer.createRandomizer(r));
	// break;
	// default:
	// f = new GaussianFunctionFactory(widthParm,
	// heightRandomizer.createRandomizer(r));
	// break;
	// }
	// return f;
	// }

	/**
	 * @return
	 */
	private JPanel createPlanPane() {
		return new GridLayoutHelper<>(new JPanel()).modify("insets,5") //$NON-NLS-1$
				.add("+hspan", xSlopeRandomizer, zSlopeRandomizer) //$NON-NLS-1$
				.getContainer();
	}

	/**
	 * @return
	 * 
	 */
	private JPanel createWidthPane() {
		return new GridLayoutHelper<>(Messages.RESOURCE_BUNDLE, new JPanel())
				.modify("insets,5 w") //$NON-NLS-1$
				.add("FunctionDialog.width.text", //$NON-NLS-1$
						"+hspan", //$NON-NLS-1$
						SwingTools.createNumberSpinner(widthModel, "#,##0.00", //$NON-NLS-1$
								5), "+hspan", heightRandomizer).getContainer(); //$NON-NLS-1$
	}

	/**
	 * 
	 */
	private void restore() {
		widthModel.setValue(widthParm);
		heightRandomizer.restore();
		xSlopeRandomizer.restore();
		zSlopeRandomizer.restore();
	}

	/**
	 * @param function
	 */
	public void setFunction(final Function function) {
		this.function = function;
	}

	/**
	 * 
	 * @return
	 */
	public boolean showDialog(final Function function) {
		this.function = function;
		final Container c = getContentPane();
		c.remove(planPane);
		c.remove(widthPane);
		c.add(function.equals(Function.PLAN) ? planPane : widthPane,
				BorderLayout.CENTER);
		setTitle(Messages.getString("FunctionDialog." + function + ".title")); //$NON-NLS-1$ //$NON-NLS-2$
		doLayout();
		setVisible(true);
		return validated;
	}
}
