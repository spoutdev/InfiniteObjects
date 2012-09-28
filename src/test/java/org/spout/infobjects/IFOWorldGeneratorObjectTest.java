/*
 * This file is part of InfObjects.
 *
 * Copyright (c) 2012, SpoutDev <http://www.spout.org/>
 * InfObjects is licensed under the SpoutDev License Version 1.
 *
 * InfObjects is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * InfObjects is distributed in the hope that it will be useful,
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
package org.spout.infobjects;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

import org.spout.api.material.BlockMaterial;

import org.spout.infobjects.list.IFOList;
import org.spout.infobjects.list.StaticList;
import org.spout.infobjects.material.MaterialPicker;
import org.spout.infobjects.variable.StaticVariable;
import org.spout.infobjects.variable.Variable;

public class IFOWorldGeneratorObjectTest {
	@Before
	public void initResources() {
		initTestMaterials();
		try {
			Class.forName("org.spout.infobjects.function.RandomFunction");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(IFOWorldGeneratorObjectTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Test
	public void testIFOWGO() {
		final IFOManager manager = new IFOManager(new File("src/test/resources"));
		manager.loadIFOs();
		final IFOWorldGeneratorObject ifowgo = manager.getIFO("test");
		Assert.assertTrue(ifowgo != null);
		printInfo(ifowgo);
		testVariables(ifowgo);
		testLists(ifowgo);
		testPickers(ifowgo);
		long start = System.nanoTime();
		ifowgo.randomize();
		System.out.println("Estimated time for randomization: " + ((System.nanoTime() - start) / 1000000d) + "ms");
		System.out.println();
	}

	private void testVariables(IFOWorldGeneratorObject ifowgo) {
		Assert.assertTrue(ifowgo.getVariable("vtest1").getValue() == 6);
	}

	private void testLists(IFOWorldGeneratorObject ifowgo) {
		final double test1Value = ifowgo.getVariable("vtest1").getValue();
		final double t4estValue = ifowgo.getVariable("vtest4").getValue();
		final IFOList ltest3 = ifowgo.getList("ltest3");
		final IFOList ltest4 = ifowgo.getList("ltest4");
		Assert.assertTrue(ltest4.getSize() == test1Value);
		Assert.assertTrue(ltest4.getSize() == ltest3.getSize());
		for (int i = 0; i < ltest4.getSize(); i++) {
			Assert.assertTrue(ltest3.getValue(i) + t4estValue == ltest4.getValue(i));
		}
		final IFOList ltest2 = ifowgo.getList("ltest2");
		Assert.assertTrue(ltest2.getSize() == 12);
	}

	private void testPickers(IFOWorldGeneratorObject ifowgo) {
		final MaterialPicker picker = ifowgo.getPicker("ptest1");
		Assert.assertTrue(picker != null);
		final String materialName = picker.pickMaterial(false).getDisplayName();
		Assert.assertTrue(materialName.equals("Stone") || materialName.equals("Air"));
	}

	private void printInfo(IFOWorldGeneratorObject ifowgo) {
		System.out.println("Name:");
		System.out.println("\t" + ifowgo.getName());
		System.out.println("Variables:");
		for (Variable variable : ifowgo.getVariables()) {
			String type = variable instanceof StaticVariable ? "{STATIC}" : "{NORMAL}";
			System.out.println("\t" + type + " " + variable.getName() + ": " + variable.getValue());
		}
		System.out.println("Lists:");
		for (IFOList list : ifowgo.getLists()) {
			String type = list instanceof StaticList ? "{STATIC}" : "{NORMAL}";
			final List<Double> values = new ArrayList<Double>();
			for (int i = 0; i < list.getSize(); i++) {
				values.add(list.getValue(i));
			}
			System.out.println("\t" + type + " " + list.getName() + ": " + values);
		}
		System.out.println("Pickers:");
		for (MaterialPicker picker : ifowgo.getPickers()) {
			System.out.println("\t" + picker.getName() + ": " + picker);
		}
		System.out.println();
	}

	private void initTestMaterials() {
		final String[] testMaterials = new String[]{
			"Stone",
			"Dirt",
			"Log",
			"Wood",
			"Cobblestone",
			"Leaves"
		};
		Constructor constructor = null;
		try {
			constructor = TestMaterial.class.getDeclaredConstructor(String.class);
			constructor.setAccessible(true);
		} catch (Exception ex) {
			Logger.getLogger(IFOWorldGeneratorObjectTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		for (String testMaterial : testMaterials) {
			try {
				constructor.newInstance(testMaterial);
			} catch (Exception ex) {
				Logger.getLogger(IFOWorldGeneratorObjectTest.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private static class TestMaterial extends BlockMaterial {
		private TestMaterial(String name) {
			super(name);
		}
	}
}
