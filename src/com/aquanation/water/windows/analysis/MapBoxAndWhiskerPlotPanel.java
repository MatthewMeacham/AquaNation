package com.aquanation.water.windows.analysis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.aquanation.water.AquaNation;
import com.aquanation.water.State;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The panel that contains the box and whisker plot for the entire map
 *
 */
public class MapBoxAndWhiskerPlotPanel {

	private JFrame parentFrame;

	private JPanel contentPane;

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
	private final int GRAPH_BORDER = 25;
	private final int DOT_WIDTH = 10;
	private final int DOT_HEIGHT = 10;

	private ArrayList<Double> waterValues = new ArrayList<Double>();

	public MapBoxAndWhiskerPlotPanel(JFrame parentFrame) {
		this.parentFrame = parentFrame;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		calculateStatistics();
	}

	public void render() {
		Graphics g = contentPane.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (g == null) return;
		g2.setStroke(new BasicStroke(5));
		g.setFont(new Font("Verdana", Font.PLAIN, 10));
		g.setColor(Color.BLACK);

		int boxAndWhiskerPlotWidth = contentPane.getWidth() - (GRAPH_BORDER * 2);
		int boxAndWhiskerPlotHeight = contentPane.getHeight() - (GRAPH_BORDER * 2);
		int contentPaneWidth = contentPane.getWidth();
		int contentPaneHeight = contentPane.getHeight();

		// x-axis line
		g.drawLine(GRAPH_BORDER, contentPaneHeight - GRAPH_BORDER, contentPaneWidth - GRAPH_BORDER, contentPaneHeight - GRAPH_BORDER);

		if (waterValues.size() <= 2) return;

		// Draw lower outlier stuff
		for (Double lowerOutlier : lowerOutliers) {
			int lowerOutlierX = GRAPH_BORDER + (int) ((lowerOutlier / (maximum + minimum)) * boxAndWhiskerPlotWidth);
			g.drawLine(lowerOutlierX, contentPaneHeight - GRAPH_BORDER, lowerOutlierX, contentPaneHeight - GRAPH_BORDER + 10);
			int stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(lowerOutlier));
			g.drawString(String.valueOf(lowerOutlier), lowerOutlierX - stringWidth / 2, contentPaneHeight - GRAPH_BORDER + 20);
			g.fillOval(lowerOutlierX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
		}

		// Draw upper outliers stuff
		for (Double upperOutlier : upperOutliers) {
			int upperOutlierX = GRAPH_BORDER + (int) ((upperOutlier / (maximum + minimum)) * boxAndWhiskerPlotWidth);
			g.drawLine(upperOutlierX, contentPaneHeight - GRAPH_BORDER, upperOutlierX, contentPaneHeight - GRAPH_BORDER + 10);
			int stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(upperOutlier));
			g.drawString(String.valueOf(upperOutlier), upperOutlierX - stringWidth / 2, contentPaneHeight - GRAPH_BORDER + 20);
			g.fillOval(upperOutlierX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
		}

		// Draw minimum (the lowest one not considered an outlier) stuff
		int minimumX = GRAPH_BORDER + (int) ((waterValues.get(lowerOutliers.size()) / (maximum + minimum)) * boxAndWhiskerPlotWidth);
		g.drawLine(minimumX, contentPaneHeight - GRAPH_BORDER, minimumX, contentPaneHeight - GRAPH_BORDER + 10);
		g.fillOval(minimumX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
		int stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(waterValues.get(lowerOutliers.size())));
		g.drawString(String.valueOf(waterValues.get(lowerOutliers.size())), minimumX - stringWidth / 2, contentPaneHeight - GRAPH_BORDER + 20);

		// Draw Q1 stuff
		int firstQuartileX = GRAPH_BORDER + (int) ((firstQuartile / (maximum + minimum)) * boxAndWhiskerPlotWidth);
		g.drawLine(firstQuartileX, contentPaneHeight - GRAPH_BORDER, firstQuartileX, contentPaneHeight - GRAPH_BORDER + 10);
		g.fillOval(firstQuartileX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
		stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(firstQuartile));
		g.drawString(String.valueOf(firstQuartile), firstQuartileX - stringWidth / 2, contentPaneHeight - GRAPH_BORDER + 20);

		// Draw median stuff
		int medianX = GRAPH_BORDER + (int) ((median / (maximum + minimum)) * boxAndWhiskerPlotWidth);
		g.drawLine(medianX, contentPaneHeight - GRAPH_BORDER, medianX, contentPaneHeight - GRAPH_BORDER + 10);
		g.fillOval(medianX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
		stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(median));
		g.drawString(String.valueOf(median), medianX - stringWidth / 2, contentPaneHeight - GRAPH_BORDER + 20);

		// Draw Q3 stuff
		int thirdQuartileX = GRAPH_BORDER + (int) ((thirdQuartile / (maximum + minimum)) * boxAndWhiskerPlotWidth);
		g.drawLine(thirdQuartileX, contentPaneHeight - GRAPH_BORDER, thirdQuartileX, contentPaneHeight - GRAPH_BORDER + 10);
		g.fillOval(thirdQuartileX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
		stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(thirdQuartile));
		g.drawString(String.valueOf(thirdQuartile), thirdQuartileX - stringWidth / 2, contentPaneHeight - GRAPH_BORDER + 20);

		// Draw maximum (the largest one not considered an outlier) stuff
		int maximumX = GRAPH_BORDER + (int) ((waterValues.get(waterValues.size() - upperOutliers.size() - 1) / (maximum + minimum)) * boxAndWhiskerPlotWidth);
		g.drawLine(maximumX, contentPaneHeight - GRAPH_BORDER, maximumX, contentPaneHeight - GRAPH_BORDER + 10);
		g.fillOval(maximumX, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2) - DOT_HEIGHT / 2, DOT_WIDTH, DOT_HEIGHT);
		stringWidth = g2.getFontMetrics().stringWidth(String.valueOf(waterValues.get(waterValues.size() - upperOutliers.size() - 1)));
		g.drawString(String.valueOf(waterValues.get(waterValues.size() - upperOutliers.size() - 1)), maximumX - stringWidth / 2, contentPaneHeight - GRAPH_BORDER + 20);

		// Draw the line between the lowest value thats not an outlier and the first quartile
		g.drawLine(minimumX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2), firstQuartileX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2));

		// Draw the box that goes from the first quartile to the third quartile
		g.drawRect(firstQuartileX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2 - BOX_AND_WHISKER_PLOT_HEIGHT_SIZE / 2), (int) (thirdQuartileX - firstQuartileX), BOX_AND_WHISKER_PLOT_HEIGHT_SIZE);

		// Draw the line that goes from the top of the box to the median to the bottom of the box
		g.drawLine(medianX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2 - BOX_AND_WHISKER_PLOT_HEIGHT_SIZE / 2), medianX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2 + BOX_AND_WHISKER_PLOT_HEIGHT_SIZE / 2));

		// Draw the line that connects the third quartile to the highest value that isnt an outlier
		g.drawLine(thirdQuartileX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2), maximumX + DOT_WIDTH / 2, GRAPH_BORDER + (int) (boxAndWhiskerPlotHeight / 2));

	}

	public void resetStatistics() {
		lowerOutliers.clear();
		upperOutliers.clear();
		waterValues.clear();
		calculateStatistics();
	}

	private void calculateStatistics() {
		ArrayList<Double> values = new ArrayList<Double>();
		for (State state : AquaNation.getStates()) {
			Double value = state.getWaterValue(((MapAnalysisFrame) parentFrame).getDataType());
			if (value != null) values.add(value);
		}

		Collections.sort(values);
		waterValues = values;

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

	public JPanel getContentPane() {
		return contentPane;
	}

}
