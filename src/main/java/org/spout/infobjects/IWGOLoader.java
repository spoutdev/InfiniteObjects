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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.spout.api.Spout;
import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.spout.infobjects.condition.Condition;
import org.spout.infobjects.exception.ConditionLoadingException;
import org.spout.infobjects.exception.IWGOLoadingException;
import org.spout.infobjects.exception.InstructionLoadingException;
import org.spout.infobjects.exception.MaterialSetterLoadingException;
import org.spout.infobjects.exception.ShapeLoadingException;
import org.spout.infobjects.exception.VariableLoadingException;
import org.spout.infobjects.instruction.BlockInstruction;
import org.spout.infobjects.instruction.Instruction;
import org.spout.infobjects.instruction.ShapeInstruction;
import org.spout.infobjects.instruction.RepeatInstruction;
import org.spout.infobjects.material.MaterialSetter;
import org.spout.infobjects.shape.Shape;
import org.spout.infobjects.util.IWGOUtils;
import org.spout.infobjects.value.IncrementableValue;
import org.spout.infobjects.value.ValueParser;
import org.spout.infobjects.variable.Variable;
import org.spout.infobjects.variable.VariableSource;

/**
 * A static class for loading iWGO from files and configurations.
 */
public class IWGOLoader {
	private IWGOLoader() {
	}

	/**
	 * Attempts to load an iWGO from a file. This file must be a YAML configuration.
	 *
	 * @param file The YAML configuration file
	 * @return The loaded and ready to use iWGO
	 * @throws IWGOLoadingException If loading of the iWGO fails
	 */
	public static IWGO loadIWGO(File file) throws IWGOLoadingException {
		return loadIWGO(new YamlConfiguration(file), file.getPath());
	}

	/**
	 * Attempts to load an iWGO from a configuration.
	 *
	 * @param config The configuration
	 * @return The loaded and ready to use iWGO
	 * @throws IWGOLoadingException If loading of the iWGO fails
	 */
	public static IWGO loadIWGO(Configuration config) throws IWGOLoadingException {
		return loadIWGO(config, null);
	}

	/**
	 * Attempts to load an iWGO from a configuration. This method accepts the source of the
	 * configuration for better debugging of iWGO loading failures. This source maybe a file path,
	 * plugin name or any other string representation of the source of the configuration.
	 *
	 * @param config The configuration
	 * @param source The source of the configuration. May be null
	 * @return The loaded and ready to use iWGO
	 * @throws IWGOLoadingException If loading of the iWGO fails
	 */
	public static IWGO loadIWGO(Configuration config, String source) throws IWGOLoadingException {
		try {
			config.load();
			final IWGO iwgo = new IWGO(config.getNode("name").getString());
			loadVariables(iwgo, config.getNode("variables"), iwgo);
			loadMaterialSetters(iwgo, config.getNode("setters"));
			loadConditions(iwgo, config.getNode("conditions"));
			loadInstructions(iwgo, config.getNode("instructions"));
			iwgo.randomize();
			return iwgo;
		} catch (Exception ex) {
			throw new IWGOLoadingException(source != null ? source : "unknown source", ex);
		}
	}

	/**
	 * Loads the variables into the source.
	 *
	 * @param destination The source to load to
	 * @param variableNode The configuration node with the variable info
	 * @param sources The variable sources for the new variables (as these might depend on other
	 * already loaded variables)
	 * @throws VariableLoadingException If variable loading fails
	 */
	public static void loadVariables(VariableSource destination, ConfigurationNode variableNode,
			VariableSource... sources) throws VariableLoadingException {
		for (String key : variableNode.getKeys(false)) {
			try {
				destination.addVariable(new Variable(key, ValueParser.parse(variableNode.getNode(key).getString(), sources)));
			} catch (Exception ex) {
				throw new VariableLoadingException(key, ex);
			}
		}
	}

	/**
	 * Loads the material setter to the iWGO.
	 *
	 * @param iwgo The iWGO to load to
	 * @param settersNode The configuration node with the material setter info
	 * @throws MaterialSetterLoadingException If material setter loading fails
	 */
	public static void loadMaterialSetters(IWGO iwgo, ConfigurationNode settersNode)
			throws MaterialSetterLoadingException {
		for (String key : settersNode.getKeys(false)) {
			try {
				final ConfigurationNode setterNode = settersNode.getNode(key);
				final MaterialSetter setter =
						MaterialSetter.newMaterialSetter(setterNode.getNode("type").getString(), key);
				setter.configure(IWGOUtils.toStringMap(setterNode.getNode("properties")));
				iwgo.addMaterialSetter(setter);
			} catch (Exception ex) {
				throw new MaterialSetterLoadingException(key, ex);
			}
		}
	}

	/**
	 * Loads the instructions to the iWGO.
	 *
	 * @param iwgo The iWGO to load to
	 * @param instructionsNode The configuration node with the instruction info
	 * @throws InstructionLoadingException If instruction loading fails
	 */
	public static void loadInstructions(IWGO iwgo, ConfigurationNode instructionsNode)
			throws InstructionLoadingException {
		for (String key : instructionsNode.getKeys(false)) {
			try {
				final ConfigurationNode instructionNode = instructionsNode.getNode(key);
				final Instruction instruction =
						Instruction.newInstruction(instructionNode.getNode("type").getString(), iwgo, key);
				loadVariables(instruction, instructionNode.getNode("variables"), iwgo, instruction);
				if (instruction instanceof ShapeInstruction) {
					loadShapeInstruction((ShapeInstruction) instruction, instructionNode);
				} else if (instruction instanceof RepeatInstruction) {
					loadRepeatInstruction((RepeatInstruction) instruction, instructionNode);
				} else if (instruction instanceof BlockInstruction) {
					loadBlockInstruction((BlockInstruction) instruction, instructionNode);
				}
				iwgo.addInstruction(instruction);
			} catch (Exception ex) {
				throw new InstructionLoadingException(key, ex);
			}
		}
	}

	private static void loadShapeInstruction(ShapeInstruction instruction, ConfigurationNode placeNode)
			throws ShapeLoadingException {
		final IWGO iwgo = instruction.getIWGO();
		final ConfigurationNode shapesNode = placeNode.getNode("shapes");
		for (String key : shapesNode.getKeys(false)) {
			try {
				final ConfigurationNode shapeNode = shapesNode.getNode(key);
				final Shape shape = Shape.newShape(shapeNode.getNode("type").getString(), iwgo);
				shape.setSize(ValueParser.parse(IWGOUtils.toStringMap(shapeNode.getNode("size")), iwgo, instruction));
				shape.setPosition(ValueParser.parse(IWGOUtils.toStringMap(shapeNode.getNode("position")), iwgo, instruction));
				final MaterialSetter setter = iwgo.getMaterialSetter(shapeNode.getNode("material").getString());
				if (setter == null) {
					throw new ShapeLoadingException("Material setter \"" + shapeNode.getNode("material").getString()
							+ "\" does not exist");
				}
				shape.setMaterialSetter(setter);
				instruction.addShape(shape);
			} catch (Exception ex) {
				throw new ShapeLoadingException(key, ex);
			}
		}
	}

	private static void loadRepeatInstruction(RepeatInstruction instruction, ConfigurationNode repeatNode)
			throws InstructionLoadingException {
		final IWGO iwgo = instruction.getIWGO();
		final Instruction repeat = iwgo.getInstruction(repeatNode.getNode("repeat").getString());
		if (repeat == null) {
			throw new InstructionLoadingException("Repeat instruction \""
					+ repeatNode.getNode("repeat").getString() + "\" does not exist");
		}
		instruction.setRepeat(repeat);
		instruction.setTimes(ValueParser.parse(repeatNode.getNode("times").getString(), iwgo, instruction));
		final ConfigurationNode incrementNode = repeatNode.getNode("increment");
		for (String key : incrementNode.getKeys(false)) {
			final Variable increment = iwgo.getVariable(key);
			if (increment == null) {
				throw new InstructionLoadingException("Increment variable \"" + key + "\" does not exist");
			}
			instruction.addIncrementableValue(key, new IncrementableValue(increment.getRawValue(),
					ValueParser.parse(incrementNode.getNode(key).getString(), iwgo, instruction)));
		}
	}

	private static void loadBlockInstruction(BlockInstruction instruction, ConfigurationNode blockNode)
			throws InstructionLoadingException {
		final IWGO iwgo = instruction.getIWGO();
		final ConfigurationNode positionNode = blockNode.getNode("position");
		instruction.setX(ValueParser.parse(positionNode.getNode("x").getString(), iwgo, instruction));
		instruction.setY(ValueParser.parse(positionNode.getNode("y").getString(), iwgo, instruction));
		instruction.setZ(ValueParser.parse(positionNode.getNode("z").getString(), iwgo, instruction));
		final MaterialSetter setter = iwgo.getMaterialSetter(blockNode.getNode("material").getString());
		if (setter == null) {
			throw new InstructionLoadingException("Material setter \"" + blockNode.getNode("material").getString()
					+ "\" does not exist");
		}
		instruction.setMaterialSetter(setter);
		instruction.setOuter(Boolean.parseBoolean(blockNode.getNode("outer").getString()));
	}

	/**
	 * Loads the conditions to the iWGO.
	 *
	 * @param iwgo The iWGO to load to
	 * @param conditionsNode The configuration node with the condition info
	 * @throws ConditionLoadingException If condition loading fails
	 */
	public static void loadConditions(IWGO iwgo, ConfigurationNode conditionsNode)
			throws ConditionLoadingException {
		for (String key : conditionsNode.getKeys(false)) {
			try {
				final ConfigurationNode conditionNode = conditionsNode.getNode(key);
				final Condition condition = Condition.newCondition(conditionNode.getNode("shape").getString(), iwgo);
				condition.setMode(Condition.ConditionMode.valueOf(conditionNode.getNode("mode").getString().toUpperCase()));
				condition.setSize(ValueParser.parse(IWGOUtils.toStringMap(conditionNode.getNode("size")), iwgo));
				condition.setPosition(ValueParser.parse(IWGOUtils.toStringMap(conditionNode.getNode("position")), iwgo));
				for (String name : conditionNode.getNode("check").getStringList()) {
					condition.addBlockMaterial(IWGOUtils.tryGetBlockMaterial(name));
				}
				iwgo.addCondition(condition);
			} catch (Exception ex) {
				throw new ConditionLoadingException(key, ex);
			}
		}
	}

	/**
	 * Logs iWGO loading exceptions to the Spout default logger (as defined by {@link org.spout.api.Spout#getLogger()})
	 * in a user friendly manner.
	 *
	 * @param exception The exception to log
	 */
	public static void logIWGOLoadingException(IWGOLoadingException exception) {
		logIWGOLoadingException(Spout.getLogger(), exception);
	}

	/**
	 * Logs iWGO loading exceptions to the specified logger in a user friendly manner.
	 *
	 * @param logger The logger to log to
	 * @param exception The exception to log
	 */
	public static void logIWGOLoadingException(Logger logger, IWGOLoadingException exception) {
		logger.log(Level.WARNING, "------------------------");
		logger.log(Level.WARNING, "| IWGO LOADING FAILURE |");
		logger.log(Level.WARNING, "------------------------");
		Throwable cause = exception;
		int tabCount = 0;
		do {
			String tabs = "";
			for (int i = 0; i < tabCount; i++) {
				tabs += "\t";
			}
			final String message = cause.getMessage();
			if (message != null) {
				logger.log(Level.WARNING, tabs + cause.getMessage());
			} else {
				logger.log(Level.WARNING, tabs + cause.getClass().getSimpleName());
			}
			tabCount++;
		} while ((cause = cause.getCause()) != null);
	}
}
