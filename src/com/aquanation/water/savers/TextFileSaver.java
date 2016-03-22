package com.aquanation.water.savers;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.aquanation.water.AquaNation;
import com.aquanation.water.State;
import com.aquanation.water.helpers.DataSet;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Represents a system that saves the data of the map as a text file
 *
 */
public class TextFileSaver {

	private final String EXTENSION = ".txt";

	public TextFileSaver() {

	}

	public void save(File file) {
		try {
			// Ensure the file has the .mapdata extensions
			if (!file.getName().contains(EXTENSION)) {
				file = (file.getName().contains(".")) ? new File(file.getName().substring(0, file.getName().indexOf(".")) + EXTENSION) : new File(file.getName() + EXTENSION);
			}
			PrintWriter writer = new PrintWriter(file);

			writer.print("state name,postal code,");
			List<DataSet> dataSets = AquaNation.getStates().get(0).getWaterValues();

			ArrayList<Integer> maxes = new ArrayList<Integer>();
			for (int i = 0; i < dataSets.size(); i++) {
				maxes.add(1);
			}

			for (State state : AquaNation.getStates()) {
				List<DataSet> stateDataSets = state.getWaterValues();
				for (int i = 0; i < stateDataSets.size(); i++) {
					if (stateDataSets.get(i).getValues().size() > maxes.get(i)) maxes.set(i, stateDataSets.get(i).getValues().size());
				}
			}

			for (int i = 0; i < dataSets.size(); i++) {
				for (int j = 0; j < maxes.get(i); j++) {
					writer.print(dataSets.get(i).getDataType() + " Value " + (j + 1));
					writer.print(",");
				}
			}
			writer.println();

			for (State state : AquaNation.getStates()) {
				writer.print(state.getFullName());
				writer.print(",");
				writer.print(state.getPostalCode());
				writer.print(",");

				List<DataSet> waterValues = state.getWaterValues();
				for (int i = 0; i < waterValues.size(); i++) {
					List<Double> doubles = waterValues.get(i).getValues();
					for (int j = doubles.size() - 1; j >= 0; j--) {
						writer.print(doubles.get(j));
						writer.print(",");
					}

					int commasToWrite = maxes.get(i) - doubles.size();
					// commasToWrite--; // Account for the comma we wrote above
					for (int j = 0; j < commasToWrite; j++) {
						writer.print(",");
					}
				}
				writer.println();
			}
			writer.close();
			AquaNation.drawString("Exported Successfully", 2000, Color.WHITE);
		} catch (FileNotFoundException e) {
			AquaNation.drawString("Export Failed", 2500, Color.WHITE);
		}
	}

}
