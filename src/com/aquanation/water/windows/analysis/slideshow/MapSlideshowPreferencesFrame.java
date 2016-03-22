package com.aquanation.water.windows.analysis.slideshow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.aquanation.water.listeners.DoubleOnlyListener;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The frame that contains the preferences for the map slideshow
 *
 */
public class MapSlideshowPreferencesFrame extends JFrame {
	private static final long serialVersionUID = 2581379892080618091L;

	private JPanel contentPane;
	private JTextField durationTextField;

	private int width = 450;
	private int height = 300;

	private String TITLE = "Map Slideshow Preferences";

	private final long NUMBER_OF_MILLIS_IN_A_SECOND = 1000L;

	public MapSlideshowPreferencesFrame() {
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, width, height);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel inputPanel = new JPanel();
		contentPane.add(inputPanel, BorderLayout.CENTER);
		inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setPreferredSize(new Dimension(32767, 100));
		horizontalGlue.setMaximumSize(new Dimension(32767, 32767));
		inputPanel.add(horizontalGlue);

		JLabel durationOfEachLabel = new JLabel("Duration of each slideshow frame: ");
		inputPanel.add(durationOfEachLabel);

		durationTextField = new JTextField();
		durationOfEachLabel.setLabelFor(durationTextField);
		durationTextField.setText(String.valueOf(MapSlideshowFrame.getSlideshowFrameDuration() / 1000.0d));
		durationTextField.getDocument().addDocumentListener(new DoubleOnlyListener(durationTextField));
		inputPanel.add(durationTextField);
		durationTextField.setColumns(10);

		JLabel secondsLabel = new JLabel(" second(s)");
		inputPanel.add(secondsLabel);

		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new GridLayout(0, 3, 0, 0));

		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(backButton);

		Component glue = Box.createGlue();
		buttonPanel.add(glue);

		JButton doneButton = new JButton("Save");
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (durationTextField.getText().trim().isEmpty()) {
					new Thread(new Runnable() {
						public void run() {
							durationTextField.setBackground(Color.RED);
							long startTime = System.currentTimeMillis();
							while (startTime + 1500 > System.currentTimeMillis()) {
								// Do nothing
							}
							durationTextField.setBackground(Color.WHITE);
						}
					}).start();
					return;
				}
				MapSlideshowFrame.setSlideshowFrameDuration((long) (Double.parseDouble(durationTextField.getText().trim()) * NUMBER_OF_MILLIS_IN_A_SECOND));
				dispose();
			}
		});
		buttonPanel.add(doneButton);

		setVisible(true);
	}

}
