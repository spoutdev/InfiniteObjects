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
import java.util.HashSet;
import java.util.Set;

import de.congrace.exp4j.Calculable;

import org.spout.infobjects.IFOWorldGeneratorObject;

public class VariableList {
	private final IFOWorldGeneratorObject owner;
	private final String name;
	// The expression used to calculate the value
	private final Calculable rawValue;
	private final NormalVariable size;
	// The cached values
	protected double[] values;
	// Referenced variables and variables to obtain values for calculation
	private final Set<Variable> referencedVariables = new HashSet<Variable>();
	private final Set<VariableList> referencedLists = new HashSet<VariableList>();

	public VariableList(IFOWorldGeneratorObject owner, String name, Calculable rawValue, NormalVariable size) {
		this.owner = owner;
		this.name = name;
		this.rawValue = rawValue;
		this.size = size;
	}

	public void addVariableReference(Variable variable) {
		referencedVariables.add(variable);
	}

	public void addVariableReferences(Collection<Variable> variables) {
		referencedVariables.addAll(variables);
	}

	public void addListReference(VariableList variable) {
		referencedLists.add(variable);
	}

	public void addListReferences(Collection<VariableList> variables) {
		referencedLists.addAll(variables);
	}

	public Set<VariableList> getReferencedLists() {
		return referencedLists;
	}

	public Set<Variable> getReferencedVariables() {
		return referencedVariables;
	}

	public void calculate() {
		size.calculate();
		values = new double[(int) size.getValue()];
		for (Variable ref : referencedVariables) {
			rawValue.setVariable(ref.getName(), ref.getValue());
		}
		for (int i = 0; i < values.length; i++) {
			for (VariableList ref : referencedLists) {
				rawValue.setVariable(ref.getName(), ref.getValue(i));
			}
			values[i] = rawValue.calculate();
		}
	}

	public double getValue(int index) {
		return values[index];
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return values.length;
	}
}
