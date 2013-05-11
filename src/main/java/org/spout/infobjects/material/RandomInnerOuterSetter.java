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
package org.spout.infobjects.material;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.util.config.ConfigurationNode;

import org.spout.infobjects.util.RandomOwner;

/**
 * A material setter for setting a different inner and outer material or not, depending on an
 * individual.
 */
public class RandomInnerOuterSetter extends InnerOuterSetter implements RandomOwner {
	private Random random = new Random();
	private byte innerOdd;
	private byte outerOdd;

	static {
		MaterialSetter.register("random-inner-outer", RandomInnerOuterSetter.class);
	}

	/**
	 * Construct a new random inner outer-setter from its name.
	 *
	 * @param name The name of the setter
	 */
	public RandomInnerOuterSetter(String name) {
		super(name);
	}

	/**
	 * Configures the material setter. Expected properties are "inner.odd" and "outer.odd" plus
	 * those required by the super class.
	 *
	 * @param properties The property map as a string, string map.
	 */
	@Override
	public void load(ConfigurationNode properties) {
		super.load(properties);
		innerOdd = properties.getNode("inner", "odd").getByte();
		outerOdd = properties.getNode("outer", "odd").getByte();
	}

	/**
	 * Sets the material at the desired coordinates in the world. If outer is true and the next
	 * integer from the random in the range [0, 100[ is smaller than the odd for outer, the outer
	 * material is set. If outer is false, the same is done, but for the inner odd and material.
	 *
	 * @param world The world to set the material in
	 * @param x The x coordinate of the world position
	 * @param y The y coordinate of the world position
	 * @param z The z coordinate of the world position
	 * @param outer Whether or not the material is outside the shape
	 */
	@Override
	public void setMaterial(World world, int x, int y, int z, boolean outer) {
		if (random.nextInt(100) < (outer ? outerOdd : innerOdd)) {
			super.setMaterial(world, x, y, z, outer);
		}
	}

	/**
	 * Sets the random for this material setter.
	 *
	 * @param random The random to use.
	 */
	@Override
	public void setRandom(Random random) {
		this.random = random;
	}

	/**
	 * Returns the string representation of this material setter.
	 *
	 * @return The string form of this material setter
	 */
	@Override
	public String toString() {
		return "RandomInnerOuterSetter{name=" + getName() + ", inner=" + inner + ", innerData="
				+ innerData + ", innerOdd=" + innerOdd + ", outer=" + outer + ", outerData="
				+ outerData + ", outerOdd=" + outerOdd + '}';
	}
}
