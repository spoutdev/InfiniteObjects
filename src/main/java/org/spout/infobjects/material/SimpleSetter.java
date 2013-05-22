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
package org.spout.infobjects.material;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.util.config.ConfigurationNode;

import org.spout.infobjects.util.IWGOUtils;

/**
 * A setter for setting a material independently from the value of outer.
 */
public class SimpleSetter extends MaterialSetter {
	protected BlockMaterial material;
	protected short data;

	static {
		MaterialSetter.register("simple", SimpleSetter.class);
	}

	/**
	 * Constructs a new simple setter from its name.
	 *
	 * @param name The name of the setter
	 */
	public SimpleSetter(String name) {
		super(name);
	}

	/**
	 * Configures this material setter. Expected properties are "material" and "data". If data is
	 * -1, {@link org.spout.api.material.Material#getData()} is used.
	 *
	 * @param properties The properties as a string, string map
	 */
	@Override
	public void load(ConfigurationNode properties) {
		material = IWGOUtils.tryGetBlockMaterial(properties.getNode("material").getString());
		if (properties.hasNode("data")) {
			data = properties.getNode("data").getShort();
		} else {
			data = -1;
		}
	}

	/**
	 * Sets the material at the desired coordinates inside the world. This setter will always set
	 * the same material, whatever the value of outer may be.
	 *
	 * @param world The world to set the material in
	 * @param x The x coordinate of the world position
	 * @param y The y coordinate of the world position
	 * @param z The z coordinate of the world position
	 * @param outer Not used, will always set the same material
	 */
	@Override
	public void setMaterial(World world, int x, int y, int z, boolean outer) {
		world.setBlockMaterial(x, y, z, material, data == -1 ? material.getData() : data, null);
	}

	/**
	 * Returns the string representation of this material setter.
	 *
	 * @return The string form of this setter.
	 */
	@Override
	public String toString() {
		return "SimpleSetter{name=" + getName() + ", material=" + material + ", data=" + data + '}';
	}
}
