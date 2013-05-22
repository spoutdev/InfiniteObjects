/*
 * This file is part of InfiniteObjects.
 *
 * Copyright (c) 2012 Spout LLC <http://www.spout.org/>
 * InfiniteObjects is licensed under the Spout License Version 1.
 *
 * InfiniteObjects is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * InfiniteObjects is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license,
 * including the MIT license.
 */
package org.spout.infobjects.instruction;

import java.util.Random;

import org.spout.api.util.config.ConfigurationNode;
import org.spout.infobjects.IWGO;
import org.spout.infobjects.exception.InstructionLoadingException;
import org.spout.infobjects.material.MaterialSetter;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.value.Value;
import org.spout.infobjects.value.ValueParser;

/**
 * An instruction to place a single block at a position. The outer boolean dictates whether the
 * block is considered as inner or outer to the material setter.
 */
public class BlockInstruction extends Instruction {
	private MaterialSetter setter;
	private Value x;
	private Value y;
	private Value z;
	private boolean outer;

	static {
		Instruction.register("block", BlockInstruction.class);
	}

	/**
	 * Constructs a new block instruction from the parent iWGO and its name.
	 *
	 * @param iwgo The parent iWGO
	 * @param name The name of this instruction
	 */
	public BlockInstruction(IWGO iwgo, String name) {
		super(iwgo, name);
	}

	/**
	 * Loads the block instruction from the properties node. Expected values are the position (x, y
	 * and z), the material setter and the outer boolean.
	 *
	 * @param properties The properties node to load from
	 * @throws InstructionLoadingException If the loading fails
	 */
	@Override
	public void load(ConfigurationNode properties) throws InstructionLoadingException {
		final IWGO iwgo = getIWGO();
		final ConfigurationNode positionNode = properties.getNode("position");
		setX(ValueParser.parse(positionNode.getNode("x").getString(), iwgo, this));
		setY(ValueParser.parse(positionNode.getNode("y").getString(), iwgo, this));
		setZ(ValueParser.parse(positionNode.getNode("z").getString(), iwgo, this));
		final MaterialSetter material = iwgo.getMaterialSetter(properties.getNode("material").getString());
		if (material == null) {
			throw new InstructionLoadingException("Material setter \"" + properties.getNode("material").getString()
					+ "\" does not exist");
		}
		setMaterialSetter(material);
		setOuter(Boolean.parseBoolean(properties.getNode("outer").getString()));
	}

	/**
	 * Executes the instruction. Sets the material from the material setter at the desired location.
	 */
	@Override
	public void execute() {
		setter.setMaterial(getIWGO().transform(x.getValue(), y.getValue(), z.getValue()), outer);
	}

	/**
	 * Randomizes each value representing the x, y and z position coordinated and calls the super
	 * method.
	 */
	@Override
	public void randomize() {
		super.randomize();
		x.calculate();
		y.calculate();
		z.calculate();
	}

	/**
	 * Sets the random of each value representing the x, y and z position coordinates to the
	 * provided one and calls the super method.
	 *
	 * @param random The random to use
	 */
	@Override
	public void setRandom(Random random) {
		super.setRandom(random);
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
	 * Returns true if the material is considered as outer, false for inner.
	 *
	 * @return True for outer, false for inner
	 */
	public boolean isOuter() {
		return outer;
	}

	/**
	 * Sets which material should be used from the picker. If true, the outer material will be used.
	 * Else the inner one will be used.
	 *
	 * @param outer
	 */
	public void setOuter(boolean outer) {
		this.outer = outer;
	}

	/**
	 * Gets the material setter which sets the material for the block.
	 *
	 * @return The material setter for the block
	 */
	public MaterialSetter getMaterialSetter() {
		return setter;
	}

	/**
	 * Sets the material setter which sets the material for the block.
	 *
	 * @param setter The material setter to use for the block
	 */
	public void setMaterialSetter(MaterialSetter setter) {
		this.setter = setter;
	}

	/**
	 * Gets the value representing the x coordinate.
	 *
	 * @return The value for the x coordinate
	 */
	public Value getX() {
		return x;
	}

	/**
	 * Sets the value representing the x coordinate.
	 *
	 * @param x The value for the x coordinate
	 */
	public void setX(Value x) {
		this.x = x;
	}

	/**
	 * Gets the value representing the y coordinate.
	 *
	 * @return The value for the y coordinate
	 */
	public Value getY() {
		return y;
	}

	/**
	 * Sets the value representing the y coordinate.
	 *
	 * @param y The value for the y coordinate
	 */
	public void setY(Value y) {
		this.y = y;
	}

	/**
	 * Gets the value representing the z coordinate.
	 *
	 * @return The value for the z coordinate
	 */
	public Value getZ() {
		return z;
	}

	/**
	 * Sets the value representing the z coordinate.
	 *
	 * @param z The value for the z coordinate
	 */
	public void setZ(Value z) {
		this.z = z;
	}

	/**
	 * Returns the string representing this block instruction.
	 *
	 * @return The string form of this instruction
	 */
	@Override
	public String toString() {
		return "BlockInstruction{setter=" + setter + ", x=" + x + ", y=" + y + ", z=" + z
				+ ", outer=" + outer + '}';
	}
}
