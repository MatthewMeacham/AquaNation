package com.aquanation.water.helpers;

import java.io.Serializable;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Represents a range as on a number line
 *
 */
public class Range implements Serializable {
	private static final long serialVersionUID = 3700527221574677999L;
	
	private double from;
	private double to;

	public Range(double from, double to) {
		this.from = from;
		this.to = to;
	}

	public double getFrom() {
		return from;
	}

	public double getTo() {
		return to;
	}

	public void setFrom(final double from) {
		this.from = from;
	}

	public void setTo(final double to) {
		this.to = to;
	}

}
