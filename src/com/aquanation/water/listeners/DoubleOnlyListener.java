package com.aquanation.water.listeners;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 * @author Nick Wilson
 * @author Matthew Meacham
 * 
 *         Represents a Listener that only accepts numbers and a period as input
 *
 */
public class DoubleOnlyListener implements DocumentListener {

	private final JTextField textField;

	public DoubleOnlyListener(JTextField textField) {
		this.textField = textField;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		String text = textField.getText();
		removeBadCharacter(text);
	}

	// We have to perform this change in a different thread because we have to
	// wait for the Document to release the lock it has
	// on the textField at that moment. This will prevent the
	// IllegalStateException: Attempt to mutate in notification
	private void removeBadCharacter(final String text) {
		Runnable remove = new Runnable() {
			@Override
			public void run() {
				char[] characters = text.toCharArray();
				for (int i = 0; i < characters.length; i++) {
					if (!Character.isDigit(characters[i]) && characters[i] != '.' && characters[i] != ' ' && characters[i] != ',' && characters[i] != '-') {
						textField.setText(text.substring(0, i) + text.substring(i + 1));
						break;
					}
				}
			}
		};
		SwingUtilities.invokeLater(remove);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {

	}

	// Don't use this method
	@Override
	public void changedUpdate(DocumentEvent e) {

	}

}
