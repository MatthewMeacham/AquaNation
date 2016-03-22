package com.aquanation.water.tests;

import java.io.File;

import com.aquanation.water.savers.MapDataSaver;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link MapDataSaver}
 *
 */
public class MapDataSaverTest extends ATest {

	public MapDataSaverTest() {
		System.out.println("Doing MapDataSaver Tests");

		File file = new File("MapDataSaver.mapdata");

		new MapDataSaver().save(file);

		if (!file.exists()) failed("file does not exist, save method doesn't work");

		file.delete();

		System.out.println("Passed MapDataSaver Tests\n");
	}

	@Override
	public void failed(String message) {
		throw new TestFailedException("MapDataSaver: " + message);
	}

}
