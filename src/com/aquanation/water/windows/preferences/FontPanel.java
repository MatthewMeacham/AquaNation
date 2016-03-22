package com.aquanation.water.windows.preferences;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import com.aquanation.water.AquaNation;
import com.aquanation.water.listeners.IntegerOnlyListener;
import com.aquanation.water.listeners.TextOnlyListener;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The panel that contains the interface for selecting a font
 *
 */
public class FontPanel {

	private final ComboBoxActionListener comboBoxActionListener = new ComboBoxActionListener();
	private final ButtonActionListener buttonActionListener = new ButtonActionListener();

	private JFrame parentFrame;

	private JPanel contentPane;

	private JComboBox<String> fontComboBox;
	private JComboBox<String> styleComboBox;
	private JComboBox<String> sizeComboBox;

	private JButton colorButton;

	private JLabel previewLabel;

	public JPanel getFontContentPane() {
		return contentPane;
	}

	public FontPanel(JFrame frame) {
		this.parentFrame = frame;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(0, 3, 40, 10));

		JLabel fontLabel = new JLabel("Font");
		fontLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		fontLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fontLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(fontLabel);

		JLabel styleLabel = new JLabel("Style");
		styleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		styleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(styleLabel);

		JLabel sizeLabel = new JLabel("Size");
		sizeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		sizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(sizeLabel);

		fontComboBox = new JComboBox<String>();
		DefaultComboBoxModel<String> fontComboBoxModel = new DefaultComboBoxModel<String>();
		fontComboBox.setModel(fontComboBoxModel);
		for (String fontName : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
			fontComboBoxModel.addElement(fontName);
		}
		fontComboBox.setEditable(true);
		fontComboBox.setSelectedItem(AquaNation.getFont().getName());
		fontComboBox.setActionCommand("font");
		fontComboBox.addActionListener(comboBoxActionListener);
		contentPane.add(fontComboBox);

		styleComboBox = new JComboBox<String>();
		DefaultComboBoxModel<String> styleComboBoxModel = new DefaultComboBoxModel<String>(new String[] { "Plain", "Bold", "Italic" });
		styleComboBox.setModel(styleComboBoxModel);
		styleComboBox.setEditable(true);
		JTextComponent styleTextComponent = (JTextComponent) styleComboBox.getEditor().getEditorComponent();
		styleTextComponent.getDocument().addDocumentListener(new TextOnlyListener(styleTextComponent, false));
		switch (AquaNation.getFont().getStyle()) {
		case Font.PLAIN:
			styleComboBox.setSelectedItem("Plain");
			break;
		case Font.BOLD:
			styleComboBox.setSelectedItem("Bold");
			break;
		case Font.ITALIC:
			styleComboBox.setSelectedItem("Italic");
			break;
		default:
			styleComboBox.setSelectedItem("Plain");
		}
		styleComboBox.setActionCommand("style");
		styleComboBox.addActionListener(comboBoxActionListener);
		contentPane.add(styleComboBox);

		sizeComboBox = new JComboBox<String>();
		DefaultComboBoxModel<String> sizeComboBoxModel = new DefaultComboBoxModel<String>(new String[] { "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });
		sizeComboBox.setModel(sizeComboBoxModel);
		sizeComboBox.setEditable(true);
		JTextComponent sizeTextComponent = (JTextComponent) sizeComboBox.getEditor().getEditorComponent();
		sizeTextComponent.getDocument().addDocumentListener(new IntegerOnlyListener(sizeTextComponent));
		sizeComboBox.setSelectedItem(String.valueOf(AquaNation.getFont().getSize()));
		sizeComboBox.setActionCommand("size");
		sizeComboBox.addActionListener(comboBoxActionListener);
		contentPane.add(sizeComboBox);

		Component glue_1 = Box.createGlue();
		contentPane.add(glue_1);

		colorButton = new JButton("Color");
		colorButton.setForeground(Color.WHITE);
		colorButton.setBackground(AquaNation.getFontColor());
		colorButton.setActionCommand("color");
		colorButton.addActionListener(buttonActionListener);
		contentPane.add(colorButton);

		Component glue_2 = Box.createGlue();
		contentPane.add(glue_2);

		Component glue = Box.createGlue();
		contentPane.add(glue);

		previewLabel = new JLabel("Preview");
		previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(previewLabel);

		Component glue1 = Box.createGlue();
		contentPane.add(glue1);

		JButton backButton = new JButton("Back");
		backButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		backButton.setActionCommand("back");
		backButton.addActionListener(buttonActionListener);
		contentPane.add(backButton);

		Component glue2 = Box.createGlue();
		contentPane.add(glue2);

		JButton doneButton = new JButton("Done");
		doneButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doneButton.setActionCommand("done");
		doneButton.addActionListener(buttonActionListener);
		contentPane.add(doneButton);

	}

	private class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "back":
				parentFrame.dispose();
				break;
			case "done":
				AquaNation.setFont(new Font(((String) fontComboBox.getSelectedItem()).trim().toLowerCase(), getStyle((String) styleComboBox.getSelectedItem()), Integer.parseInt(((String) sizeComboBox.getSelectedItem()).trim())));
				AquaNation.setFontColor(colorButton.getBackground());
				parentFrame.dispose();
				break;
			case "color":
				Color chosenColor = JColorChooser.showDialog(null, "Choose color", colorButton.getBackground());
				if (chosenColor == null) return;
				colorButton.setBackground(chosenColor);
				previewLabel.setForeground(chosenColor);
				break;
			}
		}

	}

	private class ComboBoxActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "font":
				break;
			case "style":
				break;
			case "size":
				break;
			}
			previewLabel.setFont(new Font(((String) fontComboBox.getSelectedItem()).trim().toLowerCase(), getStyle((String) styleComboBox.getSelectedItem()), Integer.parseInt(((String) sizeComboBox.getSelectedItem()).trim())));
		}
	}

	private int getStyle(String style) {
		style = style.trim().toLowerCase();
		switch (style) {
		case "plain":
			return Font.PLAIN;
		case "bold":
			return Font.BOLD;
		case "italic":
			return Font.ITALIC;
		default:
			return Font.PLAIN;
		}
	}

}
