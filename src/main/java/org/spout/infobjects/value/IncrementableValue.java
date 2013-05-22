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
package org.spout.infobjects.value;

/**
 * Represents a value that can be incremented. This is done by wrapping an original value.
 * Incrementing does not alter the real value of the original value and can be reset. The amount to
 * increment is determined by a second value, which is recalculated and added to the increment sum
 * for each incrementation.
 */
public class IncrementableValue implements Value {
	private final Value value;
	private final Value increment;
	private double totalIncrement = 0;

	/**
	 * Construct a new incrementable value from the original value and the increment value.
	 *
	 * @param value The value to increment
	 * @param increment The value to define the amount to add for each increment
	 */
	public IncrementableValue(Value value, Value increment) {
		this.value = value;
		this.increment = increment;
	}

	/**
	 * Calculated the original value.
	 */
	@Override
	public void calculate() {
		value.calculate();
	}

	/**
	 * Return the original value's real value plus the increment sum.
	 *
	 * @return The incremented value
	 */
	@Override
	public double getValue() {
		return value.getValue() + totalIncrement;
	}

	/**
	 * Calculates the increment value and add its real value to the increment sum.
	 */
	public void increment() {
		increment.calculate();
		totalIncrement += increment.getValue();
	}

	/**
	 * Sets the increment sum to 0. The real value of the original value is now unaltered.
	 */
	public void reset() {
		totalIncrement = 0;
	}

	/**
	 * Returns the string representation of the value.
	 *
	 * @return The string form of the value
	 */
	@Override
	public String toString() {
		return "IncrementableValue{value=" + value + ", increment=" + increment
				+ ", totalIncrement=" + totalIncrement + '}';
	}
}
