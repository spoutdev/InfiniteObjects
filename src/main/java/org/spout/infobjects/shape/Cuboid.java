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

import org.spout.infobjects.IWGO;
import org.spout.infobjects.value.Value;

public class Cuboid extends Shape {
	private Value length;
	private Value height;
	private Value depth;

	public Cuboid(IWGO iwgo) {
		super(iwgo);
	}

	@Override
	public void configure(Map<String, Value> properties) {
		length = properties.get("width");
		height = properties.get("height");
		depth = properties.get("depth");
	}

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
					picker.setMaterial(iwgo.transform(px + xx, py + yy, pz + zz),
							xx == 0 || yy == 0 || zz == 0 | xx == sizeX || yy == sizeY || zz == sizeZ);
				}
			}
		}
	}

	@Override
	public void randomize() {
		super.randomize();
		length.calculate();
		height.calculate();
		depth.calculate();
	}

	@Override
	public String toString() {
		return "Cuboid{x=" + x + ", y=" + y + ", z=" + z + ", picker=" + picker + ", length="
				+ length + ", height=" + height + ", depth=" + depth + '}';
	}
}
