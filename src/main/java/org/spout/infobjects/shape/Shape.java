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

import org.spout.api.math.IntVector3;

import org.spout.infobjects.IWGO;
import org.spout.infobjects.material.MaterialPicker;
import org.spout.infobjects.value.Value;

public abstract class Shape {
	protected final IWGO parent;
	protected final IntVector3 position = new IntVector3(0, 0, 0);
	protected final MaterialPicker picker = null;

	public Shape(IWGO parent) {
		this.parent = parent;
	}

	public void setPosition(int x, int y, int z) {
		position.set(x, y, z);
	}

	public abstract void load(Map<String, Value> properties);

	public abstract void draw();
}
