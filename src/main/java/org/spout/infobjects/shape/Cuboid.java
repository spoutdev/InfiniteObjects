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

import org.spout.infobjects.IWGO;
import org.spout.infobjects.exception.ShapeLoadingException;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.value.Value;

/**
 * A shape to draw a cuboid.
 */
public class Cuboid extends Shape {
	private Value length;
	private Value height;
	private Value depth;

	/**
	 * Constructs a new cuboid from the parent iWGO.
	 *
	 * @param iwgo The parent iWGO
	 */
	public Cuboid(IWGO iwgo) {
		super(iwgo);
	}

	/**
	 * Sets the size of the cuboid from the values in the map. The expected values are "x", "y" and
	 * "z". The x size is the length, the y size, the height, and the z size, the depth. If any of
	 * these values are missing, and exception is thrown.
	 *
	 * @param sizes The size map as a string, value map
	 * @throws ShapeLoadingException If any of the "x", "y" or "z" keys are missing
	 */
	@Override
	public void setSize(Map<String, Value> sizes) throws ShapeLoadingException {
		super.setSize(sizes);
		length = sizes.get("x");
		height = sizes.get("y");
		depth = sizes.get("z");
	}

	/**
	 * Draws the cuboid. The position values define the first lower corner. The opposite corner is
	 * the position plus the size.
	 */
	@Override
	public void draw() {
		final int px = (int) x.getValue();
		final int py = (int) y.getValue();
		final int pz = (int) z.getValue();
		final int sizeX = (int) length.getValue();
		final int sizeY = (int) height.getValue();
		final int sizeZ = (int) depth.getValue();
		for (int xx = 0; xx < sizeX; xx++) {
			for (int yy = 0; yy < sizeY; yy++) {
				for (int zz = 0; zz < sizeZ; zz++) {
					setter.setMaterial(iwgo.transform(px + xx, py + yy, pz + zz),
							xx == 0 || yy == 0 || zz == 0 | xx == sizeX || yy == sizeY || zz == sizeZ);
				}
			}
		}
	}

	/**
	 * Randomizes the cuboid by recalculating the size values. Then calls the super method.
	 */
	@Override
	public void randomize() {
		super.randomize();
		length.calculate();
		height.calculate();
		depth.calculate();
	}

	/**
	 * Sets the random for each size value if they implement {@link org.spout.infobjects.util.RandomOwner}.
	 * Calls the super method.
	 *
	 * @param random The random to use
	 */
	@Override
	public void setRandom(Random random) {
		super.setRandom(random);
		if (length instanceof RandomOwner) {
			((RandomOwner) length).setRandom(random);
		}
		if (height instanceof RandomOwner) {
			((RandomOwner) height).setRandom(random);
		}
		if (depth instanceof RandomOwner) {
			((RandomOwner) depth).setRandom(random);
		}
	}

	/**
	 * Returns the string representation of the shape.
	 *
	 * @return The string form of the shape
	 */
	@Override
	public String toString() {
		return "Cuboid{x=" + x + ", y=" + y + ", z=" + z + ", setter=" + setter + ", length="
				+ length + ", height=" + height + ", depth=" + depth + '}';
	}
}
