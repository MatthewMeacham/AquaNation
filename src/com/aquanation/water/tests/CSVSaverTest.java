package com.aquanation.water.tests;

import java.io.File;

import com.aquanation.water.savers.CSVSaver;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link CSVSaver}
 *
 */
public class CSVSaverTest extends ATest {

	public CSVSaverTest() {
		System.out.println("Doing CSVSaver Tests");

		File file = new File("CSVSaverTest.csv");

		// We won't check the veracity of the exported file, only ensure that it exports successfully
		new CSVSaver().save(file);
		if (!file.exists()) failed("file does not exist, save method does not work");

		file.delete();

		System.out.println("Passed CSVSaver Tests\n");
	}

	@Override
	public void failed(String message) {
		throw new TestFailedException("CSVSaver: " + message);
	}

}
