/**
 * 
 */
package org.mmarini.jfract3d.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.SpinnerNumberModel;

/**
 * @author us00852
 * 
 */
public class SwingTools {

	/**
	 * 
	 * @param c
	 */
	public static void centerOnScreen(final Component c) {
		final Dimension s = c.getSize();
		final Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		c.setLocation((ss.width - s.width) / 2, (ss.height - s.height) / 2);
	}

	/**
	 * 
	 * @param fmt
	 * @param size
	 * @param s
	 */
	public static JSpinner createSpinner(final SpinnerNumberModel m,
			final String fmt, final int size) {
		final JSpinner s = new JSpinner(m);
		final NumberEditor e = new JSpinner.NumberEditor(s, fmt);
		e.getTextField().setColumns(size);
		s.setEditor(e);
		return s;
	}

}
