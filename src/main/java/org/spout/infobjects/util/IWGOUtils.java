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
package org.spout.infobjects.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialRegistry;

import org.spout.api.util.config.ConfigurationNode;

public class IWGOUtils {
	public static int nextInt(Random random, int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}

	public static double nextDouble(Random random, double min, double max) {
		return random.nextDouble() * (max - min) + min;
	}

	public static Map<String, String> toStringMap(ConfigurationNode node) {
		final Map<String, String> propertiesMap = new HashMap<String, String>();
		for (String key : node.getKeys(true)) {
			final ConfigurationNode n = node.getNode(key);
			if (!n.hasChildren()) {
				propertiesMap.put(key, n.getString());
			}
		}
		return propertiesMap;
	}

	public static BlockMaterial tryGetBlockMaterial(String name) {
		if (name == null || name.trim().equals("")) {
			throw new IllegalArgumentException("Name can not be null or empty");
		}
		final Material material = MaterialRegistry.get(name);
		if (material == null || !(material instanceof BlockMaterial)) {
			throw new IllegalArgumentException("\"" + name + "\" is not a block material");
		}
		return (BlockMaterial) material;
	}
}
