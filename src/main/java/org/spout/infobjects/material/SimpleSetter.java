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

import java.util.Map;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

import org.spout.infobjects.util.IWGOUtils;

public class SimpleSetter extends MaterialSetter {
	protected BlockMaterial material;
	protected short data;

	public SimpleSetter(String name) {
		super(name);
	}

	@Override
	public void configure(Map<String, String> properties) {
		material = IWGOUtils.tryGetBlockMaterial(properties.get("material"));
		if (properties.containsKey("data")) {
			data = Short.parseShort(properties.get("data"));
		} else {
			data = -1;
		}
	}

	@Override
	public void setMaterial(World world, int x, int y, int z, boolean outer) {
		world.setBlockMaterial(x, y, z, material, data == -1 ? material.getData() : data, null);
	}

	@Override
	public String toString() {
		return "SimpleSetter{name=" + getName() + ", material=" + material + ", data=" + data + '}';
	}
}
