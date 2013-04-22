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
package org.spout.infobjects.util;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * A reflection based factor to generate various subclasses of a main class. Each subclass is
 * associated to a type string representing it.
 *
 * @param <T> The main class and main type for this factory
 */
public class TypeFactory<T> {
	private final Map<String, Constructor<? extends T>> TYPES =
			new HashMap<String, Constructor<? extends T>>();
	private final Class<?>[] constructorParams;

	/**
	 * Constructs a new type factory from the argument types for the subclass' constructors.
	 *
	 * @param constructorParams The constructor argument types for the subclass' constructors
	 */
	public TypeFactory(Class<?>... constructorParams) {
		this.constructorParams = constructorParams;
	}

	/**
	 * Register a new type, with a string representing its name and the subclass for the type.
	 *
	 * @param name The name of the type
	 * @param type The subclass for the type
	 * @throws IllegalArgumentException if the subclass doesn't have the constructor which requires
	 * the types specified during construction of the factory
	 */
	public void register(String name, Class<? extends T> type) {
		try {
			TYPES.put(name, type.getConstructor(constructorParams));
		} catch (NoSuchMethodException ex) {
			throw new IllegalArgumentException("Type \"" + type + "\" doesn't have the required constructor");
		}
	}

	/**
	 * Creates a new instance of the subclass for the desired type, returning it as the main class
	 * type.
	 *
	 * @param type The type to create a new instance of
	 * @param constructorParams The parameters to pass to the subclass constructor
	 * @return A new subclass instance as the main class type
	 * @throws IllegalArgumentException if the type hasn't been registered
	 */
	public T newInstance(String type, Object... constructorParams) {
		if (!TYPES.containsKey(type)) {
			throw new IllegalArgumentException("Type \"" + type + "\" is not a registered type");
		}
		try {
			return TYPES.get(type).newInstance(constructorParams);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
