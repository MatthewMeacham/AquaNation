package com.aquanation.water.windows.analysis;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.aquanation.water.AquaNation;
import com.aquanation.water.State;
import com.aquanation.water.helpers.Range;
import com.aquanation.water.listeners.IntegerOnlyListener;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The panel that contains the histogram for the entire map
 *
 */
public class MapHistogramPanel {

	private JFrame parentFrame;

	private JPanel contentPane;
	private JPanel graphPanel;

	private final int GRAPH_BORDER = 50;
	private int numberOfRanges = 5;

	private JTextField numberOfRangesTextField;

	// True by default so that it will calculate the values at the start
	private boolean histogramNeedsToChange = true;

	private ArrayList<Integer> values = new ArrayList<Integer>();
	private ArrayList<Range> ranges = new ArrayList<Range>();

	// This is used to create the horizontal axis and its marker labels
	private double maxNumberInHistogram = 0.0d;
	// The distance between each marker on the horizontal axis
	private double distance = 0.0d;

	public MapHistogramPanel(JFrame parentFrame) {
		this.parentFrame = parentFrame;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));

		graphPanel = new JPanel();
		contentPane.add(graphPanel, BorderLayout.CENTER);

		JPanel inputPanel = new JPanel();
		contentPane.add(inputPanel, BorderLayout.SOUTH);

		JLabel numberOfRangesLabel = new JLabel("Number of Ranges: ");
		inputPanel.add(numberOfRangesLabel);

		numberOfRangesTextField = new JTextField();
		numberOfRangesTextField.setText(String.valueOf(numberOfRanges));
		// this first listener ensures there is only numbers in it
		numberOfRangesTextField.getDocument().addDocumentListener(new IntegerOnlyListener(numberOfRangesTextField));
		// This second listener will change the number of ranges
		numberOfRangesTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				try {
					numberOfRanges = Integer.parseInt(numberOfRangesTextField.getText());
					histogramNeedsToChange = true;
				} catch (NumberFormatException e1) {

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				try {
					numberOfRanges = Integer.parseInt(numberOfRangesTextField.getText());
					histogramNeedsToChange = true;
				} catch (NumberFormatException e1) {

				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {

			}
		});
		inputPanel.add(numberOfRangesTextField);
		numberOfRangesTextField.setColumns(10);
	}

	public void render() {
		// If the data type has changed we need to recalculate all the boxes
		if (histogramNeedsToChange) {
			// Reset variables
			maxNumberInHistogram = 0;
			ranges.clear();
			values.clear();

			double maxValue = 0.0d;
			for (State state : AquaNation.getStates()) {
				Double value = state.getWaterValue(((MapAnalysisFrame) parentFrame).getDataType());
				if (value == null) continue;
				if (value > maxValue) maxValue = value;
			}

			distance = maxValue / (numberOfRanges - 1);
			for (int i = 0; i < (numberOfRanges); i++) {
				ranges.add(new Range(i * distance, i * distance + distance));
				values.add(0);
			}

			for (State state : AquaNation.getStates()) {
				Double waterValue = state.getWaterValue(((MapAnalysisFrame) parentFrame).getDataType());
				if (waterValue == null) continue;

				for (int i = 0; i < ranges.size(); i++) {
					if (ranges.get(i).getFrom() <= waterValue && ranges.get(i).getTo() > waterValue) {
						values.set(i, values.get(i) + 1);
						if (values.get(i) > maxNumberInHistogram) maxNumberInHistogram = values.get(i);
						break;
					}
				}
			}
			histogramNeedsToChange = false;
			contentPane.repaint();
		}

		Graphics g = contentPane.getGraphics();
		if (g == null) return;
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));

		// Draw the borders to the bar graph
		g.drawLine(GRAPH_BORDER, graphPanel.getHeight() - GRAPH_BORDER, GRAPH_BORDER, GRAPH_BORDER);
		g.drawLine(GRAPH_BORDER, graphPanel.getHeight() - GRAPH_BORDER, graphPanel.getWidth() - GRAPH_BORDER, graphPanel.getHeight() - GRAPH_BORDER);
		// Max value line indicator
		g.drawLine(GRAPH_BORDER - 10, GRAPH_BORDER, GRAPH_BORDER, GRAPH_BORDER);
		// Middle value line indicator
		g.drawLine(GRAPH_BORDER - 10, (graphPanel.getHeight()) / 2, GRAPH_BORDER, graphPanel.getHeight() / 2);
		// Zero value line indicator
		g.drawLine(GRAPH_BORDER - 10, graphPanel.getHeight() - GRAPH_BORDER, GRAPH_BORDER, graphPanel.getHeight() - GRAPH_BORDER);

		if (ranges.size() <= 0) return;

		// * 2 because we have to account for both borders
		int graphWidth = graphPanel.getWidth() - GRAPH_BORDER * 2;
		int graphHeight = graphPanel.getHeight() - GRAPH_BORDER * 2;
		int singleBarWidth = graphWidth / ranges.size() - 5;

		// Line indicators on the horizontal axis
		for (int i = 0; i <= ranges.size(); i++) {
			g.drawLine(GRAPH_BORDER + (singleBarWidth + 5) * i, graphPanel.getHeight() - GRAPH_BORDER, GRAPH_BORDER + (singleBarWidth + 5) * i, graphPanel.getHeight() - GRAPH_BORDER + 10);
		}

		if (maxNumberInHistogram == 0) return;

		g.setFont(new Font("Verdana", Font.PLAIN, 12));
		int stringHeight = g2.getFontMetrics().getHeight();
		// The stringHeight / 4 will place the text perfectly aligned with the indicator lines
		g.drawString(String.valueOf(maxNumberInHistogram), 2, GRAPH_BORDER + stringHeight / 4);
		g.drawString(String.valueOf(maxNumberInHistogram / 2), 2, graphPanel.getHeight() / 2 + stringHeight / 4);
		g.drawString("0", 2, graphPanel.getHeight() - GRAPH_BORDER + stringHeight / 4);

		for (int i = 0; i <= ranges.size(); i++) {
			if (ranges.size() >= 10) {
				String formatted = String.format("%.2f", distance * i);
				int stringWidth = g2.getFontMetrics().stringWidth(formatted);
				g.drawString(formatted, GRAPH_BORDER + (singleBarWidth + 5) * i - stringWidth / 2, graphPanel.getHeight() - GRAPH_BORDER + 25);
			} else {
				String formatted = String.valueOf(String.format("%.3f", distance * i));
				int stringWidth = g2.getFontMetrics().stringWidth(formatted);
				g.drawString(formatted, GRAPH_BORDER + (singleBarWidth + 5) * i - stringWidth / 2, graphPanel.getHeight() - GRAPH_BORDER + 25);
			}
		}

		g.setColor(Color.BLACK);
		for (int i = 0; i < values.size(); i++) {
			int waterValueHeight = (int) (graphHeight * (values.get(i) / maxNumberInHistogram));
			g.fillRect(GRAPH_BORDER + i * (singleBarWidth + 5), graphPanel.getHeight() - GRAPH_BORDER - waterValueHeight, singleBarWidth + 5, waterValueHeight);
		}
	}

	public void setHistogramNeedsToChange(final boolean change) {
		this.histogramNeedsToChange = change;
	}

	public JPanel getContentPane() {
		return this.contentPane;
	}

}
