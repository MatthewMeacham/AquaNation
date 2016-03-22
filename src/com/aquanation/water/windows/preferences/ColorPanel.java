package com.aquanation.water.windows.preferences;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.aquanation.water.colorer.ColorRange;
import com.aquanation.water.listeners.DoubleOnlyListener;
import com.aquanation.water.windows.MainFrame;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The panel that contains the interface for creating color ranges
 *
 */
public class ColorPanel {

	private JFrame parentFrame;

	private JPanel contentPane;
	private JPanel inputPanel;
	private JLabel colorLabel;
	private JLabel fromLabel;
	private JLabel conditionLabel;
	private JLabel toLabel;
	private JLabel removeLabel;

	private ArrayList<JButton> colorButtons = new ArrayList<JButton>();
	private ArrayList<JTextField> fromTextFields = new ArrayList<JTextField>();
	private ArrayList<JLabel> conditionLabels = new ArrayList<JLabel>();
	private ArrayList<JTextField> toTextFields = new ArrayList<JTextField>();
	private ArrayList<JButton> removeButtons = new ArrayList<JButton>();

	private JPanel addNewParameterPanel;
	private JButton addAnotherParameterButton;
	private JButton backButton;
	private JButton doneButton;

	public JPanel getColorContentPane() {
		return contentPane;
	}

	public ColorPanel(JFrame frame) {
		this.parentFrame = frame;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(2, 2, 2, 2));
		contentPane.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setMaximumSize(new Dimension(23, 23));
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setSize(new Dimension(10, 10));
		panel.setMaximumSize(new Dimension(10, 10));
		panel.setBounds(new Rectangle(0, 0, 6, 20));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));

		inputPanel = new JPanel();
		panel.add(inputPanel);
		inputPanel.setLayout(new GridLayout(0, 5, 5, 5));

		// Headers

		colorLabel = new JLabel("Color");
		colorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		colorLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		inputPanel.add(colorLabel);

		fromLabel = new JLabel("From");
		fromLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fromLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		inputPanel.add(fromLabel);

		conditionLabel = new JLabel("");
		inputPanel.add(conditionLabel);

		toLabel = new JLabel("To");
		toLabel.setHorizontalAlignment(SwingConstants.CENTER);
		toLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		inputPanel.add(toLabel);

		removeLabel = new JLabel("Remove");
		removeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		removeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		inputPanel.add(removeLabel);

		// End headers

		List<ColorRange> colorRanges = MainFrame.colorer.getColorRanges();

		for (ColorRange colorRange : colorRanges) {
			addNewRow(colorRange.getColor(), colorRange.getFrom(), colorRange.getTo());
		}

		addNewParameterPanel = new JPanel();
		contentPane.add(addNewParameterPanel, BorderLayout.SOUTH);

		// This creates padding between the colors and the back, add new parameter, and done buttons
		Component verticalStrut1 = Box.createVerticalStrut(0);
		Component verticalStrut2 = Box.createVerticalStrut(0);
		Component verticalStrut3 = Box.createVerticalStrut(0);

		addNewParameterPanel.add(verticalStrut1);
		addNewParameterPanel.add(verticalStrut2);
		addNewParameterPanel.add(verticalStrut3);

		addAnotherParameterButton = new JButton("Add Another Parameter");
		addAnotherParameterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewRow(Color.RED.getRGB());
			}
		});
		addNewParameterPanel.setLayout(new GridLayout(0, 3, 40, -15));

		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentFrame.dispose();
			}
		});
		addNewParameterPanel.add(backButton);
		addNewParameterPanel.add(addAnotherParameterButton);

		doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.colorer.removeAllColorRanges();

				boolean goodToClose = true;
				for (int i = 0; i < fromTextFields.size(); i++) {
					String fromString = fromTextFields.get(i).getText().trim();
					String toString = toTextFields.get(i).getText().trim();
					if (fromString.equals("") || toString.equals("")) continue;

					double from = Double.parseDouble(fromString);
					double to = Double.parseDouble(toString);

					if (to < from) {
						setAsRed(i, 1500);
						goodToClose = false;

					}

					for (int j = 0; j < fromTextFields.size(); j++) {
						if (i == j) continue;
						String fromString2 = fromTextFields.get(j).getText().trim();
						String toString2 = toTextFields.get(j).getText().trim();
						if (fromString2.equals("") || toString2.equals("")) continue;

						double from2 = Double.parseDouble(fromString2);
						double to2 = Double.parseDouble(toString2);

						if ((from2 >= from && to2 <= to) || (from >= from2 && to <= to2)) {
							setAsRed(j, 1500);
							goodToClose = false;
						}
					}

					MainFrame.colorer.addColorRange(from, to, colorButtons.get(i).getBackground().getRGB());
				}
				if (goodToClose) {
					parentFrame.dispose();
					MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage());
				}
			}
		});
		addNewParameterPanel.add(doneButton);

		// This creates padding between the back, add new parameter, done button and the bottom
		Component verticalStrut4 = Box.createVerticalStrut(0);
		Component verticalStrut5 = Box.createVerticalStrut(0);
		Component verticalStrut6 = Box.createVerticalStrut(0);

		addNewParameterPanel.add(verticalStrut4);
		addNewParameterPanel.add(verticalStrut5);
		addNewParameterPanel.add(verticalStrut6);
	}

	private void setAsRed(final int index, final long duration) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				fromTextFields.get(index).setBackground(Color.RED);
				toTextFields.get(index).setBackground(Color.RED);
				try {
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				fromTextFields.get(index).setBackground(Color.WHITE);
				toTextFields.get(index).setBackground(Color.WHITE);
			}
		}).start();
	}

	private void addNewRow(final int color) {
		inputPanel.setVisible(false);
		addColorButton(color);
		addFromTextField();
		addConditionsLabel();
		addToTextField();
		addRemoveButton();
		inputPanel.setVisible(true);
		inputPanel.repaint();
	}

	private void addNewRow(final int color, double from, double to) {
		inputPanel.setVisible(false);
		addColorButton(color);
		addFromTextField(String.valueOf(from));
		addConditionsLabel();
		addToTextField(String.valueOf(to));
		addRemoveButton();
		inputPanel.setVisible(true);
		inputPanel.repaint();
	}

	private void addColorButton(int color) {
		JButton button = new JButton("");
		button.setBackground(new Color(color));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color chosenColor = JColorChooser.showDialog(null, "Choose color", new Color(color));
				if (chosenColor == null) return;
				button.setBackground(chosenColor);
			}
		});
		inputPanel.add(button);
		colorButtons.add(button);
	}

	private void addFromTextField() {
		addFromTextField("");
	}

	private void addFromTextField(String from) {
		JTextField textField = new JTextField();
		textField.setColumns(10);
		textField.setText(from);
		textField.getDocument().addDocumentListener(new DoubleOnlyListener(textField));
		inputPanel.add(textField);
		fromTextFields.add(textField);
	}

	private void addConditionsLabel() {
		JLabel label = new JLabel("<= X <");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 18));
		inputPanel.add(label);
		conditionLabels.add(label);
	}

	private void addToTextField() {
		addToTextField("");
	}

	private void addToTextField(String to) {
		JTextField textField = new JTextField();
		textField.setColumns(10);
		textField.setText(to);
		textField.getDocument().addDocumentListener(new DoubleOnlyListener(textField));
		inputPanel.add(textField);
		toTextFields.add(textField);
	}

	private void addRemoveButton() {
		JButton button = new JButton("Remove");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = removeButtons.indexOf(button);
				removeRow(index);
			}
		});
		inputPanel.add(button);
		removeButtons.add(button);
	}

	private void removeRow(final int index) {
		inputPanel.setVisible(false);
		inputPanel.remove(colorButtons.remove(index));
		inputPanel.remove(fromTextFields.remove(index));
		inputPanel.remove(conditionLabels.remove(index));
		inputPanel.remove(toTextFields.remove(index));
		inputPanel.remove(removeButtons.remove(index));
		inputPanel.setVisible(true);
		inputPanel.repaint();
	}

}
