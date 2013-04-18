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

import org.spout.infobjects.IWGO;
import org.spout.infobjects.exception.ShapeLoadingException;
import org.spout.infobjects.material.MaterialSetter;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.util.TypeFactory;
import org.spout.infobjects.value.Value;

/**
 * An abstract shape. This class provides the parent iWGO, position coordinate {@link org.spout.infobjects.value.Value}s
 * and the material setter.
 */
public abstract class Shape implements RandomOwner {
	private static final TypeFactory<Shape> SHAPES = new TypeFactory<Shape>(IWGO.class);
	protected final IWGO iwgo;
	protected Value x;
	protected Value y;
	protected Value z;
	protected MaterialSetter setter;

	static {
		register("cuboid", Cuboid.class);
		register("line", Line.class);
		register("sphere", Sphere.class);
	}

	/**
	 * Construct a new iWGO from its parent iWGO.
	 *
	 * @param iwgo The parent iWGO
	 */
	public Shape(IWGO iwgo) {
		this.iwgo = iwgo;
	}

	/**
	 * Gets the parent iWGO.
	 *
	 * @return The parent iWGO
	 */
	public IWGO getIWGO() {
		return iwgo;
	}

	/**
	 * Gets the material setter for the shape.
	 *
	 * @return The material setter
	 */
	public MaterialSetter getMaterialSetter() {
		return setter;
	}

	/**
	 * Sets the material setter to be used when drawing the shape.
	 *
	 * @param setter The material setter
	 */
	public void setMaterialSetter(MaterialSetter setter) {
		this.setter = setter;
	}

	/**
	 * Gets the x coordinate value.
	 *
	 * @return The x coordinate value
	 */
	public Value getX() {
		return x;
	}

	/**
	 * Sets the x coordinate value.
	 *
	 * @param x The value for the x coordinate
	 */
	public void setX(Value x) {
		this.x = x;
	}

	/**
	 * Gets the y coordinate value.
	 *
	 * @return The y coordinate value
	 */
	public Value getY() {
		return y;
	}

	/**
	 * Sets the y coordinate value.
	 *
	 * @param y The value for the x coordinate
	 */
	public void setY(Value y) {
		this.y = y;
	}

	/**
	 * Gets the z coordinate value.
	 *
	 * @return The z coordinate value
	 */
	public Value getZ() {
		return z;
	}

	/**
	 * Sets the z coordinate value.
	 *
	 * @param z The value for the x coordinate
	 */
	public void setZ(Value z) {
		this.z = z;
	}

	/**
	 * Sets the x, y and z coordinate values.
	 *
	 * @param x The value for the x coordinate
	 * @param y The value for the y coordinate
	 * @param z The value for the z coordinate
	 */
	public void setPosition(Value x, Value y, Value z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	/**
	 * Sets the x, y and z coordinates for the position from a string, value map. The expected keys
	 * are "x", "y" and "z". This method will throw an exception if any are missing. This method is
	 * used in loading the shape for the iWGO.
	 *
	 * @param position The position as a string, value map
	 * @throws ShapeLoadingException If any of the "x", "y" or "z" keys are missing
	 */
	public void setPosition(Map<String, Value> position) throws ShapeLoadingException {
		if (!position.containsKey("x")) {
			throw new ShapeLoadingException("x coordinate for position is missing");
		}
		if (!position.containsKey("y")) {
			throw new ShapeLoadingException("y coordinate for position is missing");
		}
		if (!position.containsKey("z")) {
			throw new ShapeLoadingException("z coordinate for position is missing");
		}
		setPosition(position.get("x"), position.get("y"), position.get("z"));
	}

	/**
	 * Checks the map for the presence of the "x", "y" and "z" keys. This method will throw an
	 * exception if any of the keys are missing. This method doesn't actually set the size, as this
	 * depends on the extending class, but it's not abstract to reduce duplicate code. Overrides
	 * that do not use the "x", "y" or "z" keys should not call the super method.
	 *
	 * @param sizes The size values as a string, value map
	 * @throws ShapeLoadingException If any of the "x", "y" or "z" keys are missing
	 */
	public void setSize(Map<String, Value> sizes) throws ShapeLoadingException {
		if (!sizes.containsKey("x")) {
			throw new ShapeLoadingException("x size is missing");
		}
		if (!sizes.containsKey("y")) {
			throw new ShapeLoadingException("y size is missing");
		}
		if (!sizes.containsKey("z")) {
			throw new ShapeLoadingException("z size is missing");
		}
	}

	/**
	 * Randomizes the position of the shape by recalculating the x, y and z coordinate values. The
	 * position will only change is the coordinate values are random.
	 */
	public void randomize() {
		x.calculate();
		y.calculate();
		z.calculate();
	}

	/**
	 * Sets the random for the x, y and z coordinate values if they implement {@link org.spout.infobjects.util.RandomOwner}.
	 *
	 * @param random The random to use
	 */
	@Override
	public void setRandom(Random random) {
		if (x instanceof RandomOwner) {
			((RandomOwner) x).setRandom(random);
		}
		if (y instanceof RandomOwner) {
			((RandomOwner) y).setRandom(random);
		}
		if (z instanceof RandomOwner) {
			((RandomOwner) z).setRandom(random);
		}
	}

	/**
	 * Draws the shape. This method is abstract and so the result of this call depends on the
	 * extending class. This method is called during placement of the iWGO by the {@link org.spout.infobjects.instruction.ShapeInstruction}
	 * to add the shapes which compose the iWGO structure. Implementations of this method should
	 * iterate through all the blocks that compose the shape, of size defined by the size values, at
	 * the position defined by the position values. It should then call the material setter to set
	 * the material for each block, with outer being true if the block is at the edge of the shape,
	 * false if it is inside.
	 */
	public abstract void draw();

	/**
	 * Registers a type of shape so it may be recognized and used by the {@link org.spout.infobjects.IWGOLoader}
	 * during loading of iWGOs. The type is what will be used in the iWGO configuration. Example:
	 * "cuboid", "sphere" or "line". The type must be unique.
	 *
	 * @param type The type of shape
	 * @param shape The class for the shape to register
	 */
	public static void register(String type, Class<? extends Shape> shape) {
		SHAPES.register(type, shape);
	}

	/**
	 * Constructs a new instance of a shape of the desired type.
	 *
	 * @param type The type of the shape, as used in the iWGO configuration
	 * @param iwgo The parent iWGO to be passed to the constructor
	 * @return The new shape instance
	 */
	public static Shape newShape(String type, IWGO iwgo) {
		return SHAPES.newInstance(type, iwgo);
	}
}
