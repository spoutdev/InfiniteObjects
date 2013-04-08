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
package org.spout.infobjects.function;

import de.congrace.exp4j.function.Functions;

import org.spout.infobjects.util.IWGOUtils;

/**
 * A function which returns a random double between the min (inclusive) and the max (exclusive).
 */
public class RandomDoubleFunction extends RandomFunction {
	static {
		Functions.register(new RandomDoubleFunction());
	}

	/**
	 * Constructs a new random double function. The name is "ranF" and the argument count is 2.
	 */
	public RandomDoubleFunction() {
		super("ranF", 2);
	}

	/**
	 * Applies the function to the arguments.
	 *
	 * @param arguments The arguments for the function
	 * @return A random double between the min (inclusive) and the max (exclusive)
	 */
	@Override
	public double applyFunction(double... arguments) {
		return IWGOUtils.nextDouble(random, arguments[0], arguments[1]);
	}
}
