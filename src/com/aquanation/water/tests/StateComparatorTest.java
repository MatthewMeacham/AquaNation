package com.aquanation.water.tests;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import com.aquanation.water.State;
import com.aquanation.water.comparators.StateComparator;

/**
 * 
 * @author Matthew Meacham
 * 
 *         Contains the tests for {@link StateComprator}
 *
 */
public class StateComparatorTest extends ATest {

	public StateComparatorTest() {
		System.out.println("Doing State Comparator Tests");

		ArrayList<State> states = new ArrayList<State>();
		states.add(new State("ZZ", "Zzland", new Point(1, 1), new Point(1, 1)));
		states.add(new State("AA", "Aaland", new Point(2, 2), new Point(2, 2)));
		states.add(new State("CC", "Ccland", new Point(3, 3), new Point(3, 3)));
		states.add(new State("BB", "Bbland", new Point(4, 4), new Point(4, 4)));

		Collections.sort(states, new StateComparator());

		if (!states.get(0).getPostalCode().equals("AA")) failed("first element is not correct");
		if (!states.get(1).getPostalCode().equals("BB")) failed("second element is not correct");
		if (!states.get(2).getPostalCode().equals("CC")) failed("third element is not correct");
		if (!states.get(3).getPostalCode().equals("ZZ")) failed("fourth element is not correct");

		System.out.println("Passed State Comparator Tests\n");
	}

	@Override
	void failed(String message) {
		System.out.println("State Comparator: " + message);
	}

}
