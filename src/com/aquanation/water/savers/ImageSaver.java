package com.aquanation.water.savers;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.aquanation.water.State;
import com.aquanation.water.AquaNation;
import com.aquanation.water.windows.MainFrame;

/**
 * 
 * @author Matthew Meacham
 *
 *         This class handles saving the map image to a file
 */
public class ImageSaver {

	public ImageSaver() {

	}

	public void save(File file) {
		String extension = "png";
		if (file.getPath().contains("jpg") || file.getPath().contains("jpeg")) extension = "jpg";
		else if (file.getPath().contains("gif")) extension = "gif";

		Image image = MainFrame.getFullMapImage();
		Graphics g = image.getGraphics();

		g.setColor(AquaNation.getFontColor());
		g.setFont(AquaNation.getFont());

		// Offset to offset the state name and water values because of JPanel's positioning in the MainFrame
		int xOffset = -30;
		int yOffset = -100;
		for (State state : AquaNation.getStates()) {
			if (AquaNation.showStateNames) g.drawString(state.getPostalCode(), state.getPostalCodePoint().x + xOffset, state.getPostalCodePoint().y + yOffset);

			if (AquaNation.showWaterValues && state.getWaterValues().size() > 0) {
				g.drawString(String.valueOf(state.getWaterValue(MainFrame.getSelectedDataType())), state.getWaterValuePoint().x + xOffset, state.getWaterValuePoint().y + yOffset);
			} else if (AquaNation.showWaterValues) {
				g.drawString("null", state.getWaterValuePoint().x + xOffset, state.getWaterValuePoint().y + yOffset);
			}
		}

		try {
			ImageIO.write((RenderedImage) MainFrame.getFullMapImage(), extension, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
