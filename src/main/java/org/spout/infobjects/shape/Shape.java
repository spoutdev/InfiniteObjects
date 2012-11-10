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
import org.spout.infobjects.material.MaterialPicker;
import org.spout.infobjects.util.TypeFactory;
import org.spout.infobjects.value.Value;

public abstract class Shape {
	private static final TypeFactory<Shape> SHAPES = new TypeFactory<Shape>(IWGO.class);
	protected final IWGO iwgo;
	protected Value x;
	protected Value y;
	protected Value z;
	protected MaterialPicker picker;

	static {
		register("cuboid", Cuboid.class);
		register("line", Line.class);
		register("sphere", Sphere.class);
	}

	public Shape(IWGO iwgo) {
		this.iwgo = iwgo;
	}

	public IWGO getIWGO() {
		return iwgo;
	}

	public MaterialPicker getMaterialPicker() {
		return picker;
	}

	public void setMaterialPicker(MaterialPicker picker) {
		this.picker = picker;
	}

	public Value getX() {
		return x;
	}

	public void setX(Value x) {
		this.x = x;
	}

	public Value getY() {
		return y;
	}

	public void setY(Value y) {
		this.y = y;
	}

	public Value getZ() {
		return z;
	}

	public void setZ(Value z) {
		this.z = z;
	}

	public void setPosition(Value x, Value y, Value z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setPosition(Map<String, Value> position) {
		x = position.get("x");
		y = position.get("y");
		z = position.get("z");
	}

	public abstract void configure(Map<String, Value> properties);

	public void randomize() {
		x.calculate();
		y.calculate();
		z.calculate();
	}

	public abstract void draw();

	public static void register(String type, Class<? extends Shape> shape) {
		SHAPES.register(type, shape);
	}

	public static Shape newShape(String type, IWGO iwgo) {
		return SHAPES.newInstance(type, iwgo);
	}
}
