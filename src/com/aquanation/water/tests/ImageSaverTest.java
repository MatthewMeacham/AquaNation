package com.aquanation.water.tests;

import java.io.File;

import com.aquanation.water.savers.ImageSaver;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link ImageSaver}
 *
 */
public class ImageSaverTest extends ATest {

	public ImageSaverTest() {
		System.out.println("Doing ImageSaver Tests");

		File file = new File("ImageSaverTest.png");
		File file2 = new File("ImageSaverTest.jpg");
		File file3 = new File("ImageSaverTest.jpeg");
		File file4 = new File("ImageSaverTest.gif");

		new ImageSaver().save(file);
		new ImageSaver().save(file2);
		new ImageSaver().save(file3);
		new ImageSaver().save(file4);

		if (!file.exists()) failed("png file does not exist, save method doesn't work");
		if (!file2.exists()) failed("jpg file does not exist, save method doesn't work");
		if (!file3.exists()) failed("jpeg file does not exist, save method doesn't work");
		if (!file4.exists()) failed("gif file does not exist, save method doesn't work");

		file.delete();
		file2.delete();
		file3.delete();
		file4.delete();

		System.out.println("Passed ImageSaver Tests\n");
	}

	@Override
	public void failed(String message) {
		throw new TestFailedException("ImageSaver: " + message);
	}

}
