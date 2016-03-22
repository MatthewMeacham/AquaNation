package com.aquanation.water.tests;

import com.aquanation.water.AquaNation;

/**
 * 
 * @author Matthew Meacham
 * 
 *         The main class for all the tests that need to be ran
 *
 */
public class MainTest {

	public MainTest() {
		System.out.println("Doing Tests\n");
		// Create WaterSample because some classes require the instantiation of this in order to function
		new AquaNation();

		new StateTest();
		new ColorRangeTest();
		new StateComparatorTest();
		new DataSetTest();
		new RangeTest();
		new ColorerTest();
		new CSVSaverTest();
		new ImageSaverTest();
		new MapDataSaverTest();
		new TextFileSaverTest();

		// Run this last because we directly mess with some very important variables that could influence other tests
		new WaterSampleTest();

		System.out.println("All tests passed");
		System.exit(0);
	}

	public static void main(String[] args) {
		new MainTest();
	}

}
