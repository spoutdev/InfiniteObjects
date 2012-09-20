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

import org.spout.infobjects.IFOWorldGeneratorObject;
import org.spout.infobjects.variable.NormalVariable;

public class Cuboid extends Shape {
	private NormalVariable width;
	private NormalVariable height;
	private NormalVariable depth;
	private int xSize;
	private int ySize;
	private int zSize;

	public Cuboid(IFOWorldGeneratorObject owner, String name) {
		super(owner, name);
	}

	@Override
	public void load(NormalVariable... variables) {
		if (variables.length < 3) {
			throw new IllegalArgumentException("Expected at least 3 variables.");
		}
		width = variables[0];
		height = variables[1];
		depth = variables[2];
	}

	@Override
	public void draw(int x, int y, int z) {
		for (int xx = 0; xx < xSize; xx++) {
			for (int yy = 0; yy < ySize; yy++) {
				for (int zz = 0; zz < zSize; zz++) {
					boolean outer = xx == 0 || yy == 0 || zz == 0
							|| xx + 1 == xSize || yy + 1 == ySize || zz + 1 == zSize;
					owner.setMaterial(picker.pickMaterial(outer), x + xx, y + yy, z + zz);
				}
			}
		}
	}

	@Override
	public void calculate() {
		width.calculate();
		xSize = (int) width.getValue();
		height.calculate();
		ySize = (int) height.getValue();
		depth.calculate();
		zSize = (int) depth.getValue();
	}
}
