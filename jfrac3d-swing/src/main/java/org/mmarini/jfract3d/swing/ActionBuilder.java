/**
 * 
 */
package org.mmarini.jfract3d.swing;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.net.URL;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author US00852
 * 
 */
public class ActionBuilder {
	private static final Logger logger = LoggerFactory
			.getLogger(ActionBuilder.class);
	private final ChangeListener lookAndFeelListener;

	private final Component[] comps;

	/**
	 * 
	 */
	public ActionBuilder() {
		this(null);
	}

	/**
	 * @param lookAndFeelListener
	 */
	public ActionBuilder(final ChangeListener lookAndFeelListener,
			final Component... comps) {
		this.comps = comps;
		this.lookAndFeelListener = lookAndFeelListener;
	}

	/**
	 * 
	 * @return
	 */
	public JToolBar createHorizontalToolBar(final Action... actions) {
		final JToolBar tb = new JToolBar();
		for (final Action a : actions)
			if (a != null)
				tb.add(a);
			else
				tb.add(new JSeparator(SwingConstants.VERTICAL));
		return tb;
	}

	/**
	 * @return
	 */
	private JMenu createLFMenu() {
		final JMenu mi = createMenu("lookAndFeel");
		final ButtonGroup g = new ButtonGroup();
		final ChangeListener l = new ChangeListener() {

			@Override
			public void stateChanged(final ChangeEvent e) {
				final ButtonModel s = (ButtonModel) e.getSource();
				final String lf = UIManager.getLookAndFeel().getClass()
						.getName();
				final String c = s.getActionCommand();
				if (s.isSelected() && !lf.equals(c)) {
					try {
						UIManager.setLookAndFeel(c);
						for (final Component c1 : comps)
							SwingUtilities.updateComponentTreeUI(c1);
						logger.debug("Set look&feel to {}", c);
						if (lookAndFeelListener != null)
							lookAndFeelListener.stateChanged(e);
					} catch (ClassNotFoundException | InstantiationException
							| IllegalAccessException
							| UnsupportedLookAndFeelException e1) {
						logger.error(e1.getMessage(), e);
					}
				}
			}
		};
		final String scn = UIManager.getLookAndFeel().getClass().getName();
		ButtonModel sm = null;
		for (final LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels()) {
			final JRadioButtonMenuItem mlf = new JRadioButtonMenuItem(
					lf.getName());

			final ButtonModel m = mlf.getModel();
			final String cn = lf.getClassName();
			m.setActionCommand(cn);
			m.addChangeListener(l);
			if (cn == scn)
				sm = m;
			mi.add(mlf);
			g.add(mlf);
		}
		g.setSelected(sm, true);
		return mi;
	}

	/**
	 * @param id
	 * @return
	 */
	private JMenu createMenu(final String id) {
		final JMenu m = new JMenu(Messages.getString(String.format(
				"ActionBuilder.%s.name", id))); //$NON-NLS-1$

		final String ak = Messages.getString(String.format(
				"ActionBuilder.%s.acceleratorKey", id)); //$NON-NLS-1$
		if (!ak.startsWith("!")) //$NON-NLS-1$
			m.setAccelerator(KeyStroke.getKeyStroke(ak));

		final String mk = Messages.getString(String.format(
				"ActionBuilder.%s.menmonicKey", id)); //$NON-NLS-1$
		if (!mk.startsWith("!")) //$NON-NLS-1$
			m.setMnemonic(KeyEvent.getExtendedKeyCodeForChar(mk.charAt(0)));

		final String si = Messages.getString(String.format(
				"ActionBuilder.%s.smallIcon", id)); //$NON-NLS-1$
		if (!si.startsWith("!")) { //$NON-NLS-1$
			final URL url = getClass().getResource(si);
			if (url != null)
				m.setIcon(new ImageIcon(url));
		}

		final String sd = Messages.getString(String.format(
				"ActionBuilder.%s.shortDescription", id)); //$NON-NLS-1$
		if (!sd.startsWith("!")) //$NON-NLS-1$
			m.setToolTipText(sd);
		return m;
	}

	/**
	 * 
	 * @return
	 */
	public JMenuBar createMenuBar(final Object... defs) {
		final JMenuBar mb = new JMenuBar();
		JMenu m = null;
		for (final Object d : defs) {
			if (d == null)
				m.add(new JSeparator());
			else if (d instanceof Action)
				m.add((Action) d);
			else if ("lookAndFeel".equals(d))
				m.add(createLFMenu());
			else if (d instanceof String) {
				m = createMenu(d.toString());
				mb.add(m);
			}
		}
		return mb;
	}

	public <A extends Action> A setUp(final A a, final String id) {
		a.putValue(Action.NAME,
				Messages.getString(String.format("ActionBuilder.%s.name", id))); //$NON-NLS-1$

		final String ak = Messages.getString(String.format(
				"ActionBuilder.%s.acceleratorKey", id)); //$NON-NLS-1$
		if (!ak.startsWith("!")) //$NON-NLS-1$
			a.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(ak));

		final String mk = Messages.getString(String.format(
				"ActionBuilder.%s.menmonicKey", id)); //$NON-NLS-1$
		if (!mk.startsWith("!")) //$NON-NLS-1$
			a.putValue(Action.MNEMONIC_KEY,
					KeyEvent.getExtendedKeyCodeForChar(mk.charAt(0)));

		final String si = Messages.getString(String.format(
				"ActionBuilder.%s.smallIcon", id)); //$NON-NLS-1$
		if (!si.startsWith("!")) { //$NON-NLS-1$
			final URL url = getClass().getResource(si);
			if (url != null)
				a.putValue(Action.SMALL_ICON, new ImageIcon(url));
		}

		final String li = Messages.getString(String.format(
				"ActionBuilder.%s.largeIcon", id)); //$NON-NLS-1$
		if (!li.startsWith("!")) { //$NON-NLS-1$
			final URL url = getClass().getResource(li);
			if (url != null)
				a.putValue(Action.LARGE_ICON_KEY, new ImageIcon(url));
		}

		final String sd = Messages.getString(String.format(
				"ActionBuilder.%s.shortDescription", id)); //$NON-NLS-1$
		if (!sd.startsWith("!")) //$NON-NLS-1$
			a.putValue(Action.SHORT_DESCRIPTION, sd);

		final String ld = Messages.getString(String.format(
				"ActionBuilder.%s.longDescription", id)); //$NON-NLS-1$
		if (!ld.startsWith("!")) //$NON-NLS-1$
			a.putValue(Action.LONG_DESCRIPTION, ld);
		return a;
	}
}
