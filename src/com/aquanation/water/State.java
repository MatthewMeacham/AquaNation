package com.aquanation.water;

import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.ArrayList;

import com.aquanation.water.helpers.DataSet;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Represents a State in the United States of America
 *
 */
public class State implements Serializable {
	private static final long serialVersionUID = -4709618659713293539L;

	private ArrayList<DataSet> waterValues = new ArrayList<DataSet>();
	// The two character short name for a state i.e NE, WY, TX
	private final String postalCode;
	private final String fullName;
	private Polygon outline;
	private Point waterValuePoint;
	private Point postalCodePoint;
	private ArrayList<Point> fillBucketPoints = new ArrayList<Point>();

	public State(String postalCode, String fullName, Point waterValuePoint, Point postalCodePoint, Polygon outline) {
		this.postalCode = postalCode;
		this.fullName = fullName;
		this.waterValuePoint = waterValuePoint;
		this.postalCodePoint = postalCodePoint;
		this.outline = outline;
		waterValues.add(new DataSet("Arsenic"));
		waterValues.add(new DataSet("Radon"));
		waterValues.add(new DataSet("Uranium"));
	}

	public State(String postalCode, String fullName, Point waterValuePoint, Point postalCodePoint) {
		this.postalCode = postalCode;
		this.fullName = fullName;
		this.waterValuePoint = waterValuePoint;
		this.postalCodePoint = postalCodePoint;
		waterValues.add(new DataSet("Arsenic"));
		waterValues.add(new DataSet("Radon"));
		waterValues.add(new DataSet("Uranium"));
	}

	public Point getWaterValuePoint() {
		return waterValuePoint;
	}

	public void setWaterValuePoint(final Point waterValuePoint) {
		this.waterValuePoint = waterValuePoint;
	}

	public ArrayList<Point> getFillBucketPoints() {
		return fillBucketPoints;
	}

	public Point getFillBucketPoint(int index) {
		return fillBucketPoints.get(index);
	}

	public void addFillBucketPoint(Point fillBucketPoint) {
		fillBucketPoints.add(fillBucketPoint);
	}

	public Point getPostalCodePoint() {
		return postalCodePoint;
	}

	public void setPostalCodePoint(final Point postalCodePoint) {
		this.postalCodePoint = postalCodePoint;
	}

	public ArrayList<DataSet> getWaterValues() {
		return waterValues;
	}

	public Double getWaterValue(final String dataType) {
		for (DataSet dataSet : waterValues) {
			if (dataSet.getDataType().toLowerCase().equals(dataType.toLowerCase())) {
				if (dataSet.getValues().size() <= 0) return null;
				return dataSet.getValues().get(dataSet.getValues().size() - 1);
			}
		}
		return null;
	}

	public ArrayList<Double> getWaterValues(final String dataType) {
		for (DataSet dataSet : waterValues) {
			if (dataSet.getDataType().toLowerCase().equals(dataType.toLowerCase())) {
				return dataSet.getValues();
			}
		}
		return null;
	}

	public void addDataSet(String dataType) {
		waterValues.add(new DataSet(dataType));
	}

	public void setWaterValue(final String dataType, final Double waterValue, final int index) {
		for (DataSet dataSet : waterValues) {
			if (dataType.toLowerCase().equals(dataSet.getDataType().toLowerCase())) {
				if (index >= dataSet.getValues().size() || index < 0) {
					dataSet.addValue(waterValue);
				} else {
					dataSet.getValues().set(index, waterValue);
				}
				break;
			}
		}
	}

	public void addWaterValue(final Double waterValue, final String dataType) {
		for (DataSet dataSet : waterValues) {
			if (dataType.toLowerCase().equals(dataSet.getDataType().toLowerCase())) {
				dataSet.addValue(waterValue);
				break;
			}
		}
	}

	public void setWaterValues(final ArrayList<DataSet> waterValues) {
		this.waterValues = waterValues;
	}

	public void setWaterValues(final String dataType, final ArrayList<Double> waterValues) {
		for (DataSet dataSet : this.waterValues) {
			if (dataType.toLowerCase().equals(dataSet.getDataType().toLowerCase())) {
				dataSet.setValues(waterValues);
				break;
			}
		}
	}

	public Polygon getOutline() {
		return outline;
	}

	public void setOutline(final Polygon outline) {
		this.outline = outline;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getFullName() {
		return fullName;
	}

}
