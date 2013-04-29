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

import java.util.Collection;
import java.util.Map;

/**
 * Represents a source of variables. A variable source should give access to all of the variables it
 * contains, but adding and removing variables are optional operations, although adding is very
 * suggested.
 */
public interface VariableSource {
	/**
	 * Looks up a variable from its name.
	 *
	 * @param name The name of the variable to lookup
	 * @return The variable, if found
	 */
	public Variable getVariable(String name);

	/**
	 * Gets all the variables of this variable source as a collection.
	 *
	 * @return A collection containing all the variables of this variable source
	 */
	public Collection<Variable> getVariables();

	/**
	 * Returns all the variables of this variable source as a map, where the key is the name of the
	 * variable and the value, the variable itself.
	 *
	 * @return A map containing all the variables of this variable source as a string, variable map
	 */
	public Map<String, Variable> getVariableMap();

	/**
	 * Adds a variable to this variable source. This is an optional operation, but supporting it is
	 * very suggested.
	 *
	 * @param variable The variable to add
	 */
	public void addVariable(Variable variable);

	/**
	 * Checks if this variable source contains the specified variable, by looking up its name.
	 * Returns true if the variable has been found, false if not.
	 *
	 * @param name The name of the variable to look up
	 * @return True if the variable has been found, false if not
	 */
	public boolean hasVariable(String name);
}
