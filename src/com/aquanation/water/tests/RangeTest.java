package com.aquanation.water.tests;

import com.aquanation.water.helpers.Range;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link Range}
 *
 */
public class RangeTest extends ATest {

	public RangeTest() {
		System.out.println("Doing Range Tests");

		Range range = new Range(0.0d, 1.0d);

		// Instantiation tests
		if (range.getFrom() != 0.0d) failed("from does not equal 0.0d even though it should");
		if (range.getTo() != 1.0d) failed("to does not equal 1.0d even though it should");

		System.out.println("Passed Range Tests\n");
	}

	@Override
	void failed(String message) {
		throw new TestFailedException("Range: " + message);
	}

}
