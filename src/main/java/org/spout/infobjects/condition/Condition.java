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
package org.spout.infobjects.condition;

import java.util.Random;

import org.spout.api.util.config.ConfigurationNode;

import org.spout.infobjects.IWGO;
import org.spout.infobjects.exception.ConditionLoadingException;
import org.spout.infobjects.util.ConfigurationLoadable;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.util.TypeFactory;

/**
 * An abstract condition. This class stores only the parent iWGO. Extend this class, implement
 * {@link #load(org.spout.api.util.config.ConfigurationNode)}, {@link #check()} and
 * {@link #setRandom(java.util.Random)} to create your own condition. For the loader to recognize it
 * it will also need to be registered with {@link #register(java.lang.String, java.lang.Class)}. It
 * is important to make sure the extending class has a constructor with the same arguments as this
 * class, as it is necessary for the creations of new conditions via reflection.
 */
public abstract class Condition implements ConfigurationLoadable, RandomOwner {
	private static final TypeFactory<Condition> CONDITIONS = new TypeFactory<Condition>(IWGO.class);
	private final IWGO iwgo;

	/**
	 * Constructs a new condition.
	 *
	 * @param iwgo The parent iWGO
	 */
	public Condition(IWGO iwgo) {
		this.iwgo = iwgo;
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
	 * Loads this condition from the properties node in the condition declaration.
	 *
	 * @param properties The properties node
	 * @throws ConditionLoadingException If the loading fail
	 */
	@Override
	public abstract void load(ConfigurationNode properties) throws ConditionLoadingException;

	/**
	 * An abstract method which is implemented by the extending class. Returns true if the condition
	 * check is successful, false it not.
	 *
	 * @return True if the check is successful, false if not.
	 */
	public abstract boolean check();

	/**
	 * Randomizes this condition.
	 */
	public abstract void randomize();

	/**
	 * Sets the random for this condition.
	 *
	 * @param random The random to set
	 */
	@Override
	public abstract void setRandom(Random random);

	/**
	 * Registers a new condition. This is necessary for the loader to recognize it when loading a
	 * new iWGO. This methods required the type, which used in the iWGO configurations. For example:
	 * "cuboid", "pyramid" or "cylinder". The type must be unique.
	 *
	 * @param type The type of the condition
	 * @param condition The class of the condition to register
	 */
	public static void register(String type, Class<? extends Condition> condition) {
		CONDITIONS.register(type, condition);
	}

	/**
	 * Creates a new condition via reflection. The type is the one used during the registration and
	 * the IWGO is the one to be passed to the constructor.
	 *
	 * @param type The type as registered
	 * @param iwgo The iWGO to pass to the constructor
	 * @return The new condition
	 */
	public static Condition newCondition(String type, IWGO iwgo) {
		return CONDITIONS.newInstance(type, iwgo);
	}
}
