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
package org.spout.infobjects.function;

import java.util.Random;

import de.congrace.exp4j.function.Function;
import de.congrace.exp4j.function.Functions;

import org.spout.infobjects.util.RandomOwner;

/**
 * An abstract random function. This class provides the random instance and implementation of the {@link org.spout.infobjects.util.RandomOwner}
 * interface for the extending class.
 */
public abstract class RandomFunction extends Function implements RandomOwner {
	protected Random random = new Random();

	/**
	 * Constructs a new random function from the name of the function and it's argument count.
	 *
	 * @param name The name of the function
	 * @param argumentCount The argument count
	 */
	public RandomFunction(String name, int argumentCount) {
		super(name, argumentCount);
	}

	/**
	 * Constructs a new function from it's name. The argument count will de defaulted to 1.
	 *
	 * @param name The name of the function
	 */
	public RandomFunction(String name) {
		super(name);
	}

	/**
	 * Sets the random for this random function.
	 *
	 * @param random The random to set
	 */
	@Override
	public void setRandom(Random random) {
		this.random = random;
	}
}
