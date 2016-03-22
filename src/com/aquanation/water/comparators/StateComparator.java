package com.aquanation.water.comparators;

import java.util.Comparator;

import com.aquanation.water.State;

/**
 * 
 * @author Matthew Meacham
 *
 *         Performs the comparison between two states based on postal code in order to sort them
 */
public class StateComparator implements Comparator<State> {
	@Override
	public int compare(State state1, State state2) {
		return state1.getPostalCode().compareTo(state2.getPostalCode());
	}

}
