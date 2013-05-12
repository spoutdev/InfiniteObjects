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
package org.spout.infobjects.condition;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.spout.api.material.BlockMaterial;
import org.spout.api.util.config.ConfigurationNode;

import org.spout.infobjects.IWGO;
import org.spout.infobjects.exception.ConditionLoadingException;
import org.spout.infobjects.util.IWGOUtils;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.value.Value;
import org.spout.infobjects.value.ValueParser;

/**
 * An abstract class to represent a shape based condition. Provides the materials to check for, the
 * condition's mode and its position.
 */
public abstract class ShapeCondition extends Condition {
	private final Set<BlockMaterial> materials = new HashSet<BlockMaterial>();
	private ConditionMode mode;
	private Value x;
	private Value y;
	private Value z;

	/**
	 * Constructs a new shape condition from its parent iWGO.
	 *
	 * @param iwgo The parent iWGO
	 */
	public ShapeCondition(IWGO iwgo) {
		super(iwgo);
	}

	/**
	 * Gets the checking mode of this condition.
	 *
	 * @return The condition's mode
	 */
	public ConditionMode getMode() {
		return mode;
	}

	/**
	 * Sets the condition's mode. The mode is used by the extending class to determine if it should
	 * check for the absence or presence of the materials.
	 *
	 * @param mode The mode to set
	 */
	public void setMode(ConditionMode mode) {
		this.mode = mode;
	}

	/**
	 * Gets the {@link org.spout.infobjects.value.Value} representing the x coordinate.
	 *
	 * @return The value for x
	 */
	public Value getX() {
		return x;
	}

	/**
	 * Sets the {@link org.spout.infobjects.value.Value} representing the x coordinate.
	 *
	 * @param x The value for x
	 */
	public void setX(Value x) {
		this.x = x;
	}

	/**
	 * Gets the {@link org.spout.infobjects.value.Value} representing the y coordinate.
	 *
	 * @return The value for y
	 */
	public Value getY() {
		return y;
	}

	/**
	 * Sets the {@link org.spout.infobjects.value.Value} representing the y coordinate.
	 *
	 * @param y The value for y
	 */
	public void setY(Value y) {
		this.y = y;
	}

	/**
	 * Gets the {@link org.spout.infobjects.value.Value} representing the z coordinate.
	 *
	 * @return The value for z
	 */
	public Value getZ() {
		return z;
	}

	/**
	 * Sets the {@link org.spout.infobjects.value.Value} representing the z coordinate.
	 *
	 * @param z The value for z
	 */
	public void setZ(Value z) {
		this.z = z;
	}

	/**
	 * Sets the {@link org.spout.infobjects.value.Value}s representing the x, y and z coordinates.
	 *
	 * @param x The value for z
	 * @param y The value for Y
	 * @param z The value for Y
	 */
	public void setPosition(Value x, Value y, Value z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	/**
	 * Sets the position values for x, y and z. The positions are passed as a name and
	 * {@link org.spout.infobjects.value.Value} map. The name is as declared in the iWGO
	 * configuration. Expected size values are x, y, and z.
	 *
	 * @param position The position as a map
	 * @throws ConditionLoadingException If the x, y, or z coordinate is missing
	 */
	public void setPosition(Map<String, Value> position) throws ConditionLoadingException {
		if (!position.containsKey("x")) {
			throw new ConditionLoadingException("x coordinate for position is missing");
		}
		if (!position.containsKey("y")) {
			throw new ConditionLoadingException("y coordinate for position is missing");
		}
		if (!position.containsKey("z")) {
			throw new ConditionLoadingException("z coordinate for position is missing");
		}
		setPosition(position.get("x"), position.get("y"), position.get("z"));
	}

	/**
	 * Sets the size of the cuboid volume.
	 *
	 * @param sizes The sizes mapped as name and value
	 * @throws ConditionLoadingException If any of the expect values are missing
	 */
	public abstract void setSize(Map<String, Value> sizes) throws ConditionLoadingException;

	/**
	 * Adds a block material to this condition. The materials are used in checks, where the
	 * condition's volume is tested for either absence or presence of the materials.
	 *
	 * @param material The material to add
	 */
	public void addBlockMaterial(BlockMaterial material) {
		materials.add(material);
	}

	/**
	 * Remove the material from the condition.
	 *
	 * @param material The material to remove
	 */
	public void removeMaterial(BlockMaterial material) {
		materials.remove(material);
	}

	/**
	 * Gets the materials of this condition. Changes in the set are reflected in the condition.
	 *
	 * @return The materials as a set
	 */
	public Set<BlockMaterial> getMaterials() {
		return materials;
	}

	/**
	 * Loads the condition from the properties node in the condition declaration. Expected
	 * properties are: the mode, the size, the position and the list of materials to check.
	 *
	 * @param properties The properties node to load
	 * @throws ConditionLoadingException If the loading fails
	 */
	@Override
	public void load(ConfigurationNode properties) throws ConditionLoadingException {
		setMode(ConditionMode.valueOf(properties.getNode("mode").getString().toUpperCase()));
		setSize(ValueParser.parse(IWGOUtils.toStringMap(properties.getNode("size")), getIWGO()));
		setPosition(ValueParser.parse(IWGOUtils.toStringMap(properties.getNode("position")), getIWGO()));
		for (String name : properties.getNode("check").getStringList()) {
			addBlockMaterial(IWGOUtils.tryGetBlockMaterial(name));
		}
	}

	@Override
	public void randomize() {
		x.calculate();
		y.calculate();
		z.calculate();
	}

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
	 * An enum with the modes a condition can have when checking the condition volume for materials.
	 * The include mode means the condition should return true only if all the materials are present
	 * in the volume. The exclude mode means it should check that none are present.
	 */
	public static enum ConditionMode {
		INCLUDE, EXCLUDE;

		/**
		 * Runs the check for a material according to the mode. If the mode is include, this method
		 * will return false if the material is in the provided set. If it is exclude, it will
		 * return false if it is not.
		 *
		 * @param material The material to check
		 * @param materials The material set to check in
		 * @return True or false depending on the mode and the presence or absence of the material
		 * in the set
		 */
		public boolean check(BlockMaterial material, Set<BlockMaterial> materials) {
			switch (this) {
				case INCLUDE:
					return materials.contains(material);
				case EXCLUDE:
					return !materials.contains(material);
				default:
					return false;
			}
		}
	}
}
