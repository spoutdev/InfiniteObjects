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
package org.spout.infobjects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.IntVector3;
import org.spout.api.util.Named;

import org.spout.infobjects.condition.Condition;
import org.spout.infobjects.instruction.Instruction;
import org.spout.infobjects.material.MaterialSetter;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.variable.Variable;
import org.spout.infobjects.variable.VariableSource;

public class IWGO extends WorldGeneratorObject implements VariableSource, Named, RandomOwner {
	private final String name;
	private World world;
	private final IntVector3 position = new IntVector3(0, 0, 0);
	private final Map<String, Variable> variables = new LinkedHashMap<String, Variable>();
	private final Map<String, MaterialSetter> setters = new HashMap<String, MaterialSetter>();
	private final List<Condition> conditions = new ArrayList<Condition>();
	private final Map<String, Instruction> instructions = new LinkedHashMap<String, Instruction>();

	public IWGO(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		world = w;
		position.set(x, y, z);
		for (Condition condition : conditions) {
			if (!condition.check()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		world = w;
		position.set(x, y, z);
		for (Instruction instruction : instructions.values()) {
			instruction.execute();
		}
	}

	@Override
	public void setRandom(Random random) {
		for (Variable variable : variables.values()) {
			if (variable.getRawValue() instanceof RandomOwner) {
				((RandomOwner) variable.getRawValue()).setRandom(random);
			}
		}
		for (MaterialSetter setter : setters.values()) {
			if (setter instanceof RandomOwner) {
				((RandomOwner) setter).setRandom(random);
			}
		}
		for (Condition condition : conditions) {
			condition.setRandom(random);
		}
		for (Instruction instruction : instructions.values()) {
			instruction.setRandom(random);
		}
	}

	public void randomize() {
		for (Variable variable : variables.values()) {
			variable.calculate();
		}
		for (Condition condition : conditions) {
			condition.randomize();
		}
		for (Instruction instruction : instructions.values()) {
			instruction.randomize();
		}
	}

	public Point transform(double xx, double yy, double zz) {
		return transform((int) xx, (int) yy, (int) zz);
	}

	public Point transform(int xx, int yy, int zz) {
		return transform(new Point(world, xx, yy, zz));
	}

	public Point transform(Point pos) {
		return pos.add(position.getX(), position.getY(), position.getZ());
	}

	@Override
	public void addVariable(Variable variable) {
		variables.put(variable.getName(), variable);
	}

	@Override
	public Variable getVariable(String name) {
		return variables.get(name);
	}

	@Override
	public Collection<Variable> getVariables() {
		return variables.values();
	}

	@Override
	public Map<String, Variable> getVariableMap() {
		return variables;
	}

	@Override
	public boolean hasVariable(String name) {
		return variables.containsKey(name);
	}

	public void addMaterialSetter(MaterialSetter setter) {
		setters.put(setter.getName(), setter);
	}

	public MaterialSetter getMaterialSetter(String name) {
		return setters.get(name);
	}

	public Collection<MaterialSetter> getMaterialSetters() {
		return setters.values();
	}

	public Map<String, MaterialSetter> getMaterialSetterMap() {
		return setters;
	}

	public void addCondition(Condition condition) {
		conditions.add(condition);
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void addInstruction(Instruction instruction) {
		instructions.put(instruction.getName(), instruction);
	}

	public Instruction getInstruction(String name) {
		return instructions.get(name);
	}

	public Collection<Instruction> getInstructions() {
		return instructions.values();
	}

	public Map<String, Instruction> getInstructionMap() {
		return instructions;
	}
}
