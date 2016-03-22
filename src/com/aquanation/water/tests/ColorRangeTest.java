package com.aquanation.water.tests;

import com.aquanation.water.colorer.ColorRange;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link ColorRange}
 *
 */
public class ColorRangeTest extends ATest {

	public ColorRangeTest() {
		System.out.println("Doing Color Range Tests");
		ColorRange range = new ColorRange(0.0d, 1.0d, 0);

		// Instantiation tests
		if (range.getColor() != 0) failed("color does not equal 0 even though it should because we just set it");
		if (range.getFrom() != 0.0d) failed("from does not equal 0.0d even though it should because we just set it");
		if (range.getTo() != 1.0d) failed("to does not equal 1.0d even though it should because we just set it");

		// Set tests
		range.setColor(100);
		if (range.getColor() != 100) failed("color does not equal 100 even though it should because we just set it");
		range.setFrom(10.0d);
		if (range.getFrom() != 10.0d) failed("from does not equal 10.0d even though it should because we just set it");
		range.setTo(100.0d);
		if (range.getTo() != 100.0d) failed("to does not equal 100.0d even though it should because we just set it");

		System.out.println("Passed Color Range Tests\n");
	}

	@Override
	void failed(String message) {
		throw new TestFailedException("Color Range: " + message);
	}

}
