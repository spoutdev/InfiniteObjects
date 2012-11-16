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
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.spout.infobjects.condition.Condition;
import org.spout.infobjects.condition.Condition.ConditionMode;
import org.spout.infobjects.instruction.BlockInstruction;
import org.spout.infobjects.instruction.Instruction;
import org.spout.infobjects.instruction.PlaceInstruction;
import org.spout.infobjects.instruction.RepeatInstruction;
import org.spout.infobjects.material.MaterialSetter;
import org.spout.infobjects.shape.Shape;
import org.spout.infobjects.util.IWGOUtils;
import org.spout.infobjects.value.IncrementableValue;
import org.spout.infobjects.variable.Variable;
import org.spout.infobjects.value.ValueParser;
import org.spout.infobjects.variable.VariableSource;

public class IWGOManager {
	private final File folder;
	private final Map<String, IWGO> iwgos = new HashMap<String, IWGO>();

	public IWGOManager(String folderDir, boolean createFolder) {
		this(new File(folderDir), createFolder);
	}

	public IWGOManager(File folder, boolean createFolder) {
		if (!folder.exists()) {
			if (createFolder) {
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

	public void unloadIWGOs() {
		iwgos.clear();
	}

	public void reloadIWGOs() {
		unloadIWGOs();
		loadIWGOs();
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
		loadMaterialSetters(iwgo, config.getNode("setters"));
		loadConditions(iwgo, config.getNode("conditions"));
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

	private static void loadMaterialSetters(IWGO iwgo, ConfigurationNode settersNode) {
		for (String key : settersNode.getKeys(false)) {
			final ConfigurationNode setterNode = settersNode.getNode(key);
			final MaterialSetter setter =
					MaterialSetter.newMaterialSetter(setterNode.getNode("type").getString(), key);
			if (setter != null) {
				setter.configure(IWGOUtils.toStringMap(setterNode.getNode("properties")));
				iwgo.addMaterialSetter(setter);
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
				} else if (instruction instanceof BlockInstruction) {
					loadBlockInstruction((BlockInstruction) instruction, instructionNode);
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
				shape.setSize(ValueParser.parse(IWGOUtils.toStringMap(shapeNode.getNode("size")), iwgo, instruction));
				shape.setPosition(ValueParser.parse(IWGOUtils.toStringMap(shapeNode.getNode("position")), iwgo, instruction));
				shape.setMaterialSetter(iwgo.getMaterialSetter(shapeNode.getNode("material").getString()));
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

	private static void loadBlockInstruction(BlockInstruction instruction, ConfigurationNode blockNode) {
		final IWGO iwgo = instruction.getIWGO();
		final ConfigurationNode positionNode = blockNode.getNode("position");
		instruction.setX(ValueParser.parse(positionNode.getNode("x").getString(), iwgo, instruction));
		instruction.setY(ValueParser.parse(positionNode.getNode("y").getString(), iwgo, instruction));
		instruction.setZ(ValueParser.parse(positionNode.getNode("z").getString(), iwgo, instruction));
		instruction.setPicker(iwgo.getMaterialSetter(blockNode.getNode("material").getString()));
		instruction.setOuter(Boolean.parseBoolean(blockNode.getNode("outer").getString()));
	}

	private static void loadConditions(IWGO iwgo, ConfigurationNode conditionsNode) {
		for (String key : conditionsNode.getKeys(false)) {
			final ConfigurationNode conditionNode = conditionsNode.getNode(key);
			final Condition condition = Condition.newCondition(conditionNode.getNode("shape").getString(), iwgo);
			condition.setMode(ConditionMode.valueOf(conditionNode.getNode("mode").getString().toUpperCase()));
			condition.setSize(ValueParser.parse(IWGOUtils.toStringMap(conditionNode.getNode("size")), iwgo));
			condition.setPosition(ValueParser.parse(IWGOUtils.toStringMap(conditionNode.getNode("position")), iwgo));
			for (String name : conditionNode.getNode("check").getStringList()) {
				condition.addBlockMaterial((BlockMaterial) MaterialRegistry.get(name));
			}
			iwgo.addCondition(condition);
		}
	}
}
