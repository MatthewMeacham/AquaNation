package com.aquanation.water.windows;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * 
 * @author Nick Wilson
 * 
 *         The frame that will contain the help documentation
 *
 */
public class HelpFrame {

	private int width = 500;
	private int height = 500;

	private JFrame main;
	private JEditorPane viewer;
	private JScrollPane scroll;
	private JPanel inputPanel;

	private File index;
//	private File mainInterface;
//	private File toolbar;

	// Must define all the temporary file names for each file used in the help doc
	public final static String TEMPORARY_INDEX_FILE_NAME = "index.tmp";
	public final static String TEMPORARY_MAIN_INTERFACE_FILE_NAME = "mainInterface.tmp";
	public final static String TEMPORARY_TOOLBAR_FILE_NAME = "toolbar.tmp";

	public HelpFrame() {
		loadDocs();
		render();
	}

	private void loadDocs() {
		index = loadFile("index.html", TEMPORARY_INDEX_FILE_NAME);
		loadFile("mainInterface.html", TEMPORARY_MAIN_INTERFACE_FILE_NAME);
		loadFile("toolbar.html", TEMPORARY_TOOLBAR_FILE_NAME);
	}

	private File loadFile(final String fileName, final String temporaryFileName) {
		InputStream is = getClass().getResourceAsStream("/helpDocs/" + fileName);

		try {
			byte[] buffer = new byte[is.available()];
			is.read(buffer);

			File targetFile = new File(temporaryFileName);

			OutputStream os = new FileOutputStream(targetFile);
			os.write(buffer);
			os.close();

			return targetFile;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void render() {
		main = new JFrame("Help");

		// top panel for inputs
		inputPanel = new JPanel();
		inputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		inputPanel.setLayout(new BorderLayout());
		main.add(inputPanel, BorderLayout.NORTH);

		// returns to home help page
		JButton homebtn = new JButton("Back");
		homebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					viewer.setPage(index.toURI().toURL());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		inputPanel.add(homebtn, BorderLayout.WEST);

		// opens email box
		JButton email = new JButton("Email Us");
		email.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					URI uriMailTo = new URI("mailto:nwilson126@gmail.com");
					try {
						Desktop.getDesktop().mail(uriMailTo);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		inputPanel.add(email, BorderLayout.EAST);

		// html window
		viewer = new JEditorPane();
		viewer.setContentType("text/html");
		try {
			viewer.setPage(index.toURI().toURL());
		} catch (IOException e) {
			e.printStackTrace();
			viewer.setText("That's weird! Our program can't find the help documents...");
		}
		viewer.setEditable(false);

		// allowing <a> links to work inside the viewer
		viewer.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				try {
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						viewer.setPage(e.getURL());
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					viewer.setText("It seems there was a problem with that link, click the home button to return to where you were...");
				}
			}
		});

		// scrollbars
		scroll = new JScrollPane(viewer);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(new Rectangle(width, height - 50));
		scroll.setPreferredSize(new Dimension(width, height - 50));
		main.add(scroll, BorderLayout.SOUTH);

		// render window
		main.setResizable(false);
		main.pack();
		main.setVisible(true);
		main.setLocationRelativeTo(null);
		main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

}
