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
 * Represents a numerical real (double) value. This value might be constant, random or calculated
 * from a mathematical expression. It might also depend on other values as variables,
 */
public interface Value {
	/**
	 * Calculates the value. This will not alter constant values. It is designed for randomizing
	 * random values or updating values that depend on mutable variables. To fetch the calculated
	 * value, use {@link #getValue()}.
	 */
	public void calculate();

	/**
	 * Gets the real (double) value of this value. If the value can be altered by calling {@link #calculate()},
	 * then the returned value is the result of the last {@code calculate()} call.
	 *
	 * @return The real value of this value.
	 */
	public double getValue();
}
