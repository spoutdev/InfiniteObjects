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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spout.infobjects.IWGO;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.value.IncrementableValue;
import org.spout.infobjects.value.Value;
import org.spout.infobjects.variable.Variable;

public class RepeatInstruction extends Instruction {
	private Instruction repeat;
	private Value times;
	private final Set<IncrementableValue> incrementables = new HashSet<IncrementableValue>();

	public RepeatInstruction(IWGO iwgo, String name) {
		super(iwgo, name);
	}

	public void setRepeat(Instruction repeat) {
		this.repeat = repeat;
	}

	public void setTimes(Value times) {
		this.times = times;
	}

	public Instruction getRepeat() {
		return repeat;
	}

	public Value getTimes() {
		return times;
	}

	public void addIncrementableValue(String name, IncrementableValue value) {
		getIWGO().addVariable(new Variable(name, value));
		incrementables.add(value);
	}

	public Collection<IncrementableValue> getIncrementables() {
		return incrementables;
	}

	@Override
	public void randomize() {
		super.randomize();
		times.calculate();
	}

	@Override
	public void setRandom(Random random) {
		super.setRandom(random);
		if (times instanceof RandomOwner) {
			((RandomOwner) times).setRandom(random);
		}
	}

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

	@Override
	public String toString() {
		return "RepeatInstruction{repeat=" + repeat + ", times=" + times + ", incrementables="
				+ incrementables + '}';
	}
}
