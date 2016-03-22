package com.aquanation.water.savers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Calendar;

import com.aquanation.water.AquaNation;
import com.aquanation.water.State;
import com.aquanation.water.windows.MainFrame;

/**
 * 
 * @author Nick Wilson
 * 
 *         Represents a system that is capable of printing the map image to a file
 *
 */
public class PrintManager implements Printable {

	PageFormat pf;
	private String name;

	@Override
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		// landscape
		pf.setOrientation(PageFormat.LANDSCAPE);

		// create graphics
		Graphics2D g2d = (Graphics2D) g;

		// custom text
		g2d.drawLine(0, (int) (pf.getHeight() - pf.getImageableY() - 20), (int) pf.getWidth(), (int) (pf.getHeight() - pf.getImageableY() - 20));
		Calendar calendar = Calendar.getInstance();
		String date = (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR);
		g2d.drawString(name + " -- " + date + " " + MainFrame.getSelectedDataType() + " Levels", (int) pf.getImageableX(), (int) (pf.getHeight() - pf.getImageableY() - 5));

		// center grid for map rendering
		g2d.translate((pf.getImageableWidth() - MainFrame.getFullMapImage().getWidth(null)) / 2 + pf.getImageableX(), (pf.getImageableHeight() - MainFrame.getFullMapImage().getHeight(null)) / 2 + pf.getImageableY() - 20);

		// map image w/ color
		g2d.drawImage(MainFrame.getFullMapImage(), 0, 0, null);

		// text overlay
		g2d.setColor(AquaNation.getFontColor());
		g2d.setFont(AquaNation.getFont());
		for (State state : AquaNation.getStates()) {
			// used values 30, 100 to offset the text points because of the JPanel positioning
			if (AquaNation.showStateNames) g2d.drawString(state.getPostalCode(), state.getPostalCodePoint().x - 30, state.getPostalCodePoint().y - 100);

			if (AquaNation.showWaterValues && state.getWaterValues().size() > 0) {
				g2d.drawString(String.valueOf(state.getWaterValue(MainFrame.getSelectedDataType())), state.getWaterValuePoint().x - 30, state.getWaterValuePoint().y - 100);
			} else {
				g2d.drawString("null", state.getWaterValuePoint().x - 30, state.getWaterValuePoint().y - 100);
			}
		}
		return PAGE_EXISTS;
	}

	public void setPrintname(String input) {
		name = input;
	}
}
