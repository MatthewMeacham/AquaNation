package com.aquanation.water.tests;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import com.aquanation.water.State;
import com.aquanation.water.AquaNation;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link AquaNation}
 *
 */
public class WaterSampleTest extends ATest {

	public WaterSampleTest() {
		System.out.println("Doing WaterSample Tests");

		// Init states method
		if (AquaNation.getStates().get(0) == null) failed("initStates does not work properly");

		// Test set methods
		AquaNation.setFont(new Font("Verdana", Font.BOLD, 15));
		if (!AquaNation.getFont().equals(new Font("Verdana", Font.BOLD, 15))) failed("set font does not work properly");
		AquaNation.setFontColor(Color.RED);
		if (!AquaNation.getFontColor().equals(Color.RED)) failed("set font color does not work properly");
		ArrayList<State> states = new ArrayList<State>();
		states.add(new State("AA", "AAtown", new Point(0, 0), new Point(0, 0)));
		states.add(new State("BB", "BBtown", new Point(1, 1), new Point(1, 1)));
		AquaNation.setStates(states);
		if (AquaNation.getStates().size() != 2) failed("set states method does not work properly");
		if (!AquaNation.getStates().get(0).getPostalCode().equals("AA")) failed("set states method does not work properly");

		System.out.println("Passed WaterSample Tests\n");
	}

	@Override
	public void failed(String message) {
		throw new TestFailedException("WaterSample: " + message);
	}

}
