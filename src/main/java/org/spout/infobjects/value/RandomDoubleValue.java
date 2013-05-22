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
 * Represents a random double value. This is an optimizations to replace {@link MathExpressionValue}
 * with a faster one when the value only has one random function.
 */
public class RandomDoubleValue extends RandomValue {
	private final double min;
	private final double max;
	private double value;

	/**
	 * Constructs a new random double value from the minimum and maximum of the random number range.
	 *
	 * @param min The minimum possible random real value
	 * @param max The maximum possible random real value
	 */
	public RandomDoubleValue(double min, double max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Constructs a new random double value from the expression. Note that this expression has a
	 * different format than usual mathematical expression. It must follow this format: {@code ranF=[min]-[max]},
	 * where the bracketed words are the parameters as real values.
	 *
	 * @param expression The random double value expression
	 */
	public RandomDoubleValue(String expression) {
		final String[] minMax = expression.split("=")[1].split("-");
		min = Double.parseDouble(minMax[0]);
		max = Double.parseDouble(minMax[1]);
	}

	/**
	 * Gets the real value for this random double value.
	 *
	 * @return The real value
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * Calculates the next random double value between the specified minimum and maximum.
	 */
	@Override
	public void calculate() {
		value = IWGOUtils.nextDouble(random, min, max);
	}

	/**
	 * Returns the string representation of the value.
	 *
	 * @return The string form of the value
	 */
	@Override
	public String toString() {
		return "RandomDoubleValue{" + "value=" + value + '}';
	}
}
