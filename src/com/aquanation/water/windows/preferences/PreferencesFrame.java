package com.aquanation.water.windows.preferences;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The frame that will contain the {@link ColorPanel} and the {@link FontPanel}
 *
 */
public class PreferencesFrame extends JFrame {
	private static final long serialVersionUID = 2000919243125825055L;

	private JPanel contentPane;

	private int width = 640;
	private int height = 350;

	public PreferencesFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, width, height);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		ColorPanel colorFrame = new ColorPanel(this);
		FontPanel fontFrame = new FontPanel(this);

		tabbedPane.add("Colors", colorFrame.getColorContentPane());
		tabbedPane.add("Text", fontFrame.getFontContentPane());

		setVisible(true);
	}

}
