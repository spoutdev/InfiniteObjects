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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleVariableSource implements VariableSource {
	private final Map<String, Variable> variables = new LinkedHashMap<String, Variable>();

	public SimpleVariableSource(Variable... variables) {
		addVariables(variables);
	}

	public SimpleVariableSource(Collection<Variable> variables) {
		addVariables(variables);
	}

	@Override
	public void addVariable(Variable variable) {
		variables.put(variable.getName(), variable);
	}

	public final void addVariables(Variable... variables) {
		for (Variable variable : variables) {
			addVariable(variable);
		}
	}

	public final void addVariables(Collection<Variable> variables) {
		for (Variable variable : variables) {
			addVariable(variable);
		}
	}

	@Override
	public Variable getVariable(String name) {
		return variables.get(name);
	}

	@Override
	public Collection<Variable> getVariables() {
		return variables.values();
	}

	@Override
	public Map<String, Variable> getVariableMap() {
		return Collections.unmodifiableMap(variables);
	}

	@Override
	public boolean hasVariable(String name) {
		return variables.containsKey(name);
	}
}
