/**
 * 
 */
package org.mmarini.jfract3d.swing;

import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

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

	/**
	 * 
	 */
	public RandomizerSelector(final double average, final double range,
			final double probPositive) {
		avgModel = new SpinnerNumberModel(average, null, null, 0.01);
		rangeModel = new SpinnerNumberModel(range, 0.0, null, 0.01);
		probModel = new SpinnerNumberModel(probPositive, 0.0, 1.0, 0.01);

		randomizerSelector = new JComboBox<String>(
				"None,Gaussian,Linear".split(","));
		new GridLayoutHelper<>(this).modify("insets,2 w").add(
				"RandomizerSelector.function.text", randomizerSelector,
				"RandomizerSelector.average.text", "+hspan",
				SwingTools.createSpinner(avgModel, "###0.00", 6),
				"RandomizerSelector.probability.text",
				SwingTools.createSpinner(probModel, "###0.0%", 6),
				"RandomizerSelector.range.text", "+hspan",
				SwingTools.createSpinner(rangeModel, "###0.00", 6));
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
		case 1:
			rz = new GaussRandomizer(r, a, s, p);
			break;
		case 2:
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
}
