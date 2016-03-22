package com.aquanation.water.helpers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Matthew Meacham
 * 
 * Represents some data for a specific data type
 *
 */
public class DataSet implements Serializable {
	private static final long serialVersionUID = 2228346683130402158L;

	private final String dataType;
	private ArrayList<Double> values = new ArrayList<Double>();

	public DataSet(String dataType) {
		this.dataType = dataType;
	}

	public String getDataType() {
		return dataType;
	}

	public ArrayList<Double> getValues() {
		return values;
	}

	public void addValue(final Double value) {
		values.add(value);
	}

	public void removeValue(final int index) {
		values.remove(index);
	}

	public void setValues(final ArrayList<Double> values) {
		this.values = values;
	}

}
