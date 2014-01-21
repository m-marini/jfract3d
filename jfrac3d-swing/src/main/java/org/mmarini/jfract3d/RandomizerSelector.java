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
	private static final long serialVersionUID = -1474533626768416461L;
	private final SpinnerNumberModel avgModel;
	private final SpinnerNumberModel rangeModel;
	private final SpinnerNumberModel probModel;
	private final JComboBox<String> randomizerSelector;

	private double average;
	private double range;
	private double prob;
	private int index;

	/**
	 * 
	 */
	public RandomizerSelector(final double average, final double range,
			final double probPositive) {
		this.average = average;
		this.range = range;
		this.prob = probPositive;

		avgModel = new SpinnerNumberModel(average, null, null, 0.01);
		rangeModel = new SpinnerNumberModel(range, 0.0, null, 0.01);
		probModel = new SpinnerNumberModel(probPositive, 0.0, 1.0, 0.01);

		randomizerSelector = new JComboBox<String>(
				"Gaussian,Linear,None".split(","));
		new GridLayoutHelper<>(Messages.RESOURCE_BUNDLE, this).modify(
				"insets,2 w").add("RandomizerSelector.function.text",
				randomizerSelector, "RandomizerSelector.average.text",
				"+hspan",
				SwingTools.createNumberSpinner(avgModel, "###0.00", 6),
				"RandomizerSelector.probability.text",
				SwingTools.createNumberSpinner(probModel, "###0.0%", 6),
				"RandomizerSelector.range.text", "+hspan",
				SwingTools.createNumberSpinner(rangeModel, "###0.00", 6));
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
		final double p = probModel.getNumber().doubleValue();
		switch (randomizerSelector.getSelectedIndex()) {
		case 0:
			rz = new GaussRandomizer(r, a, s, p);
			break;
		case 1:
			rz = new LinearRandomizer(r, a, s, p);
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
		probModel.setValue(prob);
		randomizerSelector.setSelectedIndex(index);
	}

	/**
	 * 
	 */
	public void apply() {
		average = avgModel.getNumber().doubleValue();
		range = rangeModel.getNumber().doubleValue();
		prob = probModel.getNumber().doubleValue();
		index = randomizerSelector.getSelectedIndex();
	}
}
