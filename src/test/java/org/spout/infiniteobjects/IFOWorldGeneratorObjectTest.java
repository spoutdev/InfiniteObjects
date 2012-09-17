/*
 * This file is part of SpoutAPI.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * SpoutAPI is licensed under the SpoutDev License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.infiniteobjects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

import org.spout.infiniteobjects.variable.Variable;
import org.spout.infiniteobjects.variable.VariableList;

public class IFOWorldGeneratorObjectTest {
	@Test
	public void testIFOWGO() {
		final IFOManager manager = new IFOManager(new File("src/test/resources"));
		manager.loadIFOs();
		final IFOWorldGeneratorObject ifowgo = manager.getIFO("test");
		Assert.assertTrue(ifowgo != null);
		printInfo(ifowgo);
		testVariables(ifowgo);
		testLists(ifowgo);
		long start = System.nanoTime();
		ifowgo.randomize();
		System.out.println("Estimated time for randomization: " + ((System.nanoTime() - start) / 1000000d) + "ms");
		System.out.println();
	}

	private void testVariables(IFOWorldGeneratorObject ifowgo) {
		Assert.assertTrue(ifowgo.getVariable("test1").getValue() == 6);
	}

	private void testLists(IFOWorldGeneratorObject ifowgo) {
		final double test1Value = ifowgo.getVariable("test1").getValue();
		final double t4estValue = ifowgo.getVariable("t4est").getValue();
		final VariableList ltest3 = ifowgo.getList("ltest3");
		final VariableList ltest4 = ifowgo.getList("ltest4");
		Assert.assertTrue(ltest4.getSize() == test1Value);
		Assert.assertTrue(ltest4.getSize() == ltest3.getSize());
		for (int i = 0; i < ltest4.getSize(); i++) {
			Assert.assertTrue(ltest3.getValue(i) + t4estValue == ltest4.getValue(i));
		}
		final VariableList ltest2 = ifowgo.getList("ltest2");
		Assert.assertTrue(ltest2.getSize() == 12);
	}
	
	private void printInfo(IFOWorldGeneratorObject ifowgo) {
		System.out.println("Name:");
		System.out.println("\t" + ifowgo.getName());
		System.out.println("Variables:");
		for (Variable variable : ifowgo.getVariables()) {
			System.out.println("\t" + variable.getName() + ": " + variable.getValue());
		}
		System.out.println("Lists:");
		for (VariableList list : ifowgo.getLists()) {
			final List<Double> values = new ArrayList<Double>();
			for (int i = 0; i < list.getSize(); i++) {
				values.add(list.getValue(i));
			}
			System.out.println("\t" + list.getName() + ": " + values);
		}
		System.out.println();
	}
}
