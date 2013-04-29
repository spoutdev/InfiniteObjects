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
package org.spout.infobjects.variable;

import org.spout.api.util.Named;

import org.spout.infobjects.value.Value;

/**
 * Represents a variable, which is a named {@link org.spout.infobjects.value.Value}.
 */
public class Variable implements Value, Named {
	private final String name;
	private final Value value;

	/**
	 * Constructs a new variable from its name and value.
	 *
	 * @param name The name of the variable
	 * @param value The value of the variable
	 */
	public Variable(String name, Value value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Gets the raw value of this variable, that is, the {@link org.spout.infobjects.value.Value}
	 * value.
	 *
	 * @return The raw value
	 */
	public Value getRawValue() {
		return value;
	}

	/**
	 * Gets the real value of this variable.
	 *
	 * @return The real value of this variable
	 */
	@Override
	public double getValue() {
		return value.getValue();
	}

	/**
	 * Calculates the variable's value.
	 */
	@Override
	public void calculate() {
		value.calculate();
	}

	/**
	 * Gets the variable's name.
	 *
	 * @return The variable name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the string representation of the value.
	 *
	 * @return The string form of the value
	 */
	@Override
	public String toString() {
		return "Variable{name=" + name + ", value=" + value + '}';
	}
}
