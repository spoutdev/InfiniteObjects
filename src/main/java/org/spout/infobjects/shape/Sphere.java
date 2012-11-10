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

public class Sphere extends Shape {
	private Value radiusX;
	private Value radiusY;
	private Value radiusZ;

	public Sphere(IWGO iwgo) {
		super(iwgo);
	}

	@Override
	public void configure(Map<String, Value> properties) {
		radiusX = properties.get("size.x");
		radiusY = properties.get("size.y");
		radiusZ = properties.get("size.z");
	}

	@Override
	public void draw() {
		final int px = (int) x.getValue();
		final int py = (int) y.getValue();
		final int pz = (int) z.getValue();
		final double rx = radiusX.getValue() + 0.5;
		final double ry = radiusY.getValue() + 0.5;
		final double rz = radiusZ.getValue() + 0.5;
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
					if (lengthSq(xn, yn, zn) > 1) {
						if (zz == 0) {
							if (yy == 0) {
								break forX;
							}
							break forY;
						}
						break forZ;
					}
					final boolean outer = lengthSq(nextXn, yn, zn) > 1
							|| lengthSq(xn, nextYn, zn) > 1
							|| lengthSq(xn, yn, nextZn) > 1;
					iwgo.setMaterial(px + xx, py + yy, pz + zz, picker.pickMaterial(outer));
					iwgo.setMaterial(px - xx, py + yy, pz + zz, picker.pickMaterial(outer));
					iwgo.setMaterial(px + xx, py - yy, pz + zz, picker.pickMaterial(outer));
					iwgo.setMaterial(px + xx, py + yy, pz - zz, picker.pickMaterial(outer));
					iwgo.setMaterial(px - xx, py - yy, pz + zz, picker.pickMaterial(outer));
					iwgo.setMaterial(px + xx, py - yy, pz - zz, picker.pickMaterial(outer));
					iwgo.setMaterial(px - xx, py + yy, pz - zz, picker.pickMaterial(outer));
					iwgo.setMaterial(px - xx, py - yy, pz - zz, picker.pickMaterial(outer));
				}
			}
		}
	}

	@Override
	public void randomize() {
		super.randomize();
		radiusX.calculate();
		radiusY.calculate();
		radiusZ.calculate();
	}

	@Override
	public String toString() {
		return "Sphere{x=" + x + ", y=" + y + ", z=" + z + ", picker=" + picker + ", radiusX="
				+ radiusX + ", radiusY=" + radiusY + ", radiusZ=" + radiusZ + '}';
	}

	private static double lengthSq(double x, double y, double z) {
		return x * x + y * y + z * z;
	}
}
