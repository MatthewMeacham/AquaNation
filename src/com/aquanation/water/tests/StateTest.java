package com.aquanation.water.tests;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import com.aquanation.water.State;
import com.aquanation.water.helpers.DataSet;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link State}
 *
 */
public class StateTest extends ATest {

	public StateTest() {
		System.out.println("Doing State Tests");
		State state = new State("TEST", "Test", new Point(100, 100), new Point(200, 200));

		// Simple instantiation tests, ensuring constructor works properly
		if (!state.getPostalCode().equals("TEST")) failed("Postal code does not equal given name");
		if (!state.getFullName().equals("Test")) failed("full name does not equal given name");
		if (state.getWaterValuePoint().getX() != 100.0d || state.getWaterValuePoint().getY() != 100.0d) failed("water value point does not equal given");
		if (state.getPostalCodePoint().getX() != 200.0d || state.getPostalCodePoint().getY() != 200.0d) failed("postal code point does not equal given");
		if (state.getOutline() != null) failed("outline does not equal null, but it should because none was passed into the constructor");
		if (state.getFillBucketPoints().size() > 0) failed("fill bucket points contains an element, but it shouldn't because none was set yet");
		if (state.getWaterValue("Arsenic") != null) failed("value for random data type does not equal null, it should because no value has been set");
		if (state.getWaterValue("afdsjfdkasj;lfkdSA") != null) failed("water value for nonsensical data type does not equal null, it should because that data type is not created");
		if (state.getWaterValues().size() < 3) failed("The number of data types is less than 3, it should also contain Arsenic, Radon, and Uranium");
		if (state.getWaterValues("Radon").size() > 0) failed("the size of the radon data ArrayList is greater than 0, shouldn't happen beacuse there is no data");
		if (state.getWaterValues("djkslafkjdsa") != null) failed("The ArrayList for a nonsensical data type exists, when it shouldn't");

		// Testing setters
		state.setOutline(new Polygon(new int[] { 1, 2 }, new int[] { 1, 2 }, 2));
		if (state.getOutline() == null) failed("Outline equals null, when it shouldn't because we just set it");
		state.setPostalCodePoint(new Point(150, 150));
		if (state.getPostalCodePoint().getX() != 150.0d || state.getPostalCodePoint().getY() != 150.0d) failed("postal code point does not equal what it should even though we just set it");
		state.setWaterValuePoint(new Point(250, 250));
		if (state.getWaterValuePoint().getX() != 250.0d || state.getWaterValuePoint().getY() != 250.0d) failed("State water value point does not equal what it should even though we just set it");
		state.setWaterValue("Radon", 1.0d, 0);
		if (state.getWaterValue("Radon") != 1.0d) failed("value for radon does not equal what we just set it as");
		state.setWaterValue("fdasjflk", 1.0d, 0);
		if (state.getWaterValue("fdasjflk") != null) failed("value for nonsensical data type does not equal null when it should because that data type doesn't exist");
		ArrayList<Double> waterValues = new ArrayList<Double>();
		waterValues.add(1.0d);
		waterValues.add(2.0d);
		waterValues.add(3.0d);
		state.setWaterValues("Radon", waterValues);
		if (state.getWaterValue("Radon") != 3.0d) failed("Radon value does not equal what it should even though we just set it");
		if (state.getWaterValues("Radon").get(0) != 1.0d || state.getWaterValues("Radon").get(1) != 2.0d || state.getWaterValues("Radon").get(2) != 3.0d) failed("Radon values do not equal what they should even though we just set them");
		ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
		dataSets.add(new DataSet("Plutonium"));
		state.setWaterValues(dataSets);
		if (state.getWaterValues().size() != 1) failed("Water value size did not equal one even though it should because we just set it");
		if (!state.getWaterValues().get(0).getDataType().equals("Plutonium")) failed("the first dataset does not equal what it should even though we just set it");

		// Testing add methods
		state.addDataSet("Colinium");
		if (state.getWaterValues().size() != 2) failed("water value array list size did not equal two even though it should");
		if (!state.getWaterValues().get(state.getWaterValues().size() - 1).getDataType().equals("Colinium")) failed("Added data set did not equal what it should even though we just set it");
		state.addFillBucketPoint(new Point(999, 999));
		if (state.getFillBucketPoints().size() != 1) failed("fill bucket points size does not equal one even though it should because we just added one");
		if (state.getFillBucketPoint(0).getX() != 999.0d || state.getFillBucketPoint(0).getY() != 999.0d) failed("");
		state.addWaterValue(2.0d, "Colinium");
		if (state.getWaterValue("Colinium") != 2.0d) failed("water value does not equal what it should even though we just set it");

		System.out.println("Passed State Tests\n");
	}

	@Override
	void failed(String message) {
		failed("State: " + message);
	}
}
