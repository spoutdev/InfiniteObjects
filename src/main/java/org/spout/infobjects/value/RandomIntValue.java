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

import org.spout.infobjects.util.IWGOUtils;

/**
 * Represents a random integer value. This is an optimizations to replace {@link MathExpressionValue}
 * with a faster one when the value only has one random function.
 */
public class RandomIntValue extends RandomValue {
	private final int min;
	private final int max;
	private int value;

	/**
	 * Constructs a new random integer value from the minimum and maximum of the random number
	 * range.
	 *
	 * @param min The minimum possible random integer value
	 * @param max The maximum possible random integer value
	 */
	public RandomIntValue(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Constructs a new random integer value from the expression. Note that this expression has a
	 * different format than usual mathematical expression. It must follow this format: {@code ranI=[min]-[max]},
	 * where the bracketed words are the parameters as integer values.
	 *
	 * @param expression The random double value expression
	 */
	public RandomIntValue(String expression) {
		final String[] minMax = expression.split("=")[1].split("-");
		min = Integer.parseInt(minMax[0]);
		max = Integer.parseInt(minMax[1]);
	}

	/**
	 * Gets the real value for this random integer value.
	 *
	 * @return The real value
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * Calculates the next random integer value between the specified minimum and maximum.
	 */
	@Override
	public void calculate() {
		value = IWGOUtils.nextInt(random, min, max);
	}

	/**
	 * Returns the string representation of the value.
	 *
	 * @return The string form of the value
	 */
	@Override
	public String toString() {
		return "RandomIntValue{" + "value=" + value + '}';
	}
}
