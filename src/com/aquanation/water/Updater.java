package com.aquanation.water;

import java.awt.Font;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

//This class handles updating when needed
public class Updater implements PropertyChangeListener {

	/** THE DOWNLOAD_URL NEEDS TO BEGIN WITH https://dl.dropboxusercontent.com/ */
	// the url from where it downloads the jar file
	private final String DOWNLOAD_URL = "https://dl.dropboxusercontent.com/s/inqz0icvjsvplme/AquaNation.jar";
	// what the downloaded file should be called
	private final String FILE_NAME = "AquaNation.jar";
	// the URL for the check for update file
	private final String UPDATE_VERSION_URL = "https://dl.dropboxusercontent.com/s/zyxmsb809ep50le/version.txt";

	private final int WIDTH = 800;
	private final int HEIGHT = 600;

	private final int PROGRESS_BAR_WIDTH = 500;
	private final int PROGRESS_BAR_HEIGHT = 50;

	private final String TITLE = "Updater";
	private final String UPDATE_LABEL_TEXT = "UPDATING";
	private final String DO_NOT_CLOSE_WINDOW_LABEL_TEXT = "Do not close the window.";
	private final Font FONT = new Font("Verdana", Font.BOLD, 32);

	private JFrame frame;
	private JPanel panel = null;

	// Should be a power of 2
	private final int MAX_DATA_TO_DOWNLOAD_AT_A_TIME = 1024;

	private JProgressBar progressBar;
	private JLabel updateLabel;
	private JLabel doNotCloseWindowLabel;

	public Updater() {
		if (checkForUpdate()) {
			System.out.println("AquaNation needs to be updated");
			initFrame();
			update();

			try {
				Runtime.getRuntime().exec("java -jar " + FILE_NAME);
			} catch (IOException e) {

			}
			System.exit(0);
		}
	}

	private void initFrame() {
		frame = new JFrame(TITLE);

		panel = new JPanel(true);
		panel.setLayout(null);
		panel.setSize(WIDTH, HEIGHT);

		// Grabs a graphics component so that we can get the update label in the perfect center
		frame.setVisible(true);
		Graphics2D g2 = (Graphics2D) frame.getGraphics();
		frame.setVisible(false);

		updateLabel = new JLabel(UPDATE_LABEL_TEXT);
		updateLabel.setFont(FONT);
		int stringWidth = g2.getFontMetrics(FONT).stringWidth(UPDATE_LABEL_TEXT);
		updateLabel.setSize(WIDTH, 50);
		updateLabel.setLocation(WIDTH / 2 - stringWidth / 2, 200);

		doNotCloseWindowLabel = new JLabel(DO_NOT_CLOSE_WINDOW_LABEL_TEXT);
		doNotCloseWindowLabel.setFont(FONT);
		stringWidth = g2.getFontMetrics(FONT).stringWidth(DO_NOT_CLOSE_WINDOW_LABEL_TEXT);
		doNotCloseWindowLabel.setSize(WIDTH, 50);
		doNotCloseWindowLabel.setLocation(WIDTH / 2 - stringWidth / 2, HEIGHT / 2 + 25);

		progressBar = new JProgressBar(0, 100);
		progressBar.setFont(FONT);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setSize(PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
		progressBar.setLocation(150, HEIGHT / 2 - PROGRESS_BAR_HEIGHT);

		panel.add(progressBar);
		panel.add(updateLabel);
		panel.add(doNotCloseWindowLabel);

		frame.add(panel);
		frame.pack();

		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		// Do not allow them to close the frame
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);

		frame.setVisible(true);
	}

	private boolean checkForUpdate() {
		try {
			URL url = new URL(UPDATE_VERSION_URL);
			Scanner scanner = new Scanner(url.openStream());

			String version = scanner.nextLine();
			scanner.close();

			return version.equals(AquaNation.VERSION) ? false : true;
		} catch (IOException e) {
			// This could happen if they aren't connected to internet so we need to make sure the application will continue on
			e.printStackTrace();
			return false;
		}
	}

	private void update() {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			URL url = new URL(DOWNLOAD_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			float fileSize = connection.getContentLength();
			bis = new BufferedInputStream(connection.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(FILE_NAME), MAX_DATA_TO_DOWNLOAD_AT_A_TIME);

			float totalDataRead = 0f;
			int i = 0;
			byte[] data = new byte[MAX_DATA_TO_DOWNLOAD_AT_A_TIME];
			float percent = 0;
			while ((i = bis.read(data, 0, MAX_DATA_TO_DOWNLOAD_AT_A_TIME)) >= 0) {
				totalDataRead += i;
				bos.write(data, 0, i);
				percent = (totalDataRead * 100.0f / fileSize);
				progressBar.setValue((int) Math.round(percent));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) bos.close();
				if (bis != null) bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals("progress")) {
			System.out.println("PROGRESS");
		}
	}

}
