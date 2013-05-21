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
package org.spout.infobjects.shape;

import java.util.Map;
import java.util.Random;

import org.spout.api.geo.discrete.Point;
import org.spout.api.util.BlockIterator;

import org.spout.infobjects.exception.ShapeLoadingException;
import org.spout.infobjects.instruction.Instruction;
import org.spout.infobjects.material.MaterialSetter;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.value.Value;

/**
 * A shape to draw a line from one point to another.
 */
public class Line extends Shape {
	private Value lengthX;
	private Value lengthY;
	private Value lengthZ;

	static {
		register("line", Line.class);
	}

	/**
	 * Constructs a new line shape from the parent instruction.
	 *
	 * @param instruction The parent instruction
	 */
	public Line(Instruction instruction) {
		super(instruction);
	}

	/**
	 * Sets the size of the line from the values in the map. The size of the line is defined like a
	 * vector, with a component for each axis. The shortest distance from the origin to the point
	 * defined by the size coordinates is the line. The expected values for the map are "lengthX",
	 * "lengthY" and "lengthZ". If any of these are missing, an exception is thrown.
	 *
	 * @param sizes The size as a string, value map
	 * @throws ShapeLoadingException If any of the "lengthX", "lengthY" or "lengthZ" keys are
	 * missing
	 */
	@Override
	public void setSize(Map<String, Value> sizes) throws ShapeLoadingException {
		if (!sizes.containsKey("lengthX")) {
			throw new ShapeLoadingException("lengthX size is missing");
		}
		if (!sizes.containsKey("lengthY")) {
			throw new ShapeLoadingException("lengthY size is missing");
		}
		if (!sizes.containsKey("lengthZ")) {
			throw new ShapeLoadingException("lengthZ size is missing");
		}
		lengthX = sizes.get("lengthX");
		lengthY = sizes.get("lengthY");
		lengthZ = sizes.get("lengthZ");
	}

	/**
	 * Draws the shape. This uses the {@link org.spout.api.util.BlockIterator} to iterate from the
	 * position to the position plus the size. Each block is set with the material setter as being
	 * outer.
	 */
	@Override
	public void draw() {
		final Point start = getInstruction().getIWGO().transform(getX().getValue(), getY().getValue(), getZ().getValue());
		final BlockIterator line = new BlockIterator(start, start.add(lengthX.getValue(),
				lengthY.getValue(), lengthZ.getValue()));
		final MaterialSetter materialSetter = getMaterialSetter();
		materialSetter.setMaterial(start, true);
		while (line.hasNext()) {
			materialSetter.setMaterial(line.next().getPosition(), true);
		}
	}

	/**
	 * Randomizes the size values of the line by recalculating them. Then calls the super method.
	 */
	@Override
	public void randomize() {
		super.randomize();
		lengthX.calculate();
		lengthY.calculate();
		lengthZ.calculate();
	}

	/**
	 * Sets the random for each size value if they implement
	 * {@link org.spout.infobjects.util.RandomOwner}. Calls the super method.
	 *
	 * @param random The random to use
	 */
	@Override
	public void setRandom(Random random) {
		super.setRandom(random);
		if (lengthX instanceof RandomOwner) {
			((RandomOwner) lengthX).setRandom(random);
		}
		if (lengthY instanceof RandomOwner) {
			((RandomOwner) lengthY).setRandom(random);
		}
		if (lengthZ instanceof RandomOwner) {
			((RandomOwner) lengthZ).setRandom(random);
		}
	}

	/**
	 * Returns the string representation of the shape.
	 *
	 * @return The string form of the shape
	 */
	@Override
	public String toString() {
		return "Line{x=" + getX() + ", y=" + getY() + ", z=" + getZ() + ", setter=" + getMaterialSetter()
				+ ", lengthX=" + lengthX + ", lengthY=" + lengthY + ", lengthZ=" + lengthZ + '}';
	}
}
