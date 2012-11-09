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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.IntVector3;
import org.spout.api.util.Named;

import org.spout.infobjects.instruction.Instruction;
import org.spout.infobjects.material.MaterialPicker;
import org.spout.infobjects.util.IWGOUtils;
import org.spout.infobjects.value.CalculableValue;
import org.spout.infobjects.value.Value;
import org.spout.infobjects.value.VariableMathExpressionValue;
import org.spout.infobjects.variable.Variable;
import org.spout.infobjects.variable.VariableSource;

public class IWGO extends WorldGeneratorObject implements VariableSource, Named {
	private final String name;
	private World world;
	private final IntVector3 position = new IntVector3(0, 0, 0);
	private final Map<String, Variable> variables = new LinkedHashMap<String, Variable>();
	private final Map<String, MaterialPicker> pickers = new HashMap<String, MaterialPicker>();
	private final Map<String, Instruction> instructions = new HashMap<String, Instruction>();

	public IWGO(String name) {
		this.name = name;
	}

	public void setPosition(Point position) {
		world = position.getWorld();
		this.position.set(position.getBlockX(), position.getBlockY(), position.getBlockZ());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void randomize() {
		calculateVariables();
		for (Instruction instruction : instructions.values()) {
			instruction.calculateVariables();
		}
	}

	private void calculateVariables() {
		IWGOUtils.calculateVariables(variables.values());
	}

	public void setMaterial(int xx, int yy, int zz, BlockMaterial material) {
		setMaterial(xx, yy, zz, material, material.getData());
	}

	public void setMaterial(int xx, int yy, int zz, BlockMaterial material, short data) {
		world.getBlock(transform(xx, yy, zz)).setMaterial(material, data);
	}

	private Point transform(int xx, int yy, int zz) {
		return new Point(world, position.getX() + xx, position.getY() + yy, position.getZ() + zz);
	}

	@Override
	public void addVariable(Variable variable) {
		final Value value = variable.getRawValue();
		if (value instanceof VariableMathExpressionValue) {
			((VariableMathExpressionValue) value).addVariableSources(this);
		}
		variables.put(variable.getName(), variable);
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

	public void addMaterialPicker(MaterialPicker picker) {
		pickers.put(picker.getName(), picker);
	}

	public Collection<MaterialPicker> getMaterialPickers() {
		return pickers.values();
	}

	public Map<String, MaterialPicker> getMaterialPickerMap() {
		return Collections.unmodifiableMap(pickers);
	}

	public void addInstruction(Instruction instruction) {
		instructions.put(instruction.getName(), instruction);
	}

	public Collection<Instruction> getInstructions() {
		return instructions.values();
	}

	public Map<String, Instruction> getInstructionMap() {
		return Collections.unmodifiableMap(instructions);
	}
}
