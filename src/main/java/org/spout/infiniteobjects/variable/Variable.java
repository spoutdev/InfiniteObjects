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
package org.spout.infiniteobjects.variable;

import java.util.ArrayList;
import java.util.List;

import de.congrace.exp4j.Calculable;
import java.util.Collection;

import org.spout.infiniteobjects.IFOWorldGeneratorObject;

public class Variable {
	private final IFOWorldGeneratorObject owner;
	private final String name;
	// the expression used to calculateValue the value
	private final Calculable rawValue;
	// a cached value
	private double value;
	// referenced variables to obtain values for calculation
	private final List<Variable> referenced = new ArrayList<Variable>();

	public Variable(IFOWorldGeneratorObject owner, String name, Calculable rawValue) {
		this.owner = owner;
		this.name = name;
		this.rawValue = rawValue;
	}

	public void calculateValue() {
		for (Variable ref : referenced) {
			rawValue.setVariable(ref.getName(), ref.getValue());
		}
		value = rawValue.calculate();
	}

	public double getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public void addReference(Variable variable) {
		referenced.add(variable);
	}
	
	public void addReferences(Collection<Variable> variables) {
		referenced.addAll(variables);
	}

	public List<Variable> getReferencedVariables() {
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
		final Variable other = (Variable) obj;
		if (owner != other.owner && (owner == null || !owner.equals(other.owner))) {
			return false;
		}
		if ((name == null) ? (other.name != null) : !name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 97 * hash + (this.owner != null ? this.owner.hashCode() : 0);
		hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}
}
