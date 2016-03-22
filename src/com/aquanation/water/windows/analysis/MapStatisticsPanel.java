package com.aquanation.water.windows.analysis;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.aquanation.water.State;
import com.aquanation.water.AquaNation;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The panel that contains the statistics for the entire map
 *
 */
public class MapStatisticsPanel {

	private JFrame parentFrame;

	private JPanel contentPane;

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

	public MapStatisticsPanel(JFrame parentFrame) {
		this.parentFrame = parentFrame;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));

		Component glue2 = Box.createGlue();
		contentPane.add(glue2);

		meanLabel = new JLabel(MEAN_STRING);
		meanLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(meanLabel);

		summationOfValuesLabel = new JLabel(SUMMATION_OF_VALUES_STRING);
		summationOfValuesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(summationOfValuesLabel);

		summationOfSquaredValuesLabel = new JLabel(SUMMATION_OF_SQUARED_VALUES_STRING);
		summationOfSquaredValuesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(summationOfSquaredValuesLabel);

		standardDeviationLabel = new JLabel(STANDARD_DEVIATION_STRING);
		standardDeviationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(standardDeviationLabel);

		numberOfValuesLabel = new JLabel(NUMBER_OF_VALUES_STRING);
		numberOfValuesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(numberOfValuesLabel);

		minimumLabel = new JLabel(MINIMUM_STRING);
		minimumLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(minimumLabel);

		firstQuartileLabel = new JLabel(FIRST_QUARTILE_STRING);
		firstQuartileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(firstQuartileLabel);

		medianLabel = new JLabel(MEDIAN_STRING);
		medianLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(medianLabel);

		thirdQuartileLabel = new JLabel(THIRD_QUARTILE_STRING);
		thirdQuartileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(thirdQuartileLabel);

		maximumLabel = new JLabel(MAXIMUM_STRING);
		maximumLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(maximumLabel);

		Component glue3 = Box.createGlue();
		contentPane.add(glue3);

		calculateStatistics();
	}

	public void render() {
		// Do nothing
	}

	public void resetStatistics() {
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
		calculateStatistics();
	}

	private void calculateStatistics() {
		ArrayList<Double> values = new ArrayList<Double>();
		for (State state : AquaNation.getStates()) {
			Double value = state.getWaterValue(((MapAnalysisFrame) parentFrame).getDataType());
			if (value != null) values.add(value);
		}

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

	private Double calculateMedian(final List<Double> values) {
		Double median = 0.0d;
		if (values.size() % 2 == 0) {
			median = (values.get(values.size() / 2) + values.get(values.size() / 2 - 1)) / 2.0d;
		} else {
			median = values.get((values.size() / 2));
		}
		return median;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

}
