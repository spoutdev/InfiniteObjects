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
package org.spout.infobjects;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

import org.spout.infobjects.list.IFOList;
import org.spout.infobjects.list.IncrementedList;
import org.spout.infobjects.list.NormalList;
import org.spout.infobjects.list.StaticList;
import org.spout.infobjects.material.MaterialPicker;
import org.spout.infobjects.util.IFOUtils;
import org.spout.infobjects.variable.NormalVariable;
import org.spout.infobjects.variable.StaticVariable;
import org.spout.infobjects.variable.Variable;

public class IFOWorldGeneratorObject extends WorldGeneratorObject {
	private final String name;
	private final Map<String, Variable> variables = new HashMap<String, Variable>();
	private final Map<String, IFOList> lists = new HashMap<String, IFOList>();
	private final Map<String, MaterialPicker> pickers = new HashMap<String, MaterialPicker>();

	public IFOWorldGeneratorObject(String name) {
		this.name = name;
	}

	public void randomize() {
		calculateVariables();
		calculateLists();
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setMaterial(BlockMaterial material, int xx, int yy, int zz) {
	}

	public String getName() {
		return name;
	}

	public Variable getVariable(String name) {
		return variables.get(name);
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

	public Collection<Variable> getVariables() {
		return variables.values();
	}

	public void addVariable(Variable variable) {
		variables.put(variable.getName(), variable);
	}

	public void calculateVariables() {
		final Set<Variable> calculated = new HashSet<Variable>();
		while (calculated.size() < variables.size()) {
			for (Variable variable : variables.values()) {
				if (variable instanceof StaticVariable) {
					calculated.add(variable);
					continue;
				}
				if (!calculated.contains(variable)
						&& calculated.containsAll(variable.getReferences())) {
					variable.calculate();
					calculated.add(variable);
				}
			}
		}
	}

	public IFOList getList(String name) {
		return lists.get(name);
	}

	public Set<IFOList> getLists(Collection<String> listNames) {
		final Set<IFOList> variableLists = new HashSet<IFOList>();
		for (String listName : listNames) {
			final IFOList list = lists.get(listName);
			if (list != null) {
				variableLists.add(list);
			}
		}
		return variableLists;
	}

	public Collection<IFOList> getLists() {
		return lists.values();
	}

	public void addList(IFOList list) {
		lists.put(list.getName(), list);
	}

	public void calculateLists() {
		final Set<IFOList> calculated = new HashSet<IFOList>();
		while (calculated.size() < lists.size()) {
			for (IFOList list : lists.values()) {
				if (list instanceof StaticList) {
					calculated.add(list);
					continue;
				}
				if (!calculated.contains(list)
						&& calculated.containsAll(list.getReferencedLists())) {
					list.calculate();
					calculated.add(list);
				}
			}
		}
	}

	public void addPicker(MaterialPicker picker) {
		pickers.put(picker.getName(), picker);
	}

	public MaterialPicker getPicker(String name) {
		return pickers.get(name.toLowerCase());
	}

	public Collection<MaterialPicker> getPickers() {
		return pickers.values();
	}

	public void optimizeVariables() {
		boolean hadChanges = true;
		while (hadChanges) {
			final Set<StaticVariable> optimizedVariables = new HashSet<StaticVariable>();
			final Set<NormalVariable> discardedVariables = new HashSet<NormalVariable>();
			for (Variable variable : variables.values()) {
				if (!(variable instanceof NormalVariable)) {
					continue;
				}
				final NormalVariable normalVariable = (NormalVariable) variable;
				if (!canOptimize(normalVariable)) {
					continue;
				}
				final StaticVariable staticVariable =
						new StaticVariable(normalVariable.getName(), normalVariable.getValue());
				optimizedVariables.add(staticVariable);
				discardedVariables.add(normalVariable);
				replaceVariable(normalVariable, staticVariable);
			}
			for (NormalVariable discardedVariable : discardedVariables) {
				variables.remove(discardedVariable.getName());
			}
			for (StaticVariable optimizedVariable : optimizedVariables) {
				addVariable(optimizedVariable);
			}
			hadChanges = !optimizedVariables.isEmpty();
		}
	}

	public void optimizeLists() {
		boolean hadChanges = true;
		while (hadChanges) {
			final Set<StaticList> optimizedLists = new HashSet<StaticList>();
			final Set<NormalList> discardedLists = new HashSet<NormalList>();
			for (IFOList list : lists.values()) {
				if (!(list instanceof NormalList)) {
					continue;
				}
				final NormalList normalList = (NormalList) list;
				if (!canOptimize(normalList)) {
					continue;
				}
				final StaticList staticList =
						new StaticList(normalList.getName(), normalList.getValues());
				optimizedLists.add(staticList);
				discardedLists.add(normalList);
				replaceList(normalList, staticList);
			}
			for (NormalList discardedList : discardedLists) {
				lists.remove(discardedList.getName());
			}
			for (StaticList optimizedList : optimizedLists) {
				addList(optimizedList);
			}
			hadChanges = !optimizedLists.isEmpty();
		}
	}

	private void replaceVariable(Variable oldVariable, Variable newVariable) {
		for (Variable variable : variables.values()) {
			if (!(variable instanceof NormalVariable)) {
				continue;
			}
			final NormalVariable normalVariable = (NormalVariable) variable;
			if (normalVariable.hasReference(oldVariable)) {
				normalVariable.removeReference(oldVariable);
				normalVariable.addReference(newVariable);
			}
		}
	}

	private void replaceList(IFOList oldList, IFOList newList) {
		for (IFOList list : lists.values()) {
			if (!(list instanceof NormalList)) {
				continue;
			}
			final NormalList normalList = (NormalList) list;
			if (normalList.hasListReference(oldList)) {
				normalList.removeListReference(oldList);
				normalList.addListReference(newList);
			}
		}
	}

	private boolean canOptimize(NormalVariable variable) {
		return IFOUtils.containsOnly(variable.getReferences(), StaticVariable.class)
				&& !IFOUtils.isRandom(variable.getRawValue().getExpression());
	}

	private boolean canOptimize(NormalList list) {
		if (list instanceof IncrementedList) {
			if (!(((IncrementedList) list).getIncrement() instanceof StaticVariable)) {
				return false;
			}
		}
		return IFOUtils.containsOnly(list.getReferencedLists(), StaticList.class)
				&& IFOUtils.containsOnly(list.getReferencedVariables(), StaticVariable.class)
				&& !IFOUtils.isRandom(list.getRawValue().getExpression())
				&& list.getSizeVariable() instanceof StaticVariable;
	}
}
