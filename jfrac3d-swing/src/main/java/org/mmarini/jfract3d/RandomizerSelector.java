/**
 * 
 */
package org.mmarini.jfract3d;

import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import org.mmarini.swing.GridLayoutHelper;
import org.mmarini.swing.SwingTools;

/**
 * @author us00852
 * 
 */
public class RandomizerSelector extends JPanel {
	public enum Type {
		GAUSSIAN, LINEAR, NONE
	}

	private static final long serialVersionUID = -1474533626768416461L;
	private final SpinnerNumberModel avgModel;
	private final SpinnerNumberModel rangeModel;
	private final JComboBox<String> randomizerSelector;

	private double average;
	private double range;
	private Type type;

	/**
	 * 
	 */
	public RandomizerSelector(final Type type, final double average,
			final double range) {
		this.average = average;
		this.range = range;
		this.type = type;

		avgModel = new SpinnerNumberModel(average, null, null, 0.01);
		rangeModel = new SpinnerNumberModel(range, 0.0, null, 0.01);

		randomizerSelector = new JComboBox<String>(
				"Gaussian,Linear,None".split(","));
		new GridLayoutHelper<>(Messages.RESOURCE_BUNDLE, this).modify(
				"insets,2 w").add("RandomizerSelector.function.text",
				randomizerSelector, "RandomizerSelector.average.text",
				SwingTools.createNumberSpinner(avgModel, "#,##0.00", 5),
				"RandomizerSelector.range.text", "+hspan",
				SwingTools.createNumberSpinner(rangeModel, "#,##0.00", 5));
		randomizerSelector.setSelectedIndex(0);
	}

	/**
	 * 
	 * @param r
	 * @return
	 */
	public Randomizer<Double> createRandomizer(final Random r) {
		final Randomizer<Double> rz;
		final double a = avgModel.getNumber().doubleValue();
		final double s = rangeModel.getNumber().doubleValue();
		switch (type) {
		case GAUSSIAN:
			rz = new GaussRandomizer(r, a, s);
			break;
		case LINEAR:
			rz = new LinearRandomizer(r, a, s);
			break;
		default:
			rz = new Randomizer<Double>() {
				@Override
				public Double next() {
					return a;
				}
			};
			break;
		}
		return rz;
	}

	/**
	 * 
	 */
	public void restore() {
		avgModel.setValue(average);
		rangeModel.setValue(range);
		randomizerSelector.setSelectedIndex(type.ordinal());
	}

	/**
	 * 
	 */
	public void apply() {
		average = avgModel.getNumber().doubleValue();
		range = rangeModel.getNumber().doubleValue();
		type = Type.values()[randomizerSelector.getSelectedIndex()];
	}
}
