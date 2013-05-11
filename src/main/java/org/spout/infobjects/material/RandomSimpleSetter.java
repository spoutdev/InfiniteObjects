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
 * A material setter for randomly setting a material, depending on the specified odd. This setter
 * sets the same material whatever the value for outer may be.
 */
public class RandomSimpleSetter extends SimpleSetter implements RandomOwner {
	private Random random = new Random();
	private byte odd;

	static {
		MaterialSetter.register("random-simple", RandomSimpleSetter.class);
	}

	/**
	 * Constructs a new random setter from its name.
	 *
	 * @param name The name of the material setter
	 */
	public RandomSimpleSetter(String name) {
		super(name);
	}

	/**
	 * Configures this material setter. Expected properties are "odd" and the ones required by the
	 * super class.
	 *
	 * @param properties The properties as a string, string map
	 */
	@Override
	public void load(ConfigurationNode properties) {
		super.load(properties);
		odd = properties.getNode("odd").getByte();
	}

	/**
	 * Sets the material at the desired coordinates in the world. Only sets the material if the next
	 * integer from the random in the [0, 100[ range is smaller than the odd.
	 *
	 * @param world The world to set the material in
	 * @param x The x coordinate of the world position
	 * @param y The y coordinate of the world position
	 * @param z The z coordinate of the world position
	 * @param outer Not used, will always set the same material
	 */
	@Override
	public void setMaterial(World world, int x, int y, int z, boolean outer) {
		if (random.nextInt(100) < odd) {
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
	 * @return The string form of this setter.
	 */
	@Override
	public String toString() {
		return "RandomSimpleSetter{name=" + getName() + ", material=" + material + ", data=" + data
				+ ", odd=" + odd + '}';
	}
}
