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

public class Sphere extends Shape {
	private NormalVariable halfWidth;
	private NormalVariable halfHeight;
	private NormalVariable halfDepth;
	private double radiusX;
	private double radiusY;
	private double radiusZ;

	public Sphere(IFOWorldGeneratorObject owner, String name) {
		super(owner, name);
	}

	@Override
	public void load(NormalVariable... variables) {
		if (variables.length < 3) {
			throw new IllegalArgumentException("Expected at least 3 variables.");
		}
		halfWidth = variables[0];
		halfHeight = variables[1];
		halfDepth = variables[2];
	}

	@Override
	public void draw(int x, int y, int z) {
		final double rx = radiusX + 0.5;
		final double ry = radiusY + 0.5;
		final double rz = radiusZ + 0.5;
		final double invRadiusX = 1 / rx;
		final double invRadiusY = 1 / ry;
		final double invRadiusZ = 1 / rz;
		final int ceilRadiusX = (int) Math.ceil(rx);
		final int ceilRadiusY = (int) Math.ceil(ry);
		final int ceilRadiusZ = (int) Math.ceil(rz);

		double nextXn = 0;
		forX:
		for (int xx = 0; xx <= ceilRadiusX; xx++) {
			final double xn = nextXn;
			nextXn = (xx + 1) * invRadiusX;
			double nextYn = 0;
			forY:
			for (int yy = 0; yy <= ceilRadiusY; yy++) {
				final double yn = nextYn;
				nextYn = (yy + 1) * invRadiusY;
				double nextZn = 0;
				forZ:
				for (int zz = 0; zz <= ceilRadiusZ; zz++) {
					final double zn = nextZn;
					nextZn = (zz + 1) * invRadiusZ;

					if (lengthSquare(xn, yn, zn) > 1) {
						if (zz == 0) {
							if (yy == 0) {
								break forX;
							}
							break forY;
						}
						break forZ;
					}

					boolean outer = lengthSquare(nextXn, yn, zn) > 1
							|| lengthSquare(xn, nextYn, zn) > 1
							|| lengthSquare(xn, yn, nextZn) > 1;

					owner.setMaterial(picker.pickMaterial(outer), x + xx, y + yy, z + zz);
					owner.setMaterial(picker.pickMaterial(outer), x - xx, y + yy, z + zz);
					owner.setMaterial(picker.pickMaterial(outer), x + xx, y - yy, z + zz);
					owner.setMaterial(picker.pickMaterial(outer), x + xx, y + yy, z - zz);
					owner.setMaterial(picker.pickMaterial(outer), x - xx, y - yy, z + zz);
					owner.setMaterial(picker.pickMaterial(outer), x + xx, y - yy, z - zz);
					owner.setMaterial(picker.pickMaterial(outer), x - xx, y + yy, z - zz);
					owner.setMaterial(picker.pickMaterial(outer), x - xx, y - yy, z - zz);
				}
			}
		}
	}

	private double lengthSquare(double x, double y, double z) {
		return (x * x) + (y * y) + (z * z);
	}

	@Override
	public void calculate() {
		halfWidth.calculate();
		radiusX = halfWidth.getValue();
		halfHeight.calculate();
		radiusY = halfHeight.getValue();
		halfDepth.calculate();
		radiusZ = halfDepth.getValue();
	}
}
