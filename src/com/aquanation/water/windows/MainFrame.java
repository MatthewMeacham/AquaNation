package com.aquanation.water.windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.aquanation.water.AquaNation;
import com.aquanation.water.State;
import com.aquanation.water.colorer.Colorer;
import com.aquanation.water.listeners.DoubleOnlyListener;
import com.aquanation.water.listeners.FrameMouseListener;
import com.aquanation.water.listeners.TextOnlyListener;
import com.aquanation.water.savers.CSVSaver;
import com.aquanation.water.savers.ImageSaver;
import com.aquanation.water.savers.MapDataSaver;
import com.aquanation.water.savers.PrintManager;
import com.aquanation.water.savers.TextFileSaver;
import com.aquanation.water.windows.analysis.MapAnalysisFrame;
import com.aquanation.water.windows.analysis.slideshow.MapSlideshowFrame;
import com.aquanation.water.windows.preferences.PreferencesFrame;

/**
 * 
 * @author Matthew Meacham
 * @author Nick Wilson
 * @author Neil Band
 * 
 *         The frame that contains the image and input panel and is the first entry point upon application start
 *
 */

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -1174645574682379777L;

	private JPanel contentPane;
	private static JPanel imagePanel;
	private static JPanel inputPanel;
	private static JLabel imageLabel;
	private static ImageIcon imageIcon;
	private JTextField postalCodeTextBox1;
	private JTextField waterSampleValueTextBox;
	private JTextField postalCodeTextBox2;
	private JLabel waterSampleValueLabel;

	public static Colorer colorer = new Colorer();

	private final String TITLE = "Water Samples";

	private static int width = 1000;
	private int height = 600;
	private static double newWidth;
	private static double newHeight;

	private int textBoxHeight = 30;

	private static Image defaultFullMapImage;
	private static Image fullMapImage;

	// These Strings are in the form [OPERATION {currently only ADD}] [STATE POSTAL NAME] [WATER VALUE]
	// Notice it is all UPPERCASE
	private static Stack<String> history = new Stack<String>();
	private static Stack<String> undoHistory = new Stack<String>();
	private JTextField valueSetNameTextField;

	private static JComboBox<String> waterValuesComboBox;
	private static DefaultComboBoxModel<String> waterValuesComboBoxModel;

	// The default width and height for the frame
	private static final double DEFAULT_WIDTH = 1000 / 3 * 2 - 100;
	private static final double DEFAULT_HEIGHT = 400;
	// Adjusting ratio for the image from default to new scale
	private final double ADJUSTING_RATIO = (368.0d / 589.0d);

	// Accounts for pixels on the side so that the image won't go out of the JFrame
	private final int BUFFER = 50;

	public MainFrame() {
		setBounds(new Rectangle(0, 0, width, height));
		setPreferredSize(new Dimension(width, height));
		setVisible(true);
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, width, height);
		setLocationRelativeTo(null);
		setFocusable(true);

		// close confirmation
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int response = JOptionPane.showConfirmDialog(null, "Any unsaved work will be lost. Are you sure you want to close?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				// TODO all files created in help frame need to be deleted here
				if (response == JOptionPane.YES_OPTION) {
					try {
						Files.deleteIfExists(Paths.get(HelpFrame.TEMPORARY_INDEX_FILE_NAME));
						Files.deleteIfExists(Paths.get(HelpFrame.TEMPORARY_MAIN_INTERFACE_FILE_NAME));
						Files.deleteIfExists(Paths.get(HelpFrame.TEMPORARY_TOOLBAR_FILE_NAME));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.exit(0);
				}
			}
		});

		// Toolbar (menubar) components
		JMenuBar menuBar = new JMenuBar();
		menuBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenu exportMenu = new JMenu("Export");
		fileMenu.add(exportMenu);

		JMenuItem exportAsPDFMenuItem = new JMenuItem("As PDF");
		exportMenu.add(exportAsPDFMenuItem);

		JMenuItem exportAsCSVMenuItem = new JMenuItem("As CSV");
		exportAsCSVMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save As CSV");
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setSelectedFile(new File("MapData.csv"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
				int selection = fileChooser.showSaveDialog(null);

				if (selection == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					CSVSaver csv = new CSVSaver();
					csv.save(file);
				}
			}
		});
		exportMenu.add(exportAsCSVMenuItem);

		JMenuItem exportAsTextFileMenuItem = new JMenuItem("As Text File");
		exportAsTextFileMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save As Text File");
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setSelectedFile(new File("MapData.txt"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
				int selection = fileChooser.showSaveDialog(null);

				if (selection == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					TextFileSaver textFile = new TextFileSaver();
					textFile.save(file);
				}
			}
		});
		exportMenu.add(exportAsTextFileMenuItem);

		JMenuItem exportAsImageMenuItem = new JMenuItem("As Image");
		exportAsImageMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save As Image");
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setSelectedFile(new File("Map.png"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif"));
				int selection = fileChooser.showSaveDialog(null);

				if (selection == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					ImageSaver imageSaver = new ImageSaver();
					imageSaver.save(file);
				}

			}
		});
		exportMenu.add(exportAsImageMenuItem);

		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Open");
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileFilter(new FileNameExtensionFilter("Map Data Files", "mapdata"));
				int selection = fileChooser.showOpenDialog(null);

				if (selection == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					MapDataSaver mapDataSaver = new MapDataSaver();
					mapDataSaver.load(file);
				}
			}
		});
		fileMenu.add(openMenuItem);

		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save");
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setSelectedFile(new File("Map.mapdata"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Map Data Files", "mapdata"));
				int selection = fileChooser.showSaveDialog(null);

				if (selection == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					MapDataSaver mapDataSaver = new MapDataSaver();
					mapDataSaver.save(file);
				}
			}
		});
		fileMenu.add(saveMenuItem);

		JMenuItem printMenuItem = new JMenuItem("Print");
		printMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		fileMenu.add(printMenuItem);
		printMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrinterJob job = PrinterJob.getPrinterJob();
				PrintManager pm = new PrintManager();
				PageFormat pf = new PageFormat();
				pf.setOrientation(PageFormat.LANDSCAPE);
				job.setPrintable(pm, pf);
				pm.setPrintname(JOptionPane.showInputDialog(null, "Please enter a name for this print", "Print name", JOptionPane.PLAIN_MESSAGE));

				// displays print window
				if (job.printDialog()) {
					try {
						job.print();
					} catch (PrinterException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);

		JMenuItem undoMenuItem = new JMenuItem("Undo");
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		undoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// This is where the undo operation happens on our stack
				if (history.size() <= 0) return;
				String undoString = history.pop();
				undoHistory.push(undoString);
				for (State state : AquaNation.getStates()) {
					if (state.getPostalCode().equals(undoString.split(" ")[1])) {
						String undoDataType = undoString.split(" ")[2];
						if (state.getWaterValues(undoString.split(" ")[2]).size() > 0) {
							ArrayList<Double> replacementArrayList = new ArrayList<Double>(state.getWaterValues(undoDataType).size());
							for (Double value : state.getWaterValues(undoDataType)) {
								replacementArrayList.add(value);
							}
							replacementArrayList.remove(replacementArrayList.size() - 1);
							state.setWaterValues(undoDataType, replacementArrayList);
						}
						imagePanel.repaint();
						break;
					}
				}
				colorer.fillImage(getDefaultFullMapImage());
			}
		});
		editMenu.add(undoMenuItem);

		JMenuItem redoMenuItem = new JMenuItem("Redo");
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		redoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// This is where the redo operation happens on our stack
				if (undoHistory.size() <= 0) return;
				String undoHistoryString = undoHistory.pop();
				history.push(undoHistoryString);
				for (State state : AquaNation.getStates()) {
					if (state.getPostalCode().equals(undoHistoryString.split(" ")[1])) {
						setStateWaterValues(state, undoHistoryString.split(" ")[3]);
						MainFrame.getImagePanel().repaint();
						break;
					}
				}
				colorer.fillImage(getDefaultFullMapImage());
			}
		});
		editMenu.add(redoMenuItem);

		JMenu optionsMenu = new JMenu("Options");
		menuBar.add(optionsMenu);

		JMenuItem preferencesMenuItem = new JMenuItem("Preferences");
		preferencesMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new PreferencesFrame();
			}
		});
		optionsMenu.add(preferencesMenuItem);

		JMenu viewMenu = new JMenu("View");
		menuBar.add(viewMenu);

		JCheckBoxMenuItem showInputPanelCheckBox = new JCheckBoxMenuItem("Show Input Panel");
		showInputPanelCheckBox.setSelected(true);
		showInputPanelCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (showInputPanelCheckBox.isSelected()) inputPanel.setVisible(true);
				else inputPanel.setVisible(false);
			}
		});
		viewMenu.add(showInputPanelCheckBox);

		JMenu analysisMenu = new JMenu("Analysis");
		menuBar.add(analysisMenu);

		JMenuItem mapSlideShowMenuItem = new JMenuItem("Map Slideshow");
		mapSlideShowMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
		mapSlideShowMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MapSlideshowFrame();
			}
		});
		analysisMenu.add(mapSlideShowMenuItem);

		JMenuItem mapHistogramMenuItem = new JMenuItem("Map Analysis Tools");
		mapHistogramMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MapAnalysisFrame();
			}
		});
		analysisMenu.add(mapHistogramMenuItem);

		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);

		JMenuItem helpMenuItem = new JMenuItem("Help");
		helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		helpMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new HelpFrame();
			}
		});
		helpMenu.add(helpMenuItem);
		// End toolbar components

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		imagePanel = new JPanel();
		imagePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(imagePanel, BorderLayout.CENTER);
		imagePanel.setLayout(new GridLayout(1, 1, 0, 0));
		FrameMouseListener frameMouseListener = new FrameMouseListener();
		imagePanel.addMouseListener(frameMouseListener);
		imagePanel.addMouseMotionListener(frameMouseListener);

		inputPanel = new JPanel();
		inputPanel.setMaximumSize(new Dimension(300, height));
		inputPanel.setPreferredSize(new Dimension(300, contentPane.getHeight()));
		contentPane.add(inputPanel, BorderLayout.EAST);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

		JLabel enterThePostalCodeLabel1 = new JLabel("Enter the postal code or full state name:");
		enterThePostalCodeLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.add(enterThePostalCodeLabel1);

		Component verticalStrut1 = Box.createVerticalStrut(20);
		verticalStrut1.setPreferredSize(new Dimension(0, 5));
		verticalStrut1.setMinimumSize(new Dimension(0, 5));
		verticalStrut1.setMaximumSize(new Dimension(32767, 5));
		inputPanel.add(verticalStrut1);

		postalCodeTextBox1 = new JTextField();
		postalCodeTextBox1.setMaximumSize(new Dimension(233, textBoxHeight));
		postalCodeTextBox1.setPreferredSize(new Dimension(50, textBoxHeight));
		postalCodeTextBox1.setBounds(new Rectangle(0, 0, 50, textBoxHeight));
		postalCodeTextBox1.getDocument().addDocumentListener(new TextOnlyListener(postalCodeTextBox1, true));
		inputPanel.add(postalCodeTextBox1);
		postalCodeTextBox1.setColumns(10);

		Component verticalStrut2 = Box.createVerticalStrut(20);
		verticalStrut2.setPreferredSize(new Dimension(0, 5));
		verticalStrut2.setMinimumSize(new Dimension(0, 5));
		verticalStrut2.setMaximumSize(new Dimension(32767, 5));
		inputPanel.add(verticalStrut2);

		JLabel enterTheWaterSampleValueLabel = new JLabel("Enter the water sample value:");
		enterTheWaterSampleValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.add(enterTheWaterSampleValueLabel);

		Component verticalStrut3 = Box.createVerticalStrut(20);
		verticalStrut3.setPreferredSize(new Dimension(0, 5));
		verticalStrut3.setMinimumSize(new Dimension(0, 5));
		verticalStrut3.setMaximumSize(new Dimension(32767, 5));
		inputPanel.add(verticalStrut3);

		waterSampleValueTextBox = new JTextField();
		waterSampleValueTextBox.setPreferredSize(new Dimension(233, textBoxHeight));
		waterSampleValueTextBox.setMaximumSize(new Dimension(233, textBoxHeight));
		waterSampleValueTextBox.getDocument().addDocumentListener(new DoubleOnlyListener(waterSampleValueTextBox));
		inputPanel.add(waterSampleValueTextBox);
		waterSampleValueTextBox.setColumns(10);
		waterSampleValueTextBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = postalCodeTextBox1.getText().toLowerCase();
				// If they aren't using the postal code (length is greater than 2) check for full state names instead
				if (text.length() > 2) {
					for (State state : AquaNation.getStates()) {
						if (state.getFullName().toLowerCase().equals(text)) {
							setStateWaterValues(state, waterSampleValueTextBox.getText());
							break;
						}
					}
				} else {
					for (State state : AquaNation.getStates()) {
						if (state.getPostalCode().toLowerCase().equals(text)) {
							setStateWaterValues(state, waterSampleValueTextBox.getText());
							break;
						}
					}
				}
				// Reset the text box values
				waterSampleValueTextBox.setText("");
				postalCodeTextBox1.setText("");

				colorer.fillImage(getDefaultFullMapImage());
				repaint();
				// Switch to the next component backwards however
				KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner().transferFocusBackward();
			}
		});

		Component verticalStrut4 = Box.createVerticalStrut(20);
		verticalStrut4.setPreferredSize(new Dimension(0, 5));
		verticalStrut4.setMinimumSize(new Dimension(0, 5));
		verticalStrut4.setMaximumSize(new Dimension(32767, 5));
		inputPanel.add(verticalStrut4);

		JButton setValueButton = new JButton("Set Value");
		setValueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		setValueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = postalCodeTextBox1.getText().toLowerCase();
				// If they aren't using the postal code (length is greater than 2) check for full state names instead
				if (text.length() > 2) {
					for (State state : AquaNation.getStates()) {
						if (state.getFullName().toLowerCase().equals(text)) {
							setStateWaterValues(state, waterSampleValueTextBox.getText());
							break;
						}
					}
				} else {
					for (State state : AquaNation.getStates()) {
						if (state.getPostalCode().toLowerCase().equals(text)) {
							setStateWaterValues(state, waterSampleValueTextBox.getText());
							break;
						}
					}
				}
				waterSampleValueTextBox.setText("");
				postalCodeTextBox1.setText("");

				colorer.fillImage(getDefaultFullMapImage());
				repaint();
			}
		});
		inputPanel.add(setValueButton);

		Component verticalStrut5 = Box.createVerticalStrut(20);
		inputPanel.add(verticalStrut5);

		JLabel enterThePostalCodeLabel2 = new JLabel("Enter the postal code or full state name:");
		enterThePostalCodeLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.add(enterThePostalCodeLabel2);

		Component verticalStrut6 = Box.createVerticalStrut(20);
		verticalStrut6.setPreferredSize(new Dimension(0, 5));
		verticalStrut6.setMinimumSize(new Dimension(0, 5));
		verticalStrut6.setMaximumSize(new Dimension(32767, 5));
		inputPanel.add(verticalStrut6);

		postalCodeTextBox2 = new JTextField();
		postalCodeTextBox2.setMaximumSize(new Dimension(233, textBoxHeight));
		postalCodeTextBox2.getDocument().addDocumentListener(new TextOnlyListener(postalCodeTextBox2, true));
		inputPanel.add(postalCodeTextBox2);
		postalCodeTextBox2.setColumns(10);
		postalCodeTextBox2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = postalCodeTextBox2.getText().toLowerCase();
				String dataType = getSelectedDataType();
				if (text.length() > 2) {
					for (State state : AquaNation.getStates()) {
						if (state.getFullName().toLowerCase().equals(text)) waterSampleValueLabel.setText("Value: " + state.getWaterValue(dataType));
					}
				} else {
					for (State state : AquaNation.getStates()) {
						if (state.getPostalCode().toLowerCase().equals(text)) waterSampleValueLabel.setText("Value: " + state.getWaterValue(dataType));
					}
				}
				postalCodeTextBox2.setText("");
				repaint();
			}
		});

		Component verticalStrut7 = Box.createVerticalStrut(20);
		verticalStrut7.setPreferredSize(new Dimension(0, 5));
		verticalStrut7.setMinimumSize(new Dimension(0, 5));
		verticalStrut7.setMaximumSize(new Dimension(32767, 5));
		inputPanel.add(verticalStrut7);

		// this component is added under the strut beneath the getValueButton
		// component
		waterSampleValueLabel = new JLabel("Value: ");
		waterSampleValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton getValueButton = new JButton("Get Value");
		getValueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		getValueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = postalCodeTextBox2.getText().toLowerCase();
				String dataType = getSelectedDataType();
				if (text.length() > 2) {
					for (State state : AquaNation.getStates()) {
						if (state.getFullName().toLowerCase().equals(text)) waterSampleValueLabel.setText("Value: " + state.getWaterValue(dataType));
					}
				} else {
					for (State state : AquaNation.getStates()) {
						if (state.getPostalCode().toLowerCase().equals(text)) waterSampleValueLabel.setText("Value: " + state.getWaterValue(dataType));
					}
				}
				postalCodeTextBox2.setText("");
				repaint();
			}
		});
		inputPanel.add(getValueButton);

		Component verticalStrut8 = Box.createVerticalStrut(20);
		verticalStrut8.setMaximumSize(new Dimension(32767, 10));
		verticalStrut8.setMinimumSize(new Dimension(0, 10));
		verticalStrut8.setPreferredSize(new Dimension(0, 10));
		inputPanel.add(verticalStrut8);

		inputPanel.add(waterSampleValueLabel);

		Component verticalStrut9 = Box.createVerticalStrut(20);
		verticalStrut9.setMaximumSize(new Dimension(32767, 25));
		verticalStrut9.setMinimumSize(new Dimension(0, 40));
		verticalStrut9.setPreferredSize(new Dimension(0, 40));
		inputPanel.add(verticalStrut9);

		JCheckBox showStateNamesCheckBox = new JCheckBox("Show State Names");
		showStateNamesCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		showStateNamesCheckBox.setSelected(true);
		showStateNamesCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AquaNation.showStateNames = (showStateNamesCheckBox.isSelected()) ? true : false;
				repaint();
			}
		});
		inputPanel.add(showStateNamesCheckBox);

		JCheckBox showWaterValuesCheckBox = new JCheckBox("Show Water Values");
		showWaterValuesCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		showWaterValuesCheckBox.setSelected(true);
		showWaterValuesCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AquaNation.showWaterValues = (showWaterValuesCheckBox.isSelected()) ? true : false;
				repaint();
			}
		});
		inputPanel.add(showWaterValuesCheckBox);

		Component verticalStrut10 = Box.createVerticalStrut(20);
		inputPanel.add(verticalStrut10);

		JLabel valuesToBeDisplayedLabel = new JLabel("Select values to be displayed:");
		valuesToBeDisplayedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.add(valuesToBeDisplayedLabel);

		Component verticalStrut11 = Box.createVerticalStrut(20);
		verticalStrut11.setMaximumSize(new Dimension(32767, 8));
		inputPanel.add(verticalStrut11);

		waterValuesComboBox = new JComboBox<String>();
		waterValuesComboBoxModel = new DefaultComboBoxModel<String>(new String[] { "Arsenic", "Radon", "Uranium" });
		waterValuesComboBox.setModel(waterValuesComboBoxModel);
		waterValuesComboBox.setMaximumSize(new Dimension(133, 20));
		waterValuesComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Reset all the states to the default no color
				repaint();

				colorer.fillImage(getDefaultFullMapImage());
			}
		});
		inputPanel.add(waterValuesComboBox);

		Component verticalStrut12 = Box.createVerticalStrut(20);
		inputPanel.add(verticalStrut12);

		JLabel addAnotherValueLabel = new JLabel("Add another value set:");
		addAnotherValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		inputPanel.add(addAnotherValueLabel);

		Component verticalStrut13 = Box.createVerticalStrut(20);
		verticalStrut13.setMaximumSize(new Dimension(32767, 5));
		inputPanel.add(verticalStrut13);

		valueSetNameTextField = new JTextField();
		valueSetNameTextField.setMaximumSize(new Dimension(233, 30));
		inputPanel.add(valueSetNameTextField);
		valueSetNameTextField.setColumns(10);

		Component verticalStrut14 = Box.createVerticalStrut(20);
		verticalStrut14.setMaximumSize(new Dimension(32767, 5));
		inputPanel.add(verticalStrut14);

		JButton addButton = new JButton("Add");
		addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (valueSetNameTextField.getText().trim().length() <= 0) return;
				String valueSetName = valueSetNameTextField.getText().trim();
				waterValuesComboBoxModel.addElement(valueSetName);
				for (State states : AquaNation.getStates()) {
					states.addDataSet(valueSetName);
				}
				valueSetNameTextField.setText("");
			}
		});
		inputPanel.add(addButton);
		refreshRatios();
		addMapImage();
		setVisible(true);
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (fullMapImage != null) {
			refreshRatios();
			refreshFullMapImage();
		}
	}

	private void addMapImage() {
		try {
			defaultFullMapImage = ImageIO.read(getClass().getResourceAsStream("/usaMapWithOutlines.gif"));
			fullMapImage = defaultFullMapImage.getScaledInstance((int) Math.round(newWidth), (int) Math.round(newHeight), Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(fullMapImage);
			imageLabel = new JLabel(imageIcon);
			imagePanel.add(imageLabel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void refreshRatios() {
		Dimension actual = getContentPane().getSize();
		newWidth = actual.getWidth() - 310 - BUFFER;
		newHeight = (newWidth * ADJUSTING_RATIO);
		if (newHeight > (actual.getHeight() - 10 - BUFFER)) {
			newHeight = actual.getHeight() - 10 - BUFFER;
			newWidth = (newHeight * ADJUSTING_RATIO);
		}

		// Avoid IllegalArgumentException
		if (newHeight < 1) newHeight = 1;
		if (newWidth < 1) newWidth = 1;
	}

	public static double getRatioX() {
		return newWidth / DEFAULT_WIDTH;
	}

	public static double getRatioY() {
		return newHeight / DEFAULT_HEIGHT;
	}

	public static double getNewWidth() {
		return newWidth;
	}

	public static double getNewHeight() {
		return newHeight;
	}

	public static Image scaleImage(Image image) {
		return image.getScaledInstance((int) newWidth, (int) newHeight, Image.SCALE_SMOOTH);
	}

	public static void setStateWaterValues(State state, String waterSampleString) throws NumberFormatException {
		if (waterSampleString.contains(",")) {
			waterSampleString = waterSampleString.replace(" ", "");
			String[] strings = waterSampleString.split(",");
			for (String string : strings) {
				state.addWaterValue(Double.valueOf(string), MainFrame.getSelectedDataType());
				if (!history.contains("ADD " + state.getPostalCode() + " " + getSelectedDataType() + " " + string)) history.push("ADD " + state.getPostalCode() + " " + getSelectedDataType() + " " + string);
			}
		} else {
			state.addWaterValue(Double.valueOf(waterSampleString), MainFrame.getSelectedDataType());
			if (!history.contains("ADD " + state.getPostalCode() + " " + getSelectedDataType() + " " + waterSampleString)) history.push("ADD " + state.getPostalCode() + " " + getSelectedDataType() + " " + waterSampleString);
		}
		colorer.fillImage(defaultFullMapImage);
	}

	public static void setFullMapImage(final Image image) {
		if (image == null) return;

		fullMapImage = image;
		imageIcon.setImage(scaleImage(image));
		imageLabel.setIcon(imageIcon);
		imagePanel.repaint();
	}

	public void refreshFullMapImage() {
		colorer.fillImage(defaultFullMapImage);
	}

	public static Image getDefaultFullMapImage() {
		return defaultFullMapImage;
	}

	public static Image getFullMapImage() {
		return fullMapImage;
	}

	public static String getSelectedDataType() {
		return (String) waterValuesComboBox.getSelectedItem();
	}

	public static JPanel getImagePanel() {
		return imagePanel;
	}

	public static JPanel getInputPanel() {
		return inputPanel;
	}

	public static int getFrameWidth() {
		return width;
	}

	public static DefaultComboBoxModel<String> getWaterValuesComboBoxModel() {
		return waterValuesComboBoxModel;
	}

}
