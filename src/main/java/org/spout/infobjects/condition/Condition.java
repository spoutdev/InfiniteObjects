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

import org.spout.infobjects.IWGO;
import org.spout.infobjects.exception.ConditionLoadingException;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.util.TypeFactory;
import org.spout.infobjects.value.Value;

/**
 * An abstract condition. This class stores all the basics for a condition, including the materials,
 * the position, the mode (exclude or include) and the parent iWGO. Extend this class, implement {@link #check()}
 * and override {@link #setSize(java.util.Map)} to create your own condition. For the loader to
 * recognize it it will also need to be registered with {@link #register(java.lang.String, java.lang.Class)}.
 * It is important to make sure the extending class has a constructor with the same arguments as
 * this class, as it is necessary for the creations of new conditions via reflection.
 */
public abstract class Condition implements RandomOwner {
	private static final TypeFactory<Condition> CONDITIONS = new TypeFactory<Condition>(IWGO.class);
	protected final Set<BlockMaterial> materials = new HashSet<BlockMaterial>();
	protected final IWGO iwgo;
	protected Value x;
	protected Value y;
	protected Value z;
	protected ConditionMode mode;

	static {
		register("cuboid", CuboidCondition.class);
	}

	/**
	 * Constructs a new condition.
	 *
	 * @param iwgo The parent iWGO
	 */
	public Condition(IWGO iwgo) {
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
	 * Partial method for setting the size of the condition. As the actual size parameters depend on
	 * the extending class, this method cannot set them. This method does provide exception throwing
	 * for shapes which have x, y, and z size parameters when one of them is missing. In such case a
	 * call to the super method can be used. The sizes are stored as map, mapped as name and value.
	 * The name is as declared in the iWGO configuration.
	 *
	 * @param sizes The size as a map
	 * @throws ConditionLoadingException If the x, y, or z size is missing
	 */
	public void setSize(Map<String, Value> sizes) throws ConditionLoadingException {
		if (!sizes.containsKey("x")) {
			throw new ConditionLoadingException("x size is missing");
		}
		if (!sizes.containsKey("y")) {
			throw new ConditionLoadingException("y size is missing");
		}
		if (!sizes.containsKey("z")) {
			throw new ConditionLoadingException("z size is missing");
		}
	}

	/**
	 * Sets the position values for x, y and z. The positions are passes as a name and {@link org.spout.infobjects.value.Value}
	 * map. The name is as declared in the iWGO configuration. Expected size values are x, y, and z.
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
		x = position.get("x");
		y = position.get("y");
		z = position.get("z");
	}

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
	 * An abstract method which is implemented by the extending class. The implementation has to
	 * determine whether or not the volume of this condition, starting from the position and ending
	 * at the position plus the size, contains or not the materials from {@link #getMaterials()}.
	 * The actual shape of the volume is decided by the implementation. Whether or not the
	 * implementation should check for the presence (include) or absence (exclude) of the materials
	 * is decided by the mode from {@link #getMode()}. If the check is successful, this method
	 * should return true, false if it isn't. Take a look at {@link CuboidCondition} for an example
	 * of an implementation.
	 *
	 * @return True if the check is successful, false if not.
	 */
	public abstract boolean check();

	/**
	 * Recalculates the position of this condition. The position will only change if the {@link org.spout.infobjects.value.Value}s
	 * representing it are randomizable.
	 */
	public void randomize() {
		x.calculate();
		y.calculate();
		z.calculate();
	}

	/**
	 * Sets the randoms of the position {@link org.spout.infobjects.value.Value}s to the provided
	 * one, if they implement {@link org.spout.infobjects.util.RandomOwner}.
	 *
	 * @param random The random to set
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
	 * Registers a new condition. This is necessary for the loader to recognize it when loading a
	 * new iWGO. This methods required the type, which is also the name used in the iWGO
	 * configurations. For example: "cuboid", "pyramid" or "cylinder".
	 *
	 * @param type The type, also the name of the condition
	 * @param condition The class of the condition to register
	 */
	public static void register(String type, Class<? extends Condition> condition) {
		CONDITIONS.register(type, condition);
	}

	/**
	 * Creates a new condition via reflection. The type is the one used during the registration and
	 * the IWGO is the one to be passed to the constructor.
	 *
	 * @param type The type as registered
	 * @param iwgo The iWGO to pass to the constructor during construction
	 * @return The new condition
	 */
	public static Condition newCondition(String type, IWGO iwgo) {
		return CONDITIONS.newInstance(type, iwgo);
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
