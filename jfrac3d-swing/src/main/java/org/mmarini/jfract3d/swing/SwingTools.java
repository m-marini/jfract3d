/**
 * 
 */
package org.mmarini.jfract3d.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.SpinnerNumberModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author us00852
 * 
 */
public class SwingTools {
	private static final Logger logger = LoggerFactory
			.getLogger(SwingTools.class);
	private static File optionFile;
	private static Properties options;

	/**
	 * 
	 * @return
	 */
	public static void setOptionsFile(final String name) {
		optionFile = new File(System.getProperty("user.home"), name);
	}

	/**
	 * 
	 */
	public static void installLookAndFeel() {
		try {
			final Properties p = getOptions();
			final String c = p.getProperty("lookAndFeelClass", //$NON-NLS-1$
					UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(c);
			logger.debug("Set Look&Feel to {}", c); //$NON-NLS-1$
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 */
	public static void saveLookAndFeel() {
		final Properties p = getOptions();
		p.setProperty("lookAndFeelClass", UIManager.getLookAndFeel().getClass()
				.getName());
		saveOptions();
	}

	/**
	 * 
	 * @return
	 */
	public static Properties getOptions() {
		if (options == null) {
			options = new Properties();
			if (optionFile != null) {
				try {
					final FileInputStream in = new FileInputStream(optionFile);
					options.loadFromXML(in);
					in.close();
				} catch (final FileNotFoundException e) {
					logger.error(e.getMessage(), e);
					saveOptions();
				} catch (final IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return options;
	}

	/**
	 * 
	 * @return
	 */
	public static void saveOptions() {
		if (optionFile != null) {
			try {
				final FileOutputStream out = new FileOutputStream(optionFile);
				options.storeToXML(out, null, "UTF8"); //$NON-NLS-1$
				out.close();
				logger.debug("Saved {}", optionFile); //$NON-NLS-1$				
			} catch (final IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

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
