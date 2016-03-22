package com.aquanation.water.tests;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Represents a simple Test
 *
 */
public abstract class ATest {

	void failed() {
		throw new TestFailedException();
	}

	abstract void failed(String message);

}
