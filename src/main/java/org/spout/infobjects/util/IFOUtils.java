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

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.MaterialRegistry;

public class IFOUtils {
	public static int nextInt(Random random, int min, int max) {
		return random.nextInt(max - min + 1) + min;
	}

	public static float nextFloat(Random random, float min, float max) {
		return random.nextFloat() * (min - max) + min;
	}

	public static boolean hasMatch(String match, String string) {
		final Matcher matcher = Pattern.compile(match).matcher(string);
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	public static BlockMaterial getBlockMaterial(String expression) {
		final String[] split = expression.split("\\.");
		final Material material = MaterialRegistry.get(split[0]);
		if (!(material instanceof BlockMaterial)) {
			throw new IllegalArgumentException("Not a block material: " + split[0]);
		}
		if (split.length > 1) {
			try {
				final short data = Short.parseShort(split[1]);
				return ((BlockMaterial) material).getSubMaterial(data);
			} catch (NumberFormatException nfe) {
				throw new IllegalArgumentException("Data is not a short");
			}
		}
		return ((BlockMaterial) material);
	}
}
