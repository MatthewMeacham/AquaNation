package com.aquanation.water.tests;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.aquanation.water.AquaNation;
import com.aquanation.water.colorer.ColorRange;
import com.aquanation.water.colorer.Colorer;
import com.aquanation.water.windows.MainFrame;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link Colorer}
 *
 */
public class ColorerTest extends ATest {

	public ColorerTest() {
		System.out.println("Doing Colorer Tests");

		Colorer colorer = new Colorer();

		// Remove tests
		colorer.removeAllColorRanges();
		if (colorer.getColorRanges().size() != 0) failed("color ranges size does not equal 0 even though it should");
		colorer.addColorRange(0.0d, 1.0d, 50);
		if (colorer.getColorRanges().size() != 1) failed("color ranges size does not equal 1 even though it should");
		colorer.removeColorRange(0.0d, 1.0d);
		if (colorer.getColorRanges().size() != 0) failed("color ranges size does not equal 0 even though it should");

		// Test add methods
		colorer.addColorRange(0.0d, 2.0d, 255, 0, 0, 255);
		if (colorer.getColorRanges().get(0).getColor() != Color.RED.getRGB()) failed("Color creation did not work");

		// Test create color method
		int color = colorer.createColor(255, 0, 0, 255);
		if (!(new Color(color)).equals(Color.RED)) failed("create color does not work");

		AquaNation.getStates().get(0).addWaterValue(1.0d, "Radon");
		BufferedImage img = (BufferedImage) colorer.fillImage(MainFrame.getDefaultFullMapImage(), "Radon", 0);
		if (img.getRGB(AquaNation.getStates().get(0).getFillBucketPoint(0).x, AquaNation.getStates().get(0).getFillBucketPoint(0).y) != Color.RED.getRGB()) failed("Fill image with parameters does not work");

		// Switch to MainFrame colorer to test if it works with the more often called fillImage that returns void and sets the fullMapImage directly
		MainFrame.colorer.fillImage(MainFrame.getDefaultFullMapImage());
		MainFrame.colorer.removeAllColorRanges();
		MainFrame.colorer.addColorRange(0.0d, 2.0d, colorer.createColor(255, 0, 0, 255));
		if (((BufferedImage) MainFrame.getFullMapImage()).getRGB(AquaNation.getStates().get(0).getFillBucketPoint(0).x, AquaNation.getStates().get(0).getFillBucketPoint(0).y) == 16777215) failed("Fill image does not work");
		if (((BufferedImage) MainFrame.getFullMapImage()).getRGB(AquaNation.getStates().get(0).getFillBucketPoint(0).x, AquaNation.getStates().get(0).getFillBucketPoint(0).y) == Color.RED.getRGB()) failed("Fill image does not work");

		ArrayList<ColorRange> colorRanges = new ArrayList<ColorRange>();
		colorRanges.add(new ColorRange(0.0d, 1.0d, 20));
		colorRanges.add(new ColorRange(1.0d, 2.0d, 30));
		colorer.setColorRanges(colorRanges);
		if (colorer.getColorRanges().size() != 2) failed("color ranges size does not equal 2 even though it should");
		if (colorer.getColorRanges().get(0).getFrom() != 0.0d) failed("color ranges first element does not equal 0.0d even though it should");
		if (colorer.getColorRanges().get(1).getTo() != 2.0d) failed("color ranges second element does not equal 2.0d even though it should");
		if (colorer.getColorRanges().get(0).getColor() != 20) failed("color ranges first element color does not equal 20 even though it should");

		System.out.println("Passed Colorer Tests\n");
	}

	@Override
	public void failed(String message) {
		throw new TestFailedException("Colorer: " + message);
	}

}
