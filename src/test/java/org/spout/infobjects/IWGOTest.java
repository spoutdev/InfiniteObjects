/*
 * This file is part of InfiniteObjects.
 *
 * Copyright (c) 2012 Spout LLC <http://www.spout.org/>
 * InfiniteObjects is licensed under the Spout License Version 1.
 *
 * InfiniteObjects is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * InfiniteObjects is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license,
 * including the MIT license.
 */
package org.spout.infobjects;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import org.spout.api.material.BlockMaterial;

import org.spout.infobjects.condition.Condition;
import org.spout.infobjects.instruction.BlockInstruction;
import org.spout.infobjects.instruction.Instruction;
import org.spout.infobjects.instruction.RepeatInstruction;
import org.spout.infobjects.instruction.ShapeInstruction;
import org.spout.infobjects.material.MaterialSetter;
import org.spout.infobjects.shape.Shape;
import org.spout.infobjects.value.IncrementableValue;
import org.spout.infobjects.variable.Variable;

public class IWGOTest {
	@Before
	public void before() throws Exception {
		EngineFaker.setupEngine();
		initTestMaterials();
	}

	@Test
	public void test() {
		final IWGOManager manager = new IWGOManager(new File("src/test/resources"), false);
		manager.loadIWGOs();
		final IWGO iwgo = manager.getIWGO("huge_tree");

		iwgo.setRandom(new Random());

		long start = System.nanoTime();
		iwgo.randomize();
		System.out.println("Estimated randomization time: " + (System.nanoTime() - start) / 1000000d + "ms");

		System.out.println("Variables:");
		for (Variable variable : iwgo.getVariables()) {
			System.out.println('\t' + variable.toString());
		}
		System.out.println();

		System.out.println("Material setters:");
		for (MaterialSetter setter : iwgo.getMaterialSetters()) {
			System.out.println('\t' + setter.toString());
		}
		System.out.println();


		System.out.println("Conditions:");
		for (Condition condition : iwgo.getConditions()) {
			System.out.println('\t' + condition.toString());
		}
		System.out.println();

		System.out.println("Instructions:");
		for (Instruction instruction : iwgo.getInstructions()) {
			System.out.println("\tName: " + instruction.getName());
			System.out.println("\tVariables:");
			for (Variable variable : instruction.getVariables()) {
				System.out.println("\t\t" + variable);
			}
			if (instruction instanceof ShapeInstruction) {
				System.out.println("\tShapes:");
				for (Shape shape : ((ShapeInstruction) instruction).getShapes()) {
					System.out.println("\t\t" + shape);
				}
			} else if (instruction instanceof RepeatInstruction) {
				final RepeatInstruction repeat = (RepeatInstruction) instruction;
				System.out.println("\tRepeat: " + repeat.getRepeat());
				System.out.println("\tTimes: " + repeat.getTimes());
				System.out.println("\tIncrements:");
				for (IncrementableValue incrementable : repeat.getIncrementables()) {
					System.out.println("\t\t" + incrementable);
				}
			} else if (instruction instanceof BlockInstruction) {
				final BlockInstruction block = (BlockInstruction) instruction;
				System.out.println("\tX: " + block.getX());
				System.out.println("\tY: " + block.getY());
				System.out.println("\tZ: " + block.getZ());
				System.out.println("\tSetter: " + block.getMaterialSetter());
				System.out.println("\tOuter: " + block.isOuter());
			}
			System.out.println("\t--------------------");
		}
		System.out.println();
	}

	private void initTestMaterials() throws Exception {
		final String[] testMaterials = new String[]{
			"Jungle Leaves",
			"Jungle Wood",
			"Stone",
			"Dirt",
			"Grass",
			"Water"
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
