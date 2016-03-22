package com.aquanation.water.tests;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Represents an exception that would occur if a test failed
 *
 */
public class TestFailedException extends RuntimeException {
	private static final long serialVersionUID = 3745766908614361456L;

	public TestFailedException() {
		super();
	}

	public TestFailedException(String message) {
		super(message);
	}

}
