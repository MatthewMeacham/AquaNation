package com.aquanation.water.windows.analysis;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.aquanation.water.State;
import com.aquanation.water.AquaNation;
import com.aquanation.water.windows.MainFrame;

/**
 * 
 * @author Matthew Meacham
 * 
 * The frame that will contain the analysis for the entire map
 *
 */
public class MapAnalysisFrame extends JFrame {
	private static final long serialVersionUID = 9101039930306871381L;

	private final String TITLE = "Map Analysis Tools";

	private JPanel contentPane;
	private JTabbedPane tabbedPane;

	private MapHistogramPanel mapHistogramPanel;
	private MapBoxAndWhiskerPlotPanel mapBoxAndWhiskerPlotPanel;
	private MapValuesPanel mapValuesPanel;
	private MapStatisticsPanel mapStatisticsPanel;

	private int width = 550;
	private int height = 450;

	private String dataType;

	private Thread thread;

	private final int HISTOGRAM_INDEX = 0;
	private final int BOX_AND_WHISKER_PLOT_INDEX = 1;
	private final int VALUES_INDEX = 2;
	private final int STATISTICS_INDEX = 3;

	public MapAnalysisFrame() {
		dataType = MainFrame.getSelectedDataType();

		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, width, height);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		mapHistogramPanel = new MapHistogramPanel(this);
		mapBoxAndWhiskerPlotPanel = new MapBoxAndWhiskerPlotPanel(this);
		mapValuesPanel = new MapValuesPanel(this);
		mapStatisticsPanel = new MapStatisticsPanel(this);

		tabbedPane.addTab("Histogram", null, mapHistogramPanel.getContentPane(), null);
		tabbedPane.addTab("Box Plot", null, mapBoxAndWhiskerPlotPanel.getContentPane(), null);
		tabbedPane.addTab("Values", null, mapValuesPanel.getContentPane(), null);
		tabbedPane.addTab("Statistics", null, mapStatisticsPanel.getContentPane(), null);

		JPanel inputPanel = new JPanel();
		contentPane.add(inputPanel, BorderLayout.SOUTH);
		inputPanel.setLayout(new GridLayout(0, 5, 0, 0));

		//TODO this is actually supposed to be a print button, but we ran out of time
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		inputPanel.add(backButton);

		Component glue1 = Box.createGlue();
		inputPanel.add(glue1);

		State state = AquaNation.getStates().get(0);
		String[] dataTypes = new String[state.getWaterValues().size()];
		for (int i = 0; i < state.getWaterValues().size(); i++) {
			dataTypes[i] = state.getWaterValues().get(i).getDataType();
		}
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>(dataTypes);
		JComboBox<String> dataTypeComboBox = new JComboBox<String>();
		dataTypeComboBox.setModel(comboBoxModel);
		dataTypeComboBox.setSelectedItem(dataType);
		dataTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataType = (String) dataTypeComboBox.getSelectedItem();
				mapHistogramPanel.setHistogramNeedsToChange(true);
				mapStatisticsPanel.resetStatistics();
				mapBoxAndWhiskerPlotPanel.resetStatistics();
				repaint();
			}
		});
		inputPanel.add(dataTypeComboBox);
		Component glue2 = Box.createGlue();
		inputPanel.add(glue2);

		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		inputPanel.add(doneButton);

		thread = new Thread(new Runnable() {
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

			if (tabbedPane.getSelectedIndex() == HISTOGRAM_INDEX) {
				mapHistogramPanel.render();
			} else if (tabbedPane.getSelectedIndex() == BOX_AND_WHISKER_PLOT_INDEX) {
				mapBoxAndWhiskerPlotPanel.render();
			} else if (tabbedPane.getSelectedIndex() == VALUES_INDEX) {
				mapValuesPanel.render();
			} else if (tabbedPane.getSelectedIndex() == STATISTICS_INDEX) {
				mapStatisticsPanel.render();
			}

		}
	}

	public String getDataType() {
		return dataType;
	}

}
