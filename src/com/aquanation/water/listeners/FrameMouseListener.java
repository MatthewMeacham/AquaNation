package com.aquanation.water.listeners;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.aquanation.water.AquaNation;
import com.aquanation.water.State;
import com.aquanation.water.windows.MainFrame;
import com.aquanation.water.windows.StateHistoryFrame;

/**
 * 
 * @author Nick Wilson
 * @author Matthew Meacham
 * 
 *         Contains all code associated with the mouse and mouse motion listening
 *
 */
public class FrameMouseListener implements MouseListener, MouseMotionListener {

	private final JPopupMenu popup = new JPopupMenu();
	private final JMenuItem viewStateHistory = new JMenuItem("View State Water Value History");
	// TODO finish this if there is time
	// private final JMenuItem printStateHistory = new JMenuItem("Print State Water Value History");

	private int mouseX = 0;
	private int mouseY = 0;

	// Adjusting where the points will center so it looks nice
	private final int CLIP_X = -30;
	private final int CLIP_Y = -97;

	public FrameMouseListener() {
		viewStateHistory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Margins to make sure image is center to facilitate mouse clicks
				float marginX = (float) ((MainFrame.getImagePanel().getWidth() - MainFrame.getNewWidth()) / 2);
				float marginY = (float) ((MainFrame.getImagePanel().getHeight() - MainFrame.getNewHeight()) / 2);

				for (State state : AquaNation.getStates()) {
					Polygon rescaledOutline = new Polygon();
					for (int i = 0; i < state.getOutline().xpoints.length; i++) {
						int tempX = (int) ((state.getOutline().xpoints[i] + CLIP_X) * MainFrame.getRatioX() + marginX);
						int tempY = (int) ((state.getOutline().ypoints[i] + CLIP_Y) * MainFrame.getRatioY() + marginY);
						rescaledOutline.addPoint(tempX, tempY);
					}
					if (rescaledOutline.contains(mouseX, mouseY)) {
						new StateHistoryFrame(state);
						break;
					}
				}
			}
		});
		popup.add(viewStateHistory);
		// TODO add this if there is time
		// popup.add(printStateHistory);

		MainFrame.getImagePanel().setComponentPopupMenu(popup);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point point = e.getPoint();
		mouseX = point.x;
		mouseY = point.y;

		if (e.getButton() == 1) {
			float marginX = (float) ((MainFrame.getImagePanel().getWidth() - MainFrame.getNewWidth()) / 2);
			float marginY = (float) ((MainFrame.getImagePanel().getHeight() - MainFrame.getNewHeight()) / 2);

			for (State state : AquaNation.getStates()) {
				Polygon rescaledOutline = new Polygon();
				for (int i = 0; i < state.getOutline().xpoints.length; i++) {

					int tempX = (int) ((state.getOutline().xpoints[i] + CLIP_X) * MainFrame.getRatioX() + marginX);
					int tempY = (int) ((state.getOutline().ypoints[i] + CLIP_Y) * MainFrame.getRatioY() + marginY);
					rescaledOutline.addPoint(tempX, tempY);
				}
				if (rescaledOutline.contains(point)) {
					String response = JOptionPane.showInputDialog(null, "Please enter the water values for " + state.getFullName(), "Water values input", JOptionPane.PLAIN_MESSAGE);
					if (response == null) return;

					setStateWaterValues(state, response);

					MainFrame.getImagePanel().repaint();
					break;
				}
			}
		}
	}

	private void setStateWaterValues(State state, String waterSampleString) {
		for (Character character : waterSampleString.toCharArray()) {
			if (character == ' ' || character == '.' || character == ',' || character == '-') continue;
			if (!Character.isDigit(character)) return;
		}

		try {
			MainFrame.setStateWaterValues(state, waterSampleString);
		} catch (NumberFormatException e) {

		}

		MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage());
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

}
