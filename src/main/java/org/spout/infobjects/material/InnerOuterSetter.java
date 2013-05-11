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

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.util.config.ConfigurationNode;

import org.spout.infobjects.util.IWGOUtils;

/**
 * A material setter for setting a different inner and outer material.
 */
public class InnerOuterSetter extends MaterialSetter {
	protected BlockMaterial inner;
	protected short innerData;
	protected BlockMaterial outer;
	protected short outerData;

	static {
		MaterialSetter.register("inner-outer", InnerOuterSetter.class);
	}

	/**
	 * Construct a new inner-outer material setter from its name
	 *
	 * @param name The name of the material setter
	 */
	public InnerOuterSetter(String name) {
		super(name);
	}

	/**
	 * Configures the material setter. Expected properties are "inner.material" for the inner
	 * material, "inner.data" for the inner material's data, "outer.material" for the outer material
	 * and "outer.data" for the outer material's data. Material values are the simple names of the
	 * materials. If the material's data (as obtained by
	 * {@link org.spout.api.material.Material#getData()}) is what should be used, the data value can
	 * be set to -1.
	 *
	 * @param properties The property map as a string, string map
	 */
	@Override
	public void load(ConfigurationNode properties) {
		final ConfigurationNode innerMaterial = properties.getNode("inner");
		inner = IWGOUtils.tryGetBlockMaterial(innerMaterial.getNode("material").getString());
		if (innerMaterial.hasNode("data")) {
			innerData = innerMaterial.getNode("data").getShort();
		} else {
			innerData = -1;
		}
		final ConfigurationNode outerMaterial = properties.getNode("outer");
		outer = IWGOUtils.tryGetBlockMaterial(outerMaterial.getNode("material").getString());
		if (outerMaterial.hasNode("data")) {
			outerData = outerMaterial.getNode("data").getShort();
		} else {
			outerData = -1;
		}
	}

	/**
	 * Sets the material at the desired coordinated inside the world. If outer is true, the outer
	 * material and data is used, if false, the inner one is. If data is -1,
	 * {@link org.spout.api.material.Material#getData()} is used.
	 *
	 * @param world The world to set the material in
	 * @param x The x coordinate of the world position
	 * @param y The y coordinate of the world position
	 * @param z The z coordinate of the world position
	 * @param outer Whether or not the material is outside or inside the shape
	 */
	@Override
	public void setMaterial(World world, int x, int y, int z, boolean outer) {
		if (outer) {
			world.setBlockMaterial(x, y, z, this.outer, outerData == -1 ? this.outer.getData() : outerData, null);
		} else {
			world.setBlockMaterial(x, y, z, inner, innerData == -1 ? inner.getData() : innerData, null);
		}
	}

	/**
	 * Returns the string representation of this material setter.
	 *
	 * @return The string form of this material setter
	 */
	@Override
	public String toString() {
		return "InnerOuterSetter{name=" + getName() + ", inner=" + inner + ", innerData="
				+ innerData + ", outer=" + outer + ", outerData=" + outerData + '}';
	}
}
