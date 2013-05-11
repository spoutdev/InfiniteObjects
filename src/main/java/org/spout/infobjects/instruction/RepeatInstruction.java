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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spout.api.util.config.ConfigurationNode;

import org.spout.infobjects.IWGO;
import org.spout.infobjects.exception.InstructionLoadingException;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.value.IncrementableValue;
import org.spout.infobjects.value.Value;
import org.spout.infobjects.value.ValueParser;
import org.spout.infobjects.variable.Variable;

/**
 * An instruction for repeating another instruction a specific number of time. This instruction can
 * also increment iWGO variables for each repeat. The values of the variables are reset once
 * execution is over.
 */
public class RepeatInstruction extends Instruction {
	private Instruction repeat;
	private Value times;
	private final Set<IncrementableValue> incrementables = new HashSet<IncrementableValue>();

	static {
		Instruction.register("repeat", RepeatInstruction.class);
	}

	/**
	 * Constructs a new repeat instruction from the parent iWGO and its name.
	 *
	 * @param iwgo The parent iWGO
	 * @param name The name
	 */
	public RepeatInstruction(IWGO iwgo, String name) {
		super(iwgo, name);
	}

	/**
	 * Gets the instruction to repeat.
	 *
	 * @return The instruction to repeat
	 */
	public Instruction getRepeat() {
		return repeat;
	}

	/**
	 * Sets the instruction to repeat.
	 *
	 * @param repeat The instruction to repeat
	 */
	public void setRepeat(Instruction repeat) {
		this.repeat = repeat;
	}

	/**
	 * Gets the number of times to repeat the instruction.
	 *
	 * @return The number of times to repeat the instruction
	 */
	public Value getTimes() {
		return times;
	}

	/**
	 * Sets the value representing the number of time to repeat the instruction.
	 *
	 * @param times The number of times to repeat the instruction
	 */
	public void setTimes(Value times) {
		this.times = times;
	}

	/**
	 * Adds a named value to be incremented. This value should have the same name as the iWGO
	 * variable that needs to be incremented. The incrementable value should have for value the
	 * value of the iWGO variable.
	 *
	 * @param name The name of the iWGO variable to increment
	 * @param value The incrementable value to increment
	 */
	public void addIncrementableValue(String name, IncrementableValue value) {
		getIWGO().addVariable(new Variable(name, value));
		incrementables.add(value);
	}

	/**
	 * Gets all the incrementable values for this instruction as a set. Changes to this set are
	 * reflected in the instruction.
	 *
	 * @return A set of all the incrementable values
	 */
	public Set<IncrementableValue> getIncrementables() {
		return incrementables;
	}

	/**
	 * Load the repeat instruction from the properties node. The expected values are the instruction
	 * to repeat, the times value, the variables to increment with their increments.
	 *
	 * @param properties The properties node to load from
	 * @throws InstructionLoadingException If the loading fails
	 */
	@Override
	public void load(ConfigurationNode properties) throws InstructionLoadingException {
		final IWGO iwgo = getIWGO();
		final Instruction toRepeat = iwgo.getInstruction(properties.getNode("repeat").getString());
		if (toRepeat == null) {
			throw new InstructionLoadingException("Repeat instruction \""
					+ properties.getNode("repeat").getString() + "\" does not exist");
		}
		setRepeat(toRepeat);
		setTimes(ValueParser.parse(properties.getNode("times").getString(), iwgo, this));
		final ConfigurationNode incrementNode = properties.getNode("increment");
		for (String key : incrementNode.getKeys(false)) {
			final Variable increment = iwgo.getVariable(key);
			if (increment == null) {
				throw new InstructionLoadingException("Increment variable \"" + key + "\" does not exist");
			}
			addIncrementableValue(key, new IncrementableValue(increment.getRawValue(),
					ValueParser.parse(incrementNode.getNode(key).getString(), iwgo, this)));
		}
	}

	/**
	 * Randomizes the value for the times to repeat the repeated instruction and calls the super
	 * method.
	 */
	@Override
	public void randomize() {
		super.randomize();
		times.calculate();
	}

	/**
	 * Sets the random of the value for the times to repeat the repeated instruction and calls the
	 * super method.
	 *
	 * @param random THe random to use
	 */
	@Override
	public void setRandom(Random random) {
		super.setRandom(random);
		if (times instanceof RandomOwner) {
			((RandomOwner) times).setRandom(random);
		}
	}

	/**
	 * Executes this instruction. Executes the repeated instruction for the number of times
	 * specified by the time value, incrementing all the incrementable values once during each
	 * iteration. Resets all the incrementable values to the original values once execution is over.
	 */
	@Override
	public void execute() {
		for (int i = (int) times.getValue(); i >= 0; i--) {
			repeat.execute();
			for (IncrementableValue increment : incrementables) {
				increment.increment();
			}
			repeat.randomize();
		}
		for (IncrementableValue increment : incrementables) {
			increment.reset();
		}
	}

	/**
	 * Returns the string representation of this repeat instruction.
	 *
	 * @return The string form of this instruction
	 */
	@Override
	public String toString() {
		return "RepeatInstruction{repeat=" + repeat + ", times=" + times + ", incrementables="
				+ incrementables + '}';
	}
}
