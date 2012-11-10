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

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.spout.infobjects.instruction.Instruction;
import org.spout.infobjects.instruction.PlaceInstruction;
import org.spout.infobjects.instruction.RepeatInstruction;
import org.spout.infobjects.material.MaterialPicker;
import org.spout.infobjects.shape.Shape;
import org.spout.infobjects.util.IWGOUtils;
import org.spout.infobjects.value.IncrementableValue;
import org.spout.infobjects.variable.Variable;
import org.spout.infobjects.value.ValueParser;
import org.spout.infobjects.variable.VariableSource;

public class IWGOManager {
	private final File folder;
	private final Map<String, IWGO> iwgos = new HashMap<String, IWGO>();

	public IWGOManager(String folderDir, boolean create) {
		this(new File(folderDir), create);
	}

	public IWGOManager(File folder, boolean create) {
		if (!folder.exists()) {
			if (create) {
				folder.mkdirs();
			} else {
				throw new IllegalArgumentException("Folder does not exist.");
			}
		}
		if (!folder.isDirectory()) {
			throw new IllegalArgumentException("Folder is not a directory.");
		}
		this.folder = folder;
	}

	public void loadIWGOs() {
		for (File file : folder.listFiles()) {
			if (file.isHidden()) {
				continue;
			}
			final YamlConfiguration config = new YamlConfiguration(file);
			try {
				config.load();
			} catch (ConfigurationException ex) {
				Logger.getLogger(IWGOManager.class.getName()).log(Level.SEVERE, null, ex);
			}
			final IWGO iwgo = buildIWGO(config);
			iwgos.put(iwgo.getName(), iwgo);
		}
	}

	public IWGO getIWGO(String name) {
		return iwgos.get(name);
	}

	public Collection<IWGO> getIWGOs() {
		return Collections.unmodifiableCollection(iwgos.values());
	}

	public Map<String, IWGO> getIWGOMap() {
		return Collections.unmodifiableMap(iwgos);
	}

	private static IWGO buildIWGO(Configuration config) {
		final IWGO iwgo = new IWGO(config.getNode("name").getString());
		loadVariables(iwgo, config.getNode("variables"), iwgo);
		loadMaterialPickers(iwgo, config.getNode("materials"));
		loadInstructions(iwgo, config.getNode("instructions"));
		iwgo.randomize();
		return iwgo;
	}

	private static void loadVariables(VariableSource destination, ConfigurationNode variableNode,
			VariableSource... sources) {
		for (String key : variableNode.getKeys(false)) {
			destination.addVariable(new Variable(key, ValueParser.parse(variableNode.getNode(key).getString(), sources)));
		}
	}

	private static void loadMaterialPickers(IWGO iwgo, ConfigurationNode materialsNode) {
		for (String key : materialsNode.getKeys(false)) {
			final ConfigurationNode pickerNode = materialsNode.getNode(key);
			final MaterialPicker picker =
					MaterialPicker.newPicker(pickerNode.getNode("type").getString(), key);
			if (picker != null) {
				picker.configure(IWGOUtils.toStringMap(pickerNode.getNode("properties")));
				iwgo.addMaterialPicker(picker);
			}
		}
	}

	private static void loadInstructions(IWGO iwgo, ConfigurationNode instructionsNode) {
		for (String key : instructionsNode.getKeys(false)) {
			final ConfigurationNode instructionNode = instructionsNode.getNode(key);
			final Instruction instruction =
					Instruction.newInstruction(instructionNode.getNode("type").getString(), iwgo, key);
			if (instruction != null) {
				loadVariables(instruction, instructionNode.getNode("variables"), iwgo, instruction);
				if (instruction instanceof PlaceInstruction) {
					loadPlaceInstruction((PlaceInstruction) instruction, instructionNode);
				} else if (instruction instanceof RepeatInstruction) {
					loadRepeatInstruction((RepeatInstruction) instruction, instructionNode);
				}
				iwgo.addInstruction(instruction);
			}
		}
	}

	private static void loadPlaceInstruction(PlaceInstruction instruction, ConfigurationNode instructionNode) {
		final IWGO iwgo = instruction.getIWGO();
		ConfigurationNode shapesNode = instructionNode.getNode("shapes");
		for (String key : shapesNode.getKeys(false)) {
			final ConfigurationNode shapeNode = shapesNode.getNode(key);
			final Shape shape = Shape.newShape(shapeNode.getNode("type").getString(), iwgo);
			if (shape != null) {
				shape.configure(ValueParser.parse(IWGOUtils.toStringMap(shapeNode.getNode("properties")), iwgo, instruction));
				shape.setPosition(ValueParser.parse(IWGOUtils.toStringMap(shapeNode.getNode("position")), iwgo, instruction));
				shape.setMaterialPicker(iwgo.getMaterialPicker(shapeNode.getNode("material").getString()));
				instruction.addShape(shape);
			}
		}
	}

	private static void loadRepeatInstruction(RepeatInstruction instruction, ConfigurationNode repeatNode) {
		final IWGO iwgo = instruction.getIWGO();
		instruction.setRepeat(iwgo.getInstruction(repeatNode.getNode("repeat").getString()));
		instruction.setTimes(ValueParser.parse(repeatNode.getNode("times").getString(), iwgo, instruction));
		final ConfigurationNode incrementNode = repeatNode.getNode("increment");
		for (String key : incrementNode.getKeys(false)) {
			instruction.addIncrementableValue(key, new IncrementableValue(iwgo.getVariable(key).getRawValue(),
					ValueParser.parse(incrementNode.getNode(key).getString(), iwgo, instruction)));
		}
	}
}
