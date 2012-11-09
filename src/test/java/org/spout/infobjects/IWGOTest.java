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

import org.junit.Before;
import org.junit.Test;

import org.spout.api.material.BlockMaterial;

import org.spout.infobjects.instruction.Instruction;
import org.spout.infobjects.instruction.PlaceInstruction;
import org.spout.infobjects.material.MaterialPicker;
import org.spout.infobjects.shape.Shape;
import org.spout.infobjects.variable.Variable;

public class IWGOTest {
	@Before
	public void before() throws Exception {
		EngineFaker.setupEngine();
		initTestMaterials();
		Class.forName("org.spout.infobjects.function.RandomFunction");
	}

	@Test
	public void test() {
		final IWGOManager manager = new IWGOManager(new File("src/test/resources"));
		manager.loadIWGOs();
		final IWGO iwgo = manager.getIWGO("test-tree");

		System.out.println("Variables:");
		for (Variable variable : iwgo.getVariables()) {
			System.out.println('\t' + variable.getName() + ": " + variable.getValue());
		}
		System.out.println();

		System.out.println("Material pickers:");
		for (MaterialPicker picker : iwgo.getMaterialPickers()) {
			System.out.println('\t' + picker.getName() + ": " + picker);
		}
		System.out.println();

		System.out.println("Instructions:");
		for (Instruction instruction : iwgo.getInstructions()) {
			System.out.println("\tName:" + instruction.getName());
			System.out.println("\tVariables:");
			for (Variable variable : instruction.getVariables()) {
				System.out.println("\t\t" + variable.getName() + ": " + variable.getValue());
			}
			if (instruction instanceof PlaceInstruction) {
				System.out.println("\tShapes:");
				for (Shape shape : ((PlaceInstruction) instruction).getShapes()) {
					System.out.println("\t\t" + shape.getClass().getSimpleName());
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private void initTestMaterials() throws Exception {
		final String[] testMaterials = new String[]{
			"log",
			"leaf"
		};
		final Constructor constructor = TestMaterial.class.getDeclaredConstructor(String.class);
		constructor.setAccessible(true);
		for (String testMaterial : testMaterials) {
			constructor.newInstance(testMaterial);
		}
	}

	private static class TestMaterial extends BlockMaterial {
		private TestMaterial(String name) {
			super(name);
		}
	}
}
