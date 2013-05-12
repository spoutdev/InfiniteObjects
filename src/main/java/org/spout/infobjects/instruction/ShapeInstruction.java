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
package org.spout.infobjects.instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.spout.api.util.config.ConfigurationNode;

import org.spout.infobjects.IWGO;
import org.spout.infobjects.exception.InstructionLoadingException;
import org.spout.infobjects.exception.ShapeLoadingException;
import org.spout.infobjects.shape.Shape;
import org.spout.infobjects.util.RandomOwner;

/**
 * A shape placing instruction.
 */
public class ShapeInstruction extends Instruction {
	private final List<Shape> shapes = new ArrayList<Shape>();

	static {
		Instruction.register("shape", ShapeInstruction.class);
		// Load all included resources so they can register themselves
		try {
			Class.forName("org.spout.infobjects.shape.Cuboid");
			Class.forName("org.spout.infobjects.shape.Sphere");
			Class.forName("org.spout.infobjects.shape.Line");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ShapeInstruction.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * Constructs a new shape instruction from the parent iWGO and its name.
	 *
	 * @param iwgo The parent iWGO
	 * @param name The name
	 */
	public ShapeInstruction(IWGO iwgo, String name) {
		super(iwgo, name);
	}

	/**
	 * Adds a shape to be placed during execution of this instruction.
	 *
	 * @param shape The shape to add
	 */
	public void addShape(Shape shape) {
		shapes.add(shape);
	}

	/**
	 * Get the list of shape for this instruction. Changes to this collection are reflected in the
	 * instruction.
	 *
	 * @return The list of shapes
	 */
	public List<Shape> getShapes() {
		return shapes;
	}

	/**
	 * Loads the shape instruction from the properties node. The expected values are the shapes and
	 * their types, sizes, positions and material setter.
	 *
	 * @param properties The properties node to load from
	 * @throws InstructionLoadingException If the loading fails
	 */
	@Override
	public void load(ConfigurationNode properties) throws InstructionLoadingException {
		final IWGO iwgo = getIWGO();
		final ConfigurationNode shapesNode = properties.getNode("shapes");
		for (String key : shapesNode.getKeys(false)) {
			try {
				final ConfigurationNode shapeNode = shapesNode.getNode(key);
				final Shape shape = Shape.newShape(shapeNode.getNode("type").getString(), iwgo);
				shape.load(shapeNode.getNode("properties"));
				addShape(shape);
			} catch (Exception ex) {
				throw new ShapeLoadingException(key, ex);
			}
		}
	}

	/**
	 * Randomizes all the shapes for this instruction and calls the super method.
	 */
	@Override
	public void randomize() {
		super.randomize();
		for (Shape shape : shapes) {
			shape.randomize();
		}
	}

	/**
	 * Sets the randoms for all the shapes for this instruction to the provided one and calls the
	 * super method.
	 *
	 * @param random The random to use
	 */
	@Override
	public void setRandom(Random random) {
		super.setRandom(random);
		for (Shape shape : shapes) {
			if (shape instanceof RandomOwner) {
				((RandomOwner) shape).setRandom(random);
			}
		}
	}

	/**
	 * Executes this instruction by drawing all the shapes.
	 */
	@Override
	public void execute() {
		for (Shape shape : shapes) {
			shape.draw();
		}
	}

	/**
	 * Returns the string representation of this shape instruction.
	 *
	 * @return The string form of this instruction
	 */
	@Override
	public String toString() {
		return "ShapeInstruction{shapes=" + shapes + '}';
	}
}
