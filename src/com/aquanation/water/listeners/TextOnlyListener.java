package com.aquanation.water.listeners;

import java.awt.KeyboardFocusManager;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import com.aquanation.water.AquaNation;
import com.aquanation.water.State;

/**
 * 
 * @author Nick Wilson
 * @author Matthew Meacham
 * 
 *         Represents a Listener that only accepts text as input
 *
 */
public class TextOnlyListener implements DocumentListener {

	private final JComponent component;
	private final boolean performAutoComplete;

	public TextOnlyListener(JComponent component, boolean performAutoComplete) {
		this.component = component;
		this.performAutoComplete = performAutoComplete;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() != component) return;

		String text = ((JTextComponent) component).getText();
		removeBadCharacter(text);

		if (performAutoComplete) autoComplete(text);
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
					if (!Character.isLetter(characters[i]) && characters[i] != ' ') {
						((JTextComponent) component).setText(text.substring(0, i) + text.substring(i + 1));
						break;
					}
				}
			}
		};
		SwingUtilities.invokeLater(remove);
	}

	private void autoComplete(final String temp) {
		Runnable autocomplete = new Runnable() {
			@Override
			public void run() {
				int matches = 0;
				State match = null;
				for (State s : AquaNation.getStates()) {
					if (matches > 1) {
						break;
					} else {
						if (temp.length() <= s.getFullName().length()) {
							if (temp.toLowerCase().equals(s.getFullName().substring(0, temp.length()).toLowerCase())) {
								matches++;
								match = s;
							}
						}

					}
				}
				if (matches == 1) {
					((JTextField) component).setText(match.getFullName());
					if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner().equals(component)) {
						KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner().transferFocus();
					}
				}
			}
		};
		SwingUtilities.invokeLater(autocomplete);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {

	}

	// Don't use this method
	@Override
	public void changedUpdate(DocumentEvent e) {

	}

}
