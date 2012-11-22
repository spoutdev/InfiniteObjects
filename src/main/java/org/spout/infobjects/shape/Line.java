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
import java.util.Random;

import org.spout.api.geo.discrete.Point;
import org.spout.api.util.BlockIterator;

import org.spout.infobjects.IWGO;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.value.Value;

public class Line extends Shape {
	private Value lengthX;
	private Value lengthY;
	private Value lengthZ;

	public Line(IWGO iwgo) {
		super(iwgo);
	}

	@Override
	public void setSize(Map<String, Value> sizes) {
		super.setSize(sizes);
		lengthX = sizes.get("x");
		lengthY = sizes.get("y");
		lengthZ = sizes.get("z");
	}

	@Override
	public void draw() {
		final Point start = iwgo.transform(x.getValue(), y.getValue(), z.getValue());
		final BlockIterator line = new BlockIterator(start, start.add(lengthX.getValue(),
				lengthY.getValue(), lengthZ.getValue()));
		while (line.hasNext()) {
			setter.setMaterial(line.next().getPosition(), true);
		}
	}

	@Override
	public void randomize() {
		super.randomize();
		lengthX.calculate();
		lengthY.calculate();
		lengthZ.calculate();
	}

	@Override
	public void setRandom(Random random) {
		super.setRandom(random);
		if (lengthX instanceof RandomOwner) {
			((RandomOwner) lengthX).setRandom(random);
		}
		if (lengthY instanceof RandomOwner) {
			((RandomOwner) lengthY).setRandom(random);
		}
		if (lengthZ instanceof RandomOwner) {
			((RandomOwner) lengthZ).setRandom(random);
		}
	}

	@Override
	public String toString() {
		return "Line{x=" + x + ", y=" + y + ", z=" + z + ", setter=" + setter + ", lengthX="
				+ lengthX + ", lengthY=" + lengthY + ", lengthZ=" + lengthZ + '}';
	}
}
