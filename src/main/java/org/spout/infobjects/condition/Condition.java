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

	public Condition(IWGO iwgo) {
		this.iwgo = iwgo;
	}

	public IWGO getIWGO() {
		return iwgo;
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

	public void addBlockMaterial(BlockMaterial material) {
		materials.add(material);
	}

	public void removeMaterial(BlockMaterial material) {
		materials.remove(material);
	}

	public Set<BlockMaterial> getMaterials() {
		return materials;
	}

	public ConditionMode getMode() {
		return mode;
	}

	public void setMode(ConditionMode mode) {
		this.mode = mode;
	}

	public abstract boolean check();

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

	public static void register(String type, Class<? extends Condition> condition) {
		CONDITIONS.register(type, condition);
	}

	public static Condition newCondition(String type, IWGO iwgo) {
		return CONDITIONS.newInstance(type, iwgo);
	}

	public static enum ConditionMode {
		INCLUDE, EXCLUDE;

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
