package com.aquanation.water.windows;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.aquanation.water.State;
import com.aquanation.water.helpers.DataSet;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The frame that contains the graphics for a certain state
 *
 */
// TODO should encapsulate what is found within the render methods to their own classes because it's a pretty large class at the moment
public class StateHistoryFrame extends JFrame {
	private static final long serialVersionUID = -1007808556664502961L;

	private final String TITLE = "State History";

	private JPanel contentPane;
	private int width = 550;
	private int height = 450;

	private String dataType;

	private JTabbedPane tabbedPane;
	private JPanel barGraphPanel;
	private JPanel lineGraphPanel;
	private JPanel boxAndWhiskerPlotPanel;

	private boolean donePressed = false;
	private Thread thread;

	private State state;

	// These will need to be changed if the order changes
	private final int BAR_GRAPH = 0;
	private final int LINE_GRAPH = 1;
	private final int BOX_AND_WHISKER_PLOT = 2;

	private final int GRAPH_BORDER = 25;

	// For Line graph
	private final int DOT_WIDTH = 10;
	private final int DOT_HEIGHT = 10;

	// For table
	private JTable table;

	// For statistics
	private final String MEAN_STRING = "Mean: ";
	private final String SUMMATION_OF_VALUES_STRING = "Summation of values: ";
	private final String SUMMATION_OF_SQUARED_VALUES_STRING = "Summation of squared values: ";
	private final String STANDARD_DEVIATION_STRING = "Standard Deviation: ";
	private final String NUMBER_OF_VALUES_STRING = "Number of values: ";
	private final String MINIMUM_STRING = "Minimum: ";
	private final String FIRST_QUARTILE_STRING = "Q1 (First quartile): ";
	private final String MEDIAN_STRING = "Median: ";
	private final String THIRD_QUARTILE_STRING = "Q3 (Third quartile): ";
	private final String MAXIMUM_STRING = "Maximum: ";

	private JLabel meanLabel;
	private JLabel summationOfValuesLabel;
	private JLabel summationOfSquaredValuesLabel;
	private JLabel standardDeviationLabel;
	private JLabel numberOfValuesLabel;
	private JLabel minimumLabel;
	private JLabel firstQuartileLabel;
	private JLabel medianLabel;
	private JLabel thirdQuartileLabel;
	private JLabel maximumLabel;

	// This stuff is for the box and whisker plot
	private double lowerFence;
	private double upperFence;
	private double minimum;
	private double firstQuartile;
	private double median;
	private double thirdQuartile;
	private double maximum;
	private ArrayList<Double> lowerOutliers = new ArrayList<Double>();
	private ArrayList<Double> upperOutliers = new ArrayList<Double>();

	private final int BOX_AND_WHISKER_PLOT_HEIGHT_SIZE = 50;

	public StateHistoryFrame(State state) {
		this.state = state;

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

		barGraphPanel = new JPanel();
		tabbedPane.addTab("Bar Graph", null, barGraphPanel, null);

		lineGraphPanel = new JPanel();
		tabbedPane.addTab("Line Graph", null, lineGraphPanel, null);

		boxAndWhiskerPlotPanel = new JPanel();
		tabbedPane.addTab("Box Plot", null, boxAndWhiskerPlotPanel, null);

		JPanel tablePanel = new JPanel();
		tabbedPane.addTab("Data Table", null, tablePanel, null);
		tablePanel.setLayout(new BorderLayout(0, 0));

		JPanel statisticsPanel = new JPanel();
		tabbedPane.addTab("Statistics", null, statisticsPanel, null);
		statisticsPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		DefaultTableModel tableModel = new DefaultTableModel();
		for (DataSet dataSet : state.getWaterValues()) {
			Double[] values = new Double[dataSet.getValues().size()];
			for (int i = dataSet.getValues().size() - 1; i >= 0; i--) {
				values[values.length - i - 1] = dataSet.getValues().get(i);
			}
			tableModel.addColumn(dataSet.getDataType(), values);
		}

		table.setModel(tableModel);
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();
				TableModel model = (TableModel) e.getSource();

				Double value = Double.valueOf((String) model.getValueAt(row, column));
				String dataType = state.getWaterValues().get(column).getDataType();

				state.setWaterValue(dataType, value, (state.getWaterValues(dataType).size() - row));
				resetStatistics();
				calculateStatistics();
				MainFrame.getImagePanel().repaint();
				MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage());
			}
		});

		scrollPane.setViewportView(table);

		Component glue2 = Box.createGlue();
		statisticsPanel.add(glue2);

		meanLabel = new JLabel(MEAN_STRING);
		meanLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsPanel.add(meanLabel);

		summationOfValuesLabel = new JLabel(SUMMATION_OF_VALUES_STRING);
		summationOfValuesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsPanel.add(summationOfValuesLabel);

		summationOfSquaredValuesLabel = new JLabel(SUMMATION_OF_SQUARED_VALUES_STRING);
		summationOfSquaredValuesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsPanel.add(summationOfSquaredValuesLabel);

		standardDeviationLabel = new JLabel(STANDARD_DEVIATION_STRING);
		standardDeviationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsPanel.add(standardDeviationLabel);

		numberOfValuesLabel = new JLabel(NUMBER_OF_VALUES_STRING);
		numberOfValuesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsPanel.add(numberOfValuesLabel);

		minimumLabel = new JLabel(MINIMUM_STRING);
		minimumLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsPanel.add(minimumLabel);

		firstQuartileLabel = new JLabel(FIRST_QUARTILE_STRING);
		firstQuartileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsPanel.add(firstQuartileLabel);

		medianLabel = new JLabel(MEDIAN_STRING);
		medianLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsPanel.add(medianLabel);

		thirdQuartileLabel = new JLabel(THIRD_QUARTILE_STRING);
		thirdQuartileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsPanel.add(thirdQuartileLabel);

		maximumLabel = new JLabel(MAXIMUM_STRING);
		maximumLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statisticsPanel.add(maximumLabel);

		Component glue3 = Box.createGlue();
		statisticsPanel.add(glue3);

		calculateStatistics();

		// End Statistics Panel

		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		// TODO this is actually supposed to be a print button, but we don't have time
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPanel.add(backButton);

		Component glue1 = Box.createGlue();
		buttonPanel.add(glue1);

		JComboBox<String> comboBox = new JComboBox<String>();
		String[] dataTypes = new String[state.getWaterValues().size()];
		for (int i = 0; i < state.getWaterValues().size(); i++) {
			dataTypes[i] = state.getWaterValues().get(i).getDataType();
		}
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>(dataTypes);
		comboBox.setModel(comboBoxModel);
		comboBox.setSelectedItem(dataType);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataType = (String) comboBox.getSelectedItem();
				resetStatistics();
				calculateStatistics();
				repaint();
			}
		});
		buttonPanel.add(comboBox);

		Component glue = Box.createGlue();
		buttonPanel.add(glue);

		JButton doneButton = new JButton("Done");
		doneButton.setActionCommand("done");
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				donePressed = true;
				dispose();
				try {
					thread.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(doneButton);

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				render();
			}
		});
		thread.start();

		setVisible(true);
	}

	@SuppressWarnings("unchecked")
	private void render() {
		while (!donePressed) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {

			}

			if (state.getWaterValues(dataType).size() == 0) continue;

			int index = tabbedPane.getSelectedIndex();
			if (index == BAR_GRAPH) {
				Graphics g = barGraphPanel.getGraphics();
				if (g == null) continue;
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke(5));

				// Draw the borders to the bar graph
				g.drawLine(GRAPH_BORDER, barGraphPanel.getHeight() - GRAPH_BORDER, GRAPH_BORDER, GRAPH_BORDER);
				g.drawLine(GRAPH_BORDER, barGraphPanel.getHeight() - GRAPH_BORDER, barGraphPanel.getWidth() - GRAPH_BORDER, barGraphPanel.getHeight() - GRAPH_BORDER);
				// Max value line indicator
				g.drawLine(GRAPH_BORDER - 10, GRAPH_BORDER, GRAPH_BORDER, GRAPH_BORDER);
				// Middle value line indicator
				g.drawLine(GRAPH_BORDER - 10, (barGraphPanel.getHeight()) / 2, GRAPH_BORDER, barGraphPanel.getHeight() / 2);
				// Zero value line indicator
				g.drawLine(GRAPH_BORDER - 10, barGraphPanel.getHeight() - GRAPH_BORDER, GRAPH_BORDER, barGraphPanel.getHeight() - GRAPH_BORDER);
				// least recent line indicator
				g.drawLine(GRAPH_BORDER, barGraphPanel.getHeight() - GRAPH_BORDER, GRAPH_BORDER, barGraphPanel.getHeight() - GRAPH_BORDER + 10);
				// most recent line indicator
				g.drawLine(barGraphPanel.getWidth() - GRAPH_BORDER, barGraphPanel.getHeight() - GRAPH_BORDER, barGraphPanel.getWidth() - GRAPH_BORDER, barGraphPanel.getHeight() - GRAPH_BORDER + 10);

				// * 2 because we have to account for both borders
				int barGraphWidth = barGraphPanel.getWidth() - GRAPH_BORDER * 2;
				int barGraphHeight = barGraphPanel.getHeight() - GRAPH_BORDER * 2;
				List<Double> waterValues = state.getWaterValues(dataType);
				if (waterValues == null) continue;

				int singleBarWidth = barGraphWidth / waterValues.size() - 5;
				double maxValue = 0.0d;
				for (Double value : waterValues) {
					if (value > maxValue) maxValue = value;
				}

				g.setFont(new Font("Verdana", Font.PLAIN, 10));
				g.drawString(String.valueOf(maxValue), 2, GRAPH_BORDER - 5);
				g.drawString(String.valueOf(maxValue / 2), 2, barGraphPanel.getHeight() / 2 - 5);
				g.drawString("0", 2, barGraphPanel.getHeight() - GRAPH_BORDER - 5);
				g.drawString("Least recent", GRAPH_BORDER + 10, barGraphPanel.getHeight() - GRAPH_BORDER + 12);
				int mostRecentStringWidth = g2.getFontMetrics().stringWidth("Most recent");
				g.drawString("Most recent", barGraphPanel.getWidth() - GRAPH_BORDER - mostRecentStringWidth - 10, barGraphPanel.getHeight() - 10);

				g.setColor(Color.BLACK);
				for (int i = 0; i < waterValues.size(); i++) {
					int waterValueHeight = (int) (barGraphHeight * (waterValues.get(i) / maxValue));
					g.fillRect(GRAPH_BORDER + i * (singleBarWidth + 5), barGraphPanel.getHeight() - GRAPH_BORDER - waterValueHeight, singleBarWidth, waterValueHeight);
				}
			} else if (index == LINE_GRAPH) {
				Graphics g = lineGraphPanel.getGraphics();
				Graphics2D g2 = (Graphics2D) g;
				if (g == null) continue;
				g2.setStroke(new BasicStroke(5));

				// Draw the borders to the bar graph
				g.drawLine(GRAPH_BORDER, lineGraphPanel.getHeight() - GRAPH_BORDER, GRAPH_BORDER, GRAPH_BORDER);
				g.drawLine(GRAPH_BORDER, lineGraphPanel.getHeight() - GRAPH_BORDER, lineGraphPanel.getWidth() - GRAPH_BORDER, lineGraphPanel.getHeight() - GRAPH_BORDER);
				// Max value line indicator
				g.drawLine(GRAPH_BORDER - 10, GRAPH_BORDER, GRAPH_BORDER, GRAPH_BORDER);
				// Middle value line indicator
				g.drawLine(GRAPH_BORDER - 10, (lineGraphPanel.getHeight()) / 2, GRAPH_BORDER, lineGraphPanel.getHeight() / 2);
				// Zero value line indicator
				g.drawLine(GRAPH_BORDER - 10, lineGraphPanel.getHeight() - GRAPH_BORDER, GRAPH_BORDER, lineGraphPanel.getHeight() - GRAPH_BORDER);
				// Least recent line indicator
				g.drawLine(GRAPH_BORDER, lineGraphPanel.getHeight() - GRAPH_BORDER, GRAPH_BORDER, lineGraphPanel.getHeight() - GRAPH_BORDER + 10);
				// Most recent line indicator
				g.drawLine(lineGraphPanel.getWidth() - GRAPH_BORDER, lineGraphPanel.getHeight() - GRAPH_BORDER, lineGraphPanel.getWidth() - GRAPH_BORDER, lineGraphPanel.getHeight() - GRAPH_BORDER + 10);

				// * 2 because we have to account for both borders
				int lineGraphWidth = lineGraphPanel.getWidth() - GRAPH_BORDER * 2;
				int lineGraphHeight = lineGraphPanel.getHeight() - GRAPH_BORDER * 2;
				List<Double> waterValues = state.getWaterValues(dataType);
				if (waterValues == null) continue;

				if (waterValues.size() == 1) continue;
				int spaceBetweenDots = lineGraphWidth / (waterValues.size() - 1);
				double maxValue = 0.0d;
				for (Double value : waterValues) {
					if (value > maxValue) maxValue = value;
				}

				g.setFont(new Font("Verdana", Font.PLAIN, 10));
				g.drawString(String.valueOf(maxValue), 2, GRAPH_BORDER - 5);
				g.drawString(String.valueOf(maxValue / 2), 2, lineGraphPanel.getHeight() / 2 - 5);
				g.drawString("0", 2, lineGraphPanel.getHeight() - GRAPH_BORDER - 5);
				g.drawString("Least recent", GRAPH_BORDER + 10, lineGraphPanel.getHeight() - GRAPH_BORDER + 12);
				int mostRecentStringWidth = g2.getFontMetrics().stringWidth("Most recent");
				g.drawString("Most recent", lineGraphPanel.getWidth() - GRAPH_BORDER - mostRecentStringWidth - 10, lineGraphPanel.getHeight() - 10);

				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setStroke(new BasicStroke(3));
				g.setColor(Color.BLACK);
				for (int i = 0; i < waterValues.size(); i++) {
					int waterValueYPosition = (int) (lineGraphHeight * (waterValues.get(i) / maxValue));
					int dotX = GRAPH_BORDER + i * spaceBetweenDots;
					int dotY = lineGraphPanel.getHeight() - GRAPH_BORDER - waterValueYPosition;
					g.fillOval(dotX, dotY, DOT_WIDTH, DOT_HEIGHT);
					if (i < waterValues.size() - 1) {
						g.drawLine(dotX + DOT_WIDTH / 2, dotY + DOT_HEIGHT / 2, dotX + spaceBetweenDots + DOT_WIDTH / 2, lineGraphPanel.getHeight() - GRAPH_BORDER - (int) (lineGraphHeight * (waterValues.get(i + 1) / maxValue)) + DOT_HEIGHT / 2);
					}
				}

			} else if (index == BOX_AND_WHISKER_PLOT) {
				Graphics g = boxAndWhiskerPlotPanel.getGraphics();
				Graphics2D g2 = (Graphics2D) g;
				if (g == null) continue;
				g2.setStroke(new BasicStroke(5));
				g.setFont(new Font("Verdana", Font.PLAIN, 10));
				g.setColor(Color.BLACK);

				// Have to clone because we are going to sort the list and we don't want to affect the actual history stored in the states
				ArrayList<Double> waterValues = (ArrayList<Double>) state.getWaterValues(dataType).clone();
				Collections.sort(waterValues);

				int boxAndWhiskerPlotWidth = boxAndWhiskerPlotPanel.getWidth() - (GRAPH_BORDER * 2);
				int boxAndWhiskerPlotHeight = boxAndWhiskerPlotPanel.getHeight() - (GRAPH_BORDER * 2);
				int boxAndWhiskerPlotPanelWidth = boxAndWhiskerPlotPanel.getWidth();
				int boxAndWhiskerPlotPanelHeight = boxAndWhiskerPlotPanel.getHeight();

				// x-axis line
				g.drawLine(GRAPH_BORDER, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER, boxAndWhiskerPlotPanelWidth - GRAPH_BORDER, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER);

				if (waterValues.size() <= 2) continue;

				// Draw lower outlier stuff
				for (Double lowerOutlier : lowerOutliers) {
					int lowerOutlierX = GRAPH_BORDER + (int) ((lowerOutlier / (maximum + minimum)) * boxAndWhiskerPlotWidth);
					g.drawLine(lowerOutlierX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER, lowerOutlierX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 10);
					int stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(lowerOutlier));
					g.drawString(String.valueOf(lowerOutlier), lowerOutlierX - stringWidth / 2, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 20);
					g.fillOval(lowerOutlierX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
				}

				// Draw upper outliers stuff
				for (Double upperOutlier : upperOutliers) {
					int upperOutlierX = GRAPH_BORDER + (int) ((upperOutlier / (maximum + minimum)) * boxAndWhiskerPlotWidth);
					g.drawLine(upperOutlierX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER, upperOutlierX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 10);
					int stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(upperOutlier));
					g.drawString(String.valueOf(upperOutlier), upperOutlierX - stringWidth / 2, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 20);
					g.fillOval(upperOutlierX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
				}

				// Draw minimum (the lowest one not considered an outlier) stuff
				int minimumX = GRAPH_BORDER + (int) ((waterValues.get(lowerOutliers.size()) / (maximum + minimum)) * boxAndWhiskerPlotWidth);
				g.drawLine(minimumX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER, minimumX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 10);
				g.fillOval(minimumX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
				int stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(waterValues.get(lowerOutliers.size())));
				g.drawString(String.valueOf(waterValues.get(lowerOutliers.size())), minimumX - stringWidth / 2, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 20);

				// Draw Q1 stuff
				int firstQuartileX = GRAPH_BORDER + (int) ((firstQuartile / (maximum + minimum)) * boxAndWhiskerPlotWidth);
				g.drawLine(firstQuartileX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER, firstQuartileX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 10);
				g.fillOval(firstQuartileX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
				stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(firstQuartile));
				g.drawString(String.valueOf(firstQuartile), firstQuartileX - stringWidth / 2, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 20);

				// Draw median stuff
				int medianX = GRAPH_BORDER + (int) ((median / (maximum + minimum)) * boxAndWhiskerPlotWidth);
				g.drawLine(medianX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER, medianX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 10);
				g.fillOval(medianX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
				stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(median));
				g.drawString(String.valueOf(median), medianX - stringWidth / 2, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 20);

				// Draw Q3 stuff
				int thirdQuartileX = GRAPH_BORDER + (int) ((thirdQuartile / (maximum + minimum)) * boxAndWhiskerPlotWidth);
				g.drawLine(thirdQuartileX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER, thirdQuartileX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 10);
				g.fillOval(thirdQuartileX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
				stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(thirdQuartile));
				g.drawString(String.valueOf(thirdQuartile), thirdQuartileX - stringWidth / 2, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 20);

				// Draw maximum (the largest one not considered an outlier) stuff
				int maximumX = GRAPH_BORDER + (int) ((waterValues.get(waterValues.size() - upperOutliers.size() - 1) / (maximum + minimum)) * boxAndWhiskerPlotWidth);
				g.drawLine(maximumX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER, maximumX, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 10);
				g.fillOval(maximumX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
				stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(waterValues.get(waterValues.size() - upperOutliers.size() - 1)));
				g.drawString(String.valueOf(waterValues.get(waterValues.size() - upperOutliers.size() - 1)), maximumX - stringWidth / 2, boxAndWhiskerPlotPanelHeight - GRAPH_BORDER + 20);

				// Draw the line between the lowest value thats not an outlier and the first quartile
				g.drawLine(minimumX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2), firstQuartileX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2));

				// Draw the box that goes from the first quartile to the third quartile
				g.drawRect(firstQuartileX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2 - BOX_AND_WHISKER_PLOT_HEIGHT_SIZE / 2), (int) (thirdQuartileX - firstQuartileX), BOX_AND_WHISKER_PLOT_HEIGHT_SIZE);

				// Draw the line that goes from the top of the box to the median to the bottom of the box
				g.drawLine(medianX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2 - BOX_AND_WHISKER_PLOT_HEIGHT_SIZE / 2), medianX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2 + BOX_AND_WHISKER_PLOT_HEIGHT_SIZE / 2));

				// Draw the line that connects the third quartile to the highest value that isnt an outlier
				g.drawLine(thirdQuartileX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2), maximumX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2));

			}
		}
	}

	private void resetStatistics() {
		maximumLabel.setText(MAXIMUM_STRING);
		minimumLabel.setText(MINIMUM_STRING);
		summationOfValuesLabel.setText(SUMMATION_OF_VALUES_STRING);
		summationOfSquaredValuesLabel.setText(SUMMATION_OF_SQUARED_VALUES_STRING);
		meanLabel.setText(MEAN_STRING);
		medianLabel.setText(MEDIAN_STRING);
		firstQuartileLabel.setText(FIRST_QUARTILE_STRING);
		thirdQuartileLabel.setText(THIRD_QUARTILE_STRING);
		standardDeviationLabel.setText(STANDARD_DEVIATION_STRING);
		numberOfValuesLabel.setText(NUMBER_OF_VALUES_STRING);

		lowerOutliers.clear();
		upperOutliers.clear();
	}

	@SuppressWarnings("unchecked")
	private void calculateStatistics() {
		// The clone is necessary because we are going to directly operate on the data set (to calculate median, q1, and q3) and
		// so we don't want to mess up the stored history
		ArrayList<Double> values = (ArrayList<Double>) state.getWaterValues(dataType).clone();

		Collections.sort(values);

		if (values.size() > 1) {
			Double summation = 0.0d;
			// Note this is the values themselves squared, not the above summation
			Double summationOfSquaredValues = 0.0d;

			for (Double value : values) {
				summation += value;
				summationOfSquaredValues += (value * value);
			}

			Double mean = (summation / values.size());
			Double median = calculateMedian(values);
			Double firstQuartile = 0.0d;
			Double thirdQuartile = 0.0d;

			if ((values.size()) % 2 == 0) {
				firstQuartile = calculateMedian(values.subList(0, values.size() / 2));
				thirdQuartile = calculateMedian(values.subList(values.size() / 2, values.size()));
			} else {
				firstQuartile = calculateMedian(values.subList(0, (int) (values.size() / 2)));
				thirdQuartile = calculateMedian(values.subList((int) (values.size() / 2 + 1), values.size()));
			}

			double standardDeviation = 0.0d;
			for (Double value : values) {
				standardDeviation += Math.pow((mean - value), 2);
			}
			standardDeviation = Math.sqrt(standardDeviation / values.size());

			double interQuartileRange = thirdQuartile - firstQuartile;
			lowerFence = firstQuartile - (interQuartileRange * 1.5);
			upperFence = thirdQuartile + (interQuartileRange * 1.5);

			this.minimum = values.get(0);
			this.firstQuartile = firstQuartile;
			this.median = median;
			this.thirdQuartile = thirdQuartile;
			this.maximum = values.get(values.size() - 1);
			this.lowerOutliers = calculateLowerOutliers(values, lowerFence);
			this.upperOutliers = calculateUpperOutliers(values, upperFence);

			numberOfValuesLabel.setText(NUMBER_OF_VALUES_STRING + values.size());
			maximumLabel.setText(MAXIMUM_STRING + values.get(values.size() - 1));
			minimumLabel.setText(MINIMUM_STRING + values.get(0));
			summationOfValuesLabel.setText(SUMMATION_OF_VALUES_STRING + summation);
			summationOfSquaredValuesLabel.setText(SUMMATION_OF_SQUARED_VALUES_STRING + summationOfSquaredValues);
			meanLabel.setText(MEAN_STRING + mean);
			medianLabel.setText(MEDIAN_STRING + median);
			firstQuartileLabel.setText(FIRST_QUARTILE_STRING + firstQuartile);
			thirdQuartileLabel.setText(THIRD_QUARTILE_STRING + thirdQuartile);
			standardDeviationLabel.setText(STANDARD_DEVIATION_STRING + standardDeviation);
		}
	}

	private ArrayList<Double> calculateLowerOutliers(final ArrayList<Double> values, final double lowerFence) {
		ArrayList<Double> lowerOutliers = new ArrayList<Double>();
		for (Double value : values) {
			if (value < lowerFence) {
				lowerOutliers.add(value);
			}
		}
		return lowerOutliers;
	}

	private ArrayList<Double> calculateUpperOutliers(final ArrayList<Double> values, final double upperFence) {
		ArrayList<Double> upperOutliers = new ArrayList<Double>();
		for (Double value : values) {
			if (value > upperFence) {
				upperOutliers.add(value);
			}
		}
		return upperOutliers;
	}

	private Double calculateMedian(final List<Double> values) {
		Double median = 0.0d;
		if (values.size() % 2 == 0) {
			median = (values.get(values.size() / 2) + values.get(values.size() / 2 - 1)) / 2.0d;
		} else {
			median = values.get((values.size() / 2));
		}
		return median;
	}

}
