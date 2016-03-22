package com.aquanation.water.savers;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.aquanation.water.AquaNation;
import com.aquanation.water.State;
import com.aquanation.water.colorer.ColorRange;
import com.aquanation.water.helpers.DataSet;
import com.aquanation.water.windows.MainFrame;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Represents a system that saves the data of the map to a file
 *
 */
public class MapDataSaver {

	private final String EXTENSION = ".mapdata";

	public MapDataSaver() {

	}

	public void save(File file) {
		try {
			// Ensure the file has the .mapdata extensions
			if (!file.getName().contains(EXTENSION)) {
				file = (file.getName().contains(".")) ? new File(file.getName().substring(0, file.getName().indexOf(".")) + EXTENSION) : new File(file.getName() + EXTENSION);
			}
			if (!file.exists()) file.createNewFile();

			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			// Write state data
			oos.writeInt(AquaNation.getStates().size());
			for (State state : AquaNation.getStates()) {
				oos.writeObject(state);
			}

			// Write the color range data
			oos.writeInt(MainFrame.colorer.getColorRanges().size());
			for (ColorRange colorRange : MainFrame.colorer.getColorRanges()) {
				oos.writeObject(colorRange);
			}

			// Write the font
			oos.writeObject(AquaNation.getFont());

			oos.flush();
			fos.flush();

			oos.close();
			AquaNation.drawString("Save successful", 2000, Color.WHITE);
		} catch (IOException e) {
			e.printStackTrace();
			AquaNation.drawString("Save Failed", 2000, Color.RED);
		}
	}

	public void load(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);

			// Read states data
			int numberOfStates = ois.readInt();
			ArrayList<State> states = new ArrayList<State>(numberOfStates);
			for (int i = 0; i < numberOfStates; i++) {
				states.add((State) ois.readObject());
			}
			AquaNation.setStates(states);

			// Add the data types
			MainFrame.getWaterValuesComboBoxModel().removeAllElements();
			for (DataSet dataSet : AquaNation.getStates().get(0).getWaterValues()) {
				MainFrame.getWaterValuesComboBoxModel().addElement(dataSet.getDataType());
			}

			// Read color range data
			int numberOfColorRanges = ois.readInt();
			List<ColorRange> colorRanges = new ArrayList<ColorRange>(numberOfColorRanges);
			for (int i = 0; i < numberOfColorRanges; i++) {
				colorRanges.add((ColorRange) ois.readObject());
			}
			MainFrame.colorer.setColorRanges(colorRanges);

			// Read font
			AquaNation.setFont((Font) ois.readObject());

			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
