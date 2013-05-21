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
import org.spout.api.math.GenericMath;
import org.spout.api.math.IntVector3;
import org.spout.api.util.Named;

import org.spout.infobjects.condition.Condition;
import org.spout.infobjects.instruction.Instruction;
import org.spout.infobjects.material.MaterialSetter;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.variable.Variable;
import org.spout.infobjects.variable.VariableSource;

/**
 * A user defined WorldGeneratorObject. This WGO is loaded from a configuration. It is not thread
 * safe and must be synchronized externally. Alternatively more copies can be loaded using the
 * {@link org.spout.infobjects.IWGOLoader} class for unsynchronized thread local use.
 */
public class IWGO extends WorldGeneratorObject implements VariableSource, Named, RandomOwner {
	private final String name;
	private World world;
	private final IntVector3 position = new IntVector3(0, 0, 0);
	private final Map<String, Variable> variables = new LinkedHashMap<String, Variable>();
	private final Map<String, MaterialSetter> setters = new HashMap<String, MaterialSetter>();
	private final List<Condition> conditions = new ArrayList<Condition>();
	private final Map<String, Instruction> instructions = new LinkedHashMap<String, Instruction>();

	/**
	 * Constructs a new iWGO. To create a new iWGO, load it using
	 * {@link org.spout.infobjects.IWGOLoader} from a configuration.
	 *
	 * @param name The name of the iWGO
	 */
	public IWGO(String name) {
		if (name == null || name.trim().equals("")) {
			throw new IllegalArgumentException("Name cannot be null or empty");
		}
		this.name = name.trim();
	}

	/**
	 * Gets the iWGO name.
	 *
	 * @return The iWGO's name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Tests if the object can be placed in the world at the given coordinated. To do so it checks
	 * all the conditions. If any is false it fails.
	 *
	 * @param w The world
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @return True if the object can be placed, false if otherwise
	 */
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

	/**
	 * Places the object in world. To do so it executes all the instructions at the given
	 * coordinates.
	 *
	 * @param w The world
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 */
	@Override
	public void placeObject(World w, int x, int y, int z) {
		world = w;
		position.set(x, y, z);
		for (Instruction instruction : instructions.values()) {
			instruction.execute();
		}
	}

	/**
	 * Sets the iWGO's random to the provided one. The variables, material setters, condition and
	 * instruction will have their random instances changed.
	 *
	 * @param random The random to use
	 */
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

	/**
	 * Randomizes the iWGO. This will result (theoretically) in a new variation of the iWGO being
	 * outputted.
	 */
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

	/**
	 * Transforms the relative real coordinates to absolute for the iWGO and adds the world to the
	 * position data.
	 *
	 * @param xx The relative x coordinate
	 * @param yy The relative y coordinate
	 * @param zz The relative z coordinate
	 * @return The absolute coordinates plus the world
	 */
	public Point transform(double xx, double yy, double zz) {
		return transform((float) xx, (float) yy, (float) zz);
	}

	/**
	 * Transforms the relative integer coordinates to absolute for the iWGO and adds the world to
	 * the position data.
	 *
	 * @param xx The relative x coordinate
	 * @param yy The relative y coordinate
	 * @param zz The relative z coordinate
	 * @return The absolute coordinates plus the world
	 */
	public Point transform(float xx, float yy, float zz) {
		return new Point(world, xx + position.getX(), yy + position.getY(), zz + position.getZ());
	}

	/**
	 * Transforms the relative point to absolute for the iWGO.
	 *
	 * @param pos The relative point
	 * @return The absolute point
	 */
	public Point transform(Point pos) {
		return pos.add(position.getX(), position.getY(), position.getZ());
	}

	/**
	 * Adds a variable to the iWGO.
	 *
	 * @param variable The variable to add
	 */
	@Override
	public void addVariable(Variable variable) {
		variables.put(variable.getName(), variable);
	}

	/**
	 * Gets a variable from it's name from the iWGO.
	 *
	 * @param name The name of the variable to lookup
	 * @return The variable if found, else null
	 */
	@Override
	public Variable getVariable(String name) {
		return variables.get(name);
	}

	/**
	 * Gets all the variables from the iWGO. Changes to this collection are reflected in the iWGO.
	 *
	 * @return All the variables as a collection
	 */
	@Override
	public Collection<Variable> getVariables() {
		return variables.values();
	}

	/**
	 * Gets the variable map (mapped as name and variable) from the iWGO. Changes to this map are
	 * reflected in the iWGO.
	 *
	 * @return The variable map.
	 */
	@Override
	public Map<String, Variable> getVariableMap() {
		return variables;
	}

	/**
	 * Checks if this iWGO has the desired variable from the name.
	 *
	 * @param name The name of the variable to lookup
	 * @return True if found, false if not
	 */
	@Override
	public boolean hasVariable(String name) {
		return variables.containsKey(name);
	}

	/**
	 * Adds a material setter to the iWGO.
	 *
	 * @param setter The material setter to add
	 */
	public void addMaterialSetter(MaterialSetter setter) {
		setters.put(setter.getName(), setter);
	}

	/**
	 * Gets a material setter from it's name from the iWGO.
	 *
	 * @param name The name of the material setter to lookup
	 * @return The material setter if found, else null
	 */
	public MaterialSetter getMaterialSetter(String name) {
		return setters.get(name);
	}

	/**
	 * Gets all the material setters from the iWGO. Changes to this collection are reflected in the
	 * iWGO.
	 *
	 * @return All the material setters as a collection
	 */
	public Collection<MaterialSetter> getMaterialSetters() {
		return setters.values();
	}

	/**
	 * Gets the material setter map (mapped as name and material setter) from the iWGO. Changes to
	 * this map are reflected in the iWGO.
	 *
	 * @return The material setter map.
	 */
	public Map<String, MaterialSetter> getMaterialSetterMap() {
		return setters;
	}

	/**
	 * Checks if this iWGO has the desired material setter from the name. use.
	 *
	 * @param name The name of the material setter to lookup
	 * @return True if found, false if not
	 */
	public boolean hasMaterialSetter(String name) {
		return setters.containsKey(name);
	}

	/**
	 * Adds a condition to the iWGO.
	 *
	 * @param condition The condition to add
	 */
	public void addCondition(Condition condition) {
		conditions.add(condition);
	}

	/**
	 * Gets all the conditions from the iWGO. Changes to this list are reflected in the iWGO.
	 *
	 * @return All the conditions as a list
	 */
	public List<Condition> getConditions() {
		return conditions;
	}

	/**
	 * Adds a material setter to the iWGO.
	 *
	 * @param instruction The material setter to add
	 */
	public void addInstruction(Instruction instruction) {
		instructions.put(instruction.getName(), instruction);
	}

	/**
	 * Gets a instruction from it's name from the iWGO.
	 *
	 * @param name The name of the instruction to lookup
	 * @return The instruction if found, else null
	 */
	public Instruction getInstruction(String name) {
		return instructions.get(name);
	}

	/**
	 * Gets all the instructions from the iWGO. Changes to this collection are reflected in the
	 * iWGO.
	 *
	 * @return All the instructions as a collection
	 */
	public Collection<Instruction> getInstructions() {
		return instructions.values();
	}

	/**
	 * Gets the instruction map (mapped as name and instruction) from the iWGO. Changes to this map
	 * are reflected in the iWGO.
	 *
	 * @return The instruction map.
	 */
	public Map<String, Instruction> getInstructionMap() {
		return instructions;
	}

	/**
	 * Checks if this iWGO has the desired instruction from the name.
	 *
	 * @param name The name of the instruction to lookup
	 * @return True if found, false if not
	 */
	public boolean hasInstruction(String name) {
		return instructions.containsKey(name);
	}
}
