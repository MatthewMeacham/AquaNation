package com.aquanation.water.tests;

import java.io.File;

import com.aquanation.water.savers.TextFileSaver;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link TextFileSaver}
 *
 */
public class TextFileSaverTest extends ATest {

	public TextFileSaverTest() {
		System.out.println("Doing TextFileSaver Tests");

		File file = new File("TextFileSaverTest.txt");

		new TextFileSaver().save(file);

		if (!file.exists()) failed("file does not exist, save method does not work");

		file.delete();

		System.out.println("Passed TextFileSaver Tests\n");
	}

	@Override
	public void failed(String message) {
		throw new TestFailedException("TextFileSaver: " + message);
	}

}
