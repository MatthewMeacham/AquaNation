package com.aquanation.water.windows.analysis.slideshow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.aquanation.water.State;
import com.aquanation.water.AquaNation;
import com.aquanation.water.windows.MainFrame;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The frame that contains the slideshow for the map
 *
 */
public class MapSlideshowFrame extends JFrame {
	private static final long serialVersionUID = -7534874433632215709L;

	private JPanel contentPane;
	private JPanel imagePanel;

	private int width = 1000;
	private int height = 600;
	private int index = 0;

	private String dataType;

	private Thread thread;

	private final MapSlideshowActionListener mapSlideshowActionListener = new MapSlideshowActionListener();

	private ImageIcon imageIcon;
	private JLabel imageLabel;

	private JSlider positionSlider;

	private static long duration = 750L;

	private boolean showStateNames = true;
	private boolean showWaterValues = true;
	private boolean doingSlideshow = false;

	public MapSlideshowFrame() {
		dataType = MainFrame.getSelectedDataType();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, width, height);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu optionsMenu = new JMenu("Options");
		menuBar.add(optionsMenu);

		JMenuItem preferencesMenuItem = new JMenuItem("Preferences");
		preferencesMenuItem.setActionCommand("preferences");
		preferencesMenuItem.addActionListener(mapSlideshowActionListener);
		optionsMenu.add(preferencesMenuItem);

		JMenu viewMenu = new JMenu("View");
		menuBar.add(viewMenu);

		JCheckBoxMenuItem stateNamesCheckBox = new JCheckBoxMenuItem("Show State Names");
		stateNamesCheckBox.setActionCommand("showStateNames");
		stateNamesCheckBox.setSelected(true);
		stateNamesCheckBox.addActionListener(mapSlideshowActionListener);
		viewMenu.add(stateNamesCheckBox);

		JCheckBoxMenuItem waterValuesCheckBox = new JCheckBoxMenuItem("Show Water Values");
		waterValuesCheckBox.setActionCommand("showWaterValues");
		waterValuesCheckBox.setSelected(true);
		waterValuesCheckBox.addActionListener(mapSlideshowActionListener);
		viewMenu.add(waterValuesCheckBox);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		imagePanel = new JPanel();
		contentPane.add(imagePanel, BorderLayout.CENTER);

		imageIcon = new ImageIcon(MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage(), dataType, index));
		imageLabel = new JLabel(imageIcon);

		setFullMapImage(MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage(), dataType, index));

		imagePanel.add(imageLabel);

		JPanel inputPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) inputPanel.getLayout();
		flowLayout.setHgap(50);
		contentPane.add(inputPanel, BorderLayout.SOUTH);

		JButton backwardButton = new JButton("Backward");
		backwardButton.setActionCommand("backward");
		backwardButton.setPreferredSize(new Dimension(103, 23));
		backwardButton.setMinimumSize(new Dimension(103, 23));
		backwardButton.setMaximumSize(new Dimension(103, 23));
		backwardButton.setHorizontalTextPosition(SwingConstants.CENTER);
		backwardButton.addActionListener(mapSlideshowActionListener);

		State state = AquaNation.getStates().get(0);
		String[] dataTypes = new String[state.getWaterValues().size()];
		for (int i = 0; i < state.getWaterValues().size(); i++) {
			dataTypes[i] = state.getWaterValues().get(i).getDataType();
		}
		DefaultComboBoxModel<String> dataTypeComboBoxModel = new DefaultComboBoxModel<String>(dataTypes);
		JComboBox<String> dataTypeComboBox = new JComboBox<String>();
		dataTypeComboBox.setModel(dataTypeComboBoxModel);
		dataTypeComboBox.setSelectedItem(dataType);
		dataTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataType = (String) dataTypeComboBox.getSelectedItem();
				positionSlider.setValue(0);
				positionSlider.setMaximum((getMaxSize() - 1 >= 0) ? getMaxSize() - 1 : 0);
				setFullMapImage(MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage(), dataType, index));
				repaint();
			}
		});
		inputPanel.add(dataTypeComboBox);
		inputPanel.add(backwardButton);

		positionSlider = new JSlider();
		positionSlider.setMaximum((getMaxSize() - 1 >= 0) ? getMaxSize() - 1 : 0);
		positionSlider.setMajorTickSpacing(1);
		positionSlider.setSnapToTicks(true);
		positionSlider.setPaintTicks(true);
		positionSlider.setPaintLabels(true);
		positionSlider.setValue(0);
		positionSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				index = positionSlider.getValue();
				setFullMapImage(MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage(), dataType, index));
				repaint();
				;
			}
		});
		inputPanel.add(positionSlider);

		JButton forwardButton = new JButton("Forward");
		forwardButton.setActionCommand("forward");
		forwardButton.setMaximumSize(new Dimension(103, 23));
		forwardButton.setMinimumSize(new Dimension(103, 23));
		forwardButton.setPreferredSize(new Dimension(103, 23));
		forwardButton.setHorizontalTextPosition(SwingConstants.CENTER);
		forwardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		forwardButton.addActionListener(mapSlideshowActionListener);
		inputPanel.add(forwardButton);

		JButton startSlideshowButton = new JButton("Start Slideshow");
		startSlideshowButton.setActionCommand("startSlideshow");
		startSlideshowButton.addActionListener(mapSlideshowActionListener);
		inputPanel.add(startSlideshowButton);

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				render();
			}
		});
		thread.start();

		setVisible(true);
	}

	private void render() {
		while (true) {
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Graphics g = imagePanel.getGraphics();
			if (g == null) continue;

			g.setColor(AquaNation.getFontColor());
			g.setFont(AquaNation.getFont());
			// I use +172 in the x-direction, and -93 in the y-direction to offset the state names and water values to account for
			// The differences between the main frame and this one
			for (State state : AquaNation.getStates()) {
				if (showStateNames) g.drawString(state.getPostalCode(), state.getPostalCodePoint().x + 172, state.getPostalCodePoint().y - 93);

				if (showWaterValues && state.getWaterValues().size() > 0) {
					if (state.getWaterValues(MainFrame.getSelectedDataType()).size() > index) g.drawString(String.valueOf(state.getWaterValues(MainFrame.getSelectedDataType()).get(index)), state.getWaterValuePoint().x + 172, state.getWaterValuePoint().y - 93);
					else g.drawString("null", state.getWaterValuePoint().x + 172, state.getWaterValuePoint().y - 93);
				} else if (showWaterValues) {
					g.drawString("null", state.getWaterValuePoint().x + 172, state.getWaterValuePoint().y - 93);
				}
			}
		}
	}

	private void setFullMapImage(final Image image) {
		if (image == null) return;

		imageIcon.setImage(image);
		imageLabel.setIcon(imageIcon);
		repaint();
	}

	private int getMaxSize() {
		int maxSize = 0;
		for (State state : AquaNation.getStates()) {
			if (state.getWaterValues(dataType).size() > maxSize) maxSize = state.getWaterValues(dataType).size();
		}
		return maxSize;
	}

	private void slideshow() {
		if (!doingSlideshow) {
			new Thread(new Runnable() {
				public void run() {
					while (index < getMaxSize() - 1) {
						try {
							Thread.sleep(duration);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						index++;
						positionSlider.setValue(index);
						setFullMapImage(MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage(), dataType, index));
					}
					doingSlideshow = false;
				}
			}).start();
			doingSlideshow = true;
		}
	}

	private class MapSlideshowActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int maxSize = getMaxSize();

			switch (e.getActionCommand()) {
			case "forward":
				index++;
				if (index >= maxSize) {
					index = maxSize - 1;
				}
				positionSlider.setValue(index);
				break;
			case "backward":
				index--;
				if (index < 0) {
					index = 0;
				}
				positionSlider.setValue(index);
				break;
			case "startSlideshow":
				slideshow();
				break;
			case "preferences":
				new MapSlideshowPreferencesFrame();
				break;
			case "showStateNames":
				showStateNames = (showStateNames) ? false : true;
				break;
			case "showWaterValues":
				showWaterValues = (showWaterValues) ? false : true;
				break;
			}
			setFullMapImage(MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage(), dataType, index));
		}
	}

	public static long getSlideshowFrameDuration() {
		return duration;
	}

	public static void setSlideshowFrameDuration(final long duration2) {
		duration = duration2;
	}

}
