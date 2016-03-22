package com.aquanation.water.tests;

import java.util.ArrayList;

import com.aquanation.water.helpers.DataSet;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link DataSet}
 *
 */
public class DataSetTest extends ATest {

	public DataSetTest() {
		System.out.println("Doing DataSet Tests");

		DataSet dataSet = new DataSet("Yongium");

		// Instantiation Tests
		if (!dataSet.getDataType().equals("Yongium")) failed("data type does not equal what it should");
		if (dataSet.getValues().size() != 0) failed("size does not equal 0 even though it should");

		// Add test
		dataSet.addValue(1.0d);
		if (dataSet.getValues().size() != 1) failed("size does not equal 1 even though it should");
		if (dataSet.getValues().get(0) != 1.0d) failed("first element does not equal 1.0d even though it should");

		// Remove test
		dataSet.removeValue(0);
		if (dataSet.getValues().size() != 0) failed("size does not equal 0 even though it should");

		// Set tests
		ArrayList<Double> values = new ArrayList<Double>();
		values.add(1.0d);
		values.add(2.0d);
		dataSet.setValues(values);
		if (dataSet.getValues().size() != 2) failed("size does not equal 2 even though it should");
		if (dataSet.getValues().get(0) != 1.0d) failed("first element does not equal 1.0d even though it should");
		if (dataSet.getValues().get(1) != 2.0d) failed("second element does not equal 2.0d even though it should");

		System.out.println("Passed DataSet Tests\n");
	}

	@Override
	void failed(String message) {
		throw new TestFailedException("DataSet: " + message);
	}

}
