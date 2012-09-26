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

import de.congrace.exp4j.expression.Calculable;

public class NormalVariable implements Variable {
	private final String name;
	// The expression used to calculate the value
	private final Calculable rawValue;
	// A cached value
	private double value;
	// Referenced variables to obtain values for calculation
	private final Set<Variable> referenced = new HashSet<Variable>();

	public NormalVariable(String name, Calculable rawValue) {
		this.name = name;
		this.rawValue = rawValue;
	}

	@Override
	public void calculate() {
		for (Variable ref : referenced) {
			rawValue.setVariable(ref.getName(), ref.getValue());
		}
		value = rawValue.calculate();
	}

	public Calculable getRawValue() {
		return rawValue;
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public String getName() {
		return name;
	}

	public boolean hasReference(Variable variable) {
		return referenced.contains(variable);
	}

	public void addReference(Variable variable) {
		referenced.add(variable);
	}

	public void addReferences(Collection<Variable> variables) {
		referenced.addAll(variables);
	}

	public void removeReference(Variable variable) {
		referenced.remove(variable);
	}

	public void removeReferences(Collection<Variable> variables) {
		referenced.removeAll(variables);
	}

	@Override
	public Set<Variable> getReferences() {
		return referenced;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final NormalVariable other = (NormalVariable) obj;
		if ((name == null) ? (other.name != null) : !name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return name;
	}
}
