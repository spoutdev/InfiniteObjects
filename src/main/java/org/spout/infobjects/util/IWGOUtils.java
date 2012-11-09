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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.spout.api.util.config.ConfigurationNode;
import org.spout.infobjects.value.CalculableValue;
import org.spout.infobjects.value.Value;

import org.spout.infobjects.variable.Variable;

public class IWGOUtils {
	public static int nextInt(Random random, int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}

	public static double nextDouble(Random random, double min, double max) {
		return random.nextDouble() * (max - min) + min;
	}

	public static Map<String, String> toStringMap(ConfigurationNode propertiesNode) {
		final Map<String, String> propertiesMap = new HashMap<String, String>();
		for (String key : propertiesNode.getKeys(true)) {
			final ConfigurationNode node = propertiesNode.getNode(key);
			if (!node.hasChildren()) {
				propertiesMap.put(key, node.getString());
			}
		}
		return propertiesMap;
	}

	public static void calculateVariables(Collection<Variable> variables) {
		for (Variable variable : variables) {
			final Value value = variable.getRawValue();
			if (value instanceof CalculableValue) {
				((CalculableValue) value).calculate();
			}
		}
	}
}
