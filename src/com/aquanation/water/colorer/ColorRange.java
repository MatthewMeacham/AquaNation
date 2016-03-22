package com.aquanation.water.colorer;

import java.io.Serializable;

import com.aquanation.water.helpers.Range;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Represents a range of values from a value to another value that also will represent a certain color
 *
 */

public class ColorRange implements Serializable {
	private static final long serialVersionUID = 3087220213874480306L;

	// The range of values that will be associated with this color, inclusive lower bound to exclusive upper bound
	private Range range;
	private int color;

	public ColorRange(double from, double to, int color) {
		this.range = new Range(from, to);
		this.color = color;
	}

	public double getFrom() {
		return range.getFrom();
	}

	public double getTo() {
		return range.getTo();
	}

	public int getColor() {
		return color;
	}

	public void setFrom(final double from) {
		this.range.setFrom(from);
	}

	public void setTo(final double to) {
		this.range.setTo(to);
	}

	public void setColor(final int color) {
		this.color = color;
	}

}
