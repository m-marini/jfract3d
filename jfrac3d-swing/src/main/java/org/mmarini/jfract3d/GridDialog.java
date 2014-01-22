/**
 * 
 */
package org.mmarini.jfract3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import org.mmarini.swing.ActionBuilder;
import org.mmarini.swing.GridLayoutHelper;
import org.mmarini.swing.SwingTools;

/**
 * @author us00852
 * 
 */
public class GridDialog extends JDialog {
	private static final long serialVersionUID = 8388879557737014394L;

	private double xMin;
	private double xMax;
	private double zMin;
	private double zMax;
	private boolean validated;
	private final SpinnerNumberModel xMinModel;
	private final SpinnerNumberModel xMaxModel;
	private final SpinnerNumberModel zMinModel;
	private final SpinnerNumberModel zMaxModel;
	private JLabel xError;
	private JLabel zError;

	/**
	 * 
	 * @param owner
	 * @param xMin
	 * @param xMax
	 * @param zMin
	 * @param zMax
	 */
	public GridDialog(final Frame owner, final double xMin, final double xMax,
			final double zMin, final double zMax) {
		super(owner, Messages.getString("GridDialog.title"), true); //$NON-NLS-1$
		this.xMin = xMin;
		this.xMax = xMax;
		this.zMin = zMin;
		this.zMax = zMax;
		xMinModel = new SpinnerNumberModel(xMin, -10, 10, 0.1);
		xMaxModel = new SpinnerNumberModel(xMax, -10, 10, 0.1);
		zMinModel = new SpinnerNumberModel(zMin, -10, 10, 0.1);
		zMaxModel = new SpinnerNumberModel(zMax, -10, 10, 0.1);
		xError = new JLabel();
		zError = new JLabel();

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
				if (validate()) {
					final double x0 = xMinModel.getNumber().doubleValue();
					final double xm = xMaxModel.getNumber().doubleValue();
					final double z0 = zMinModel.getNumber().doubleValue();
					final double zm = zMaxModel.getNumber().doubleValue();
					GridDialog.this.xMin = x0;
					GridDialog.this.xMax = xm;
					GridDialog.this.zMin = z0;
					GridDialog.this.zMax = zm;
					dispose();
				}
			}

			private boolean validate() {
				final double x0 = xMinModel.getNumber().doubleValue();
				final double xm = xMaxModel.getNumber().doubleValue();
				final double z0 = zMinModel.getNumber().doubleValue();
				final double zm = zMaxModel.getNumber().doubleValue();
				validated = true;
				if (x0 >= xm) {
					xError.setText(String.format(
							Messages.getString("GridDialog.xError.text"), x0, xm)); //$NON-NLS-1$
					validated = false;
				}
				if (z0 >= zm) {
					zError.setText(String.format(
							Messages.getString("GridDialog.zError.text"), z0, zm)); //$NON-NLS-1$
					validated = false;
				}
				return validated;
			}

		};

		final ActionBuilder b = ActionBuilder.create(Messages.RESOURCE_BUNDLE);
		b.setUp(cancelAction, "cancel"); //$NON-NLS-1$
		b.setUp(restoreAction, "restore"); //$NON-NLS-1$
		b.setUp(applyAction, "apply"); //$NON-NLS-1$

		xError.setForeground(Color.RED);
		zError.setForeground(Color.RED);

		createContent(cancelAction, restoreAction, applyAction);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(500, 180);
		SwingTools.centerOnScreen(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(final WindowEvent e) {
				restore();
			}

			@Override
			public void windowActivated(final WindowEvent e) {
				validated = false;
			}

		});
	}

	/**
	 * 
	 */
	private void restore() {
		xMinModel.setValue(GridDialog.this.xMin);
		xMaxModel.setValue(GridDialog.this.xMax);
		zMinModel.setValue(GridDialog.this.zMin);
		zMaxModel.setValue(GridDialog.this.zMax);
		xError.setText(""); //$NON-NLS-1$
		zError.setText(""); //$NON-NLS-1$
	}

	/**
	 * 
	 * @param cancelAction
	 * @param restoreAction
	 * @param applyAction
	 */
	private void createContent(final AbstractAction cancelAction,
			final AbstractAction restoreAction, final AbstractAction applyAction) {
		final Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(new GridLayoutHelper<>(Messages.RESOURCE_BUNDLE, new JPanel())
				.modify("insets,2,5") //$NON-NLS-1$
				.add("GridDialog.xRange.text", //$NON-NLS-1$
						SwingTools.createNumberSpinner(xMinModel, "#,##0.0", 5), //$NON-NLS-1$
						"GridDialog.to.text", //$NON-NLS-1$
						SwingTools.createNumberSpinner(xMaxModel, "#,##0.0", 5), //$NON-NLS-1$
						"+w hw hspan", //$NON-NLS-1$
						xError,
						"GridDialog.zRange.text", //$NON-NLS-1$
						SwingTools.createNumberSpinner(zMinModel, "#,##0.0", 5), //$NON-NLS-1$
						"GridDialog.to.text", //$NON-NLS-1$
						SwingTools.createNumberSpinner(zMaxModel, "#,##0.0", 5), //$NON-NLS-1$
						"+w hw hspan", zError).getContainer(), //$NON-NLS-1$
				BorderLayout.CENTER);
		c.add(new GridLayoutHelper<>(new JPanel()).modify("insets,10") //$NON-NLS-1$
				.add("+e hw", new JButton(cancelAction), //$NON-NLS-1$
						new JButton(restoreAction), new JButton(applyAction))
				.getContainer(), BorderLayout.SOUTH);
	}

	/**
	 * @return the xMin
	 */
	public double getxMin() {
		return xMin;
	}

	/**
	 * @return the xMax
	 */
	public double getxMax() {
		return xMax;
	}

	/**
	 * @return the zMin
	 */
	public double getzMin() {
		return zMin;
	}

	/**
	 * @return the zMax
	 */
	public double getzMax() {
		return zMax;
	}

	/**
	 * @return the gridValidated
	 */
	public boolean isValidated() {
		return validated;
	}

	/**
	 * 
	 * @return
	 */
	public boolean showDialog() {
		setVisible(true);
		return validated;
	}
}
