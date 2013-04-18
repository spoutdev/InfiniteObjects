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
package org.spout.infobjects.instruction;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.spout.infobjects.IWGO;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.util.TypeFactory;
import org.spout.infobjects.variable.Variable;
import org.spout.infobjects.variable.VariableSource;

/**
 * An abstract instruction. This class provides the parent iWGO, the name of the instruction and
 * it's variables to the extending class. Register your own instruction with {@link #register(java.lang.String, java.lang.Class)}
 * so the iWGO loader can recognize it. Make sure there's at least one constructor with the same
 * arguments as the one for this class, as it's the one that will be called for construction.
 */
public abstract class Instruction implements VariableSource, RandomOwner {
	private static final TypeFactory<Instruction> INSTRUCTIONS = new TypeFactory<Instruction>(IWGO.class, String.class);
	private final IWGO iwgo;
	private final String name;
	private final Map<String, Variable> variables = new LinkedHashMap<String, Variable>();

	/**
	 * Constructs a new instruction from the parent iWGO and its name.
	 *
	 * @param iwgo The parent iWGO
	 * @param name The name of the instruction
	 */
	public Instruction(IWGO iwgo, String name) {
		this.iwgo = iwgo;
		this.name = name;
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
	 * Gets the name of this instruction.
	 *
	 * @return The name of this iWGO
	 */
	public String getName() {
		return name;
	}

	/**
	 * Executes this instruction. Each instruction is called once by placement call, unless another
	 * instruction calls the method.
	 */
	public abstract void execute();

	/**
	 * Sets the random of the variables for this instruction.
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
	}

	/**
	 * Randomizes the variables for this instruction.
	 */
	public void randomize() {
		for (Variable variable : variables.values()) {
			variable.calculate();
		}
	}

	/**
	 * Adds a variable to the instruction.
	 *
	 * @param variable The variable to add
	 */
	@Override
	public void addVariable(Variable variable) {
		variables.put(variable.getName(), variable);
	}

	/**
	 * Gets a variable from it's name from the instruction.
	 *
	 * @param name The name of the variable to lookup
	 * @return The variable if found, else null
	 */
	@Override
	public Variable getVariable(String name) {
		return variables.get(name);
	}

	/**
	 * Gets all the variables from the instruction. Changes to this collection are reflected in the
	 * instruction.
	 *
	 * @return All the variables as a collection
	 */
	@Override
	public Collection<Variable> getVariables() {
		return variables.values();
	}

	/**
	 * Returns the variable map (mapped as name and variable) from the instruction. Changes to this
	 * map are reflected in the instruction.
	 *
	 * @return The variable map.
	 */
	@Override
	public Map<String, Variable> getVariableMap() {
		return variables;
	}

	/**
	 * Checks if this instruction has the desired variable from the name.
	 *
	 * @param name The name of the variable to lookup
	 * @return True if found, false if not
	 */
	@Override
	public boolean hasVariable(String name) {
		return variables.containsKey(name);
	}

	/**
	 * Registers a new instruction. This is necessary for the loader to recognize it when loading a
	 * new iWGO. This methods required the type, which used in the iWGO configurations. For example:
	 * "shape", "repeat" or "block". The type must be unique.
	 *
	 * @param type The type of the instruction
	 * @param instruction The class of the instruction to register
	 */
	public static void register(String type, Class<? extends Instruction> instruction) {
		INSTRUCTIONS.register(type, instruction);
	}

	/**
	 * Creates a new instruction via reflection. The type is the one used during the registration.
	 * The IWGO and the name will be passed to the constructor.
	 *
	 * @param type The type as registered
	 * @param iwgo The iWGO to pass to the constructor
	 * @param name The name to pass to the constructor
	 * @return The new instruction
	 */
	public static Instruction newInstruction(String type, IWGO iwgo, String name) {
		return INSTRUCTIONS.newInstance(type, iwgo, name);

	}
}
