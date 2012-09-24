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
package org.spout.infobjects.list;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.congrace.exp4j.Calculable;

import org.spout.infobjects.variable.Variable;

public class NormalList implements IFOList {
	private final String name;
	// The expression used to calculate the value
	private final Calculable rawValue;
	private final Variable size;
	// The cached values
	protected double[] values;
	// Referenced variables and variables to obtain values for calculation
	private final Set<Variable> referencedVariables = new HashSet<Variable>();
	private final Set<IFOList> referencedLists = new HashSet<IFOList>();

	public NormalList(String name, Calculable rawValue, Variable size) {
		this.name = name;
		this.rawValue = rawValue;
		this.size = size;
	}

	public boolean hasVariableReference(Variable variable) {
		return referencedVariables.contains(variable);
	}

	public void addVariableReference(Variable variable) {
		referencedVariables.add(variable);
	}

	public void addVariableReferences(Collection<Variable> variables) {
		referencedVariables.addAll(variables);
	}

	public void removeVariableReference(Variable variable) {
		referencedVariables.remove(variable);
	}

	public void removeVariableReferences(Collection<Variable> variables) {
		referencedVariables.removeAll(variables);
	}

	public boolean hasListReference(IFOList list) {
		return referencedLists.contains(list);
	}

	public void addListReference(IFOList list) {
		referencedLists.add(list);
	}

	public void addListReferences(Collection<IFOList> lists) {
		referencedLists.addAll(lists);
	}

	public void removeListReference(IFOList list) {
		referencedLists.remove(list);
	}

	public void removeListReferences(Collection<IFOList> lists) {
		referencedLists.removeAll(lists);
	}

	@Override
	public Set<IFOList> getReferencedLists() {
		return referencedLists;
	}

	public Set<Variable> getReferencedVariables() {
		return referencedVariables;
	}

	@Override
	public void calculate() {
		size.calculate();
		values = new double[(int) size.getValue()];
		for (Variable ref : referencedVariables) {
			rawValue.setVariable(ref.getName(), ref.getValue());
		}
		for (int i = 0; i < values.length; i++) {
			for (IFOList ref : referencedLists) {
				rawValue.setVariable(ref.getName(), ref.getValue(i));
			}
			values[i] = rawValue.calculate();
		}
	}

	@Override
	public double getValue(int index) {
		return values[index];
	}

	public Calculable getRawValue() {
		return rawValue;
	}

	public double[] getValues() {
		return values;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getSize() {
		return values.length;
	}

	public Variable getSizeVariable() {
		return size;
	}
}
