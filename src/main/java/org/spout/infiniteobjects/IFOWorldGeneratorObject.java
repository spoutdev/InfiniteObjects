/*
 * This file is part of SpoutAPI.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * SpoutAPI is licensed under the SpoutDev License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
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
package org.spout.infiniteobjects;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;

import org.spout.infiniteobjects.variable.Variable;

public class IFOWorldGeneratorObject extends WorldGeneratorObject {
	private final String name;
	private final Map<String, Variable> variables = new HashMap<String, Variable>();

	public IFOWorldGeneratorObject(String name) {
		this.name = name;
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getName() {
		return name;
	}

	public boolean hasVariable(String name) {
		return variables.containsKey(name);
	}

	public Variable getVariable(String name) {
		return variables.get(name);
	}

	public Map<String, Variable> getVariableMap() {
		return variables;
	}

	public Collection<Variable> getVariables() {
		return variables.values();
	}

	public Set<Variable> getVariables(Collection<String> varNames) {
		final Set<Variable> vars = new HashSet<Variable>();
		for (String varName : varNames) {
			final Variable variable = variables.get(varName);
			if (variable != null) {
				vars.add(variable);
			}
		}
		return vars;
	}

	public void addVariable(Variable variable) {
		variables.put(variable.getName(), variable);
	}
}
