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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.CustomFunction;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.InvalidCustomFunctionException;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.spout.infobjects.function.RandomFloatFunction;
import org.spout.infobjects.function.RandomIntFunction;
import org.spout.infobjects.list.IncrementedList;
import org.spout.infobjects.list.NormalList;
import org.spout.infobjects.material.MaterialPicker;
import org.spout.infobjects.material.MaterialPickers;
import org.spout.infobjects.util.IFOUtils;
import org.spout.infobjects.variable.NormalVariable;
import org.spout.infobjects.variable.Variable;

public class IFOManager {
	private static final Map<String, CustomFunction> FUNCTIONS = new HashMap<String, CustomFunction>();
	private static final TObjectDoubleMap<String> CONSTANTS = new TObjectDoubleHashMap<String>();
	private final File folder;
	private static final Map<String, IFOWorldGeneratorObject> ifowgos = new HashMap<String, IFOWorldGeneratorObject>();

	static {
		try {
			FUNCTIONS.put("randomInt", new RandomIntFunction());
			FUNCTIONS.put("randomFloat", new RandomFloatFunction());
		} catch (InvalidCustomFunctionException ex) {
			Logger.getLogger(IFOManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		CONSTANTS.put("PI", Math.PI);
		CONSTANTS.put("E", Math.E);
	}

	public IFOManager(File folder) {
		this.folder = folder;
	}

	public void loadIFOs() {
		if (!folder.exists()) {
			throw new IllegalStateException("Folder does not exist.");
		}
		if (!folder.isDirectory()) {
			throw new IllegalStateException("Folder is not a directory.");
		}
		for (File assemblyFile : folder.listFiles()) {
			if (assemblyFile.isHidden()) {
				continue;
			}
			final YamlConfiguration config = new YamlConfiguration(assemblyFile);
			try {
				config.load();
			} catch (ConfigurationException ex) {
				Logger.getLogger(IFOManager.class.getName()).log(Level.SEVERE, null, ex);
			}
			final IFOWorldGeneratorObject ifowgo = buildIFO(config);
			ifowgos.put(ifowgo.getName(), ifowgo);
		}
	}

	private IFOWorldGeneratorObject buildIFO(YamlConfiguration config) {
		final IFOWorldGeneratorObject ifowgo = new IFOWorldGeneratorObject(config.getNode("name").getString());
		buildInitVariables(ifowgo, config.getNode("variables"));
		ifowgo.calculateVariables();
		buildLists(ifowgo, config.getNode("lists"));
		ifowgo.calculateLists();
		buildPickers(ifowgo, config.getNode("pickers"));
		return ifowgo;
	}

	private void buildInitVariables(IFOWorldGeneratorObject ifowgo, ConfigurationNode variablesNode) {
		final Set<String> variableNames = variablesNode.getKeys(false);
		final Map<NormalVariable, Set<String>> references = new HashMap<NormalVariable, Set<String>>();
		for (String variableName : variableNames) {
			final String expression = variablesNode.getNode(variableName).getString();
			final Set<String> referencedVariableNames = new HashSet<String>();
			for (String vn : variableNames) {
				if (IFOUtils.hasMatch("\\b\\Q" + vn + "\\E\\b", expression)) {
					referencedVariableNames.add(vn);
				}
			}
			try {
				final Calculable rawValue = getExpressionBuilder(expression).
						withVariableNames(referencedVariableNames.toArray(new String[referencedVariableNames.size()])).
						build();
				final NormalVariable variable = new NormalVariable(variableName, rawValue);
				ifowgo.addVariable(variable);
				references.put(variable, referencedVariableNames);
			} catch (Exception ex) {
				Logger.getLogger(IFOManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		for (Entry<NormalVariable, Set<String>> entry : references.entrySet()) {
			entry.getKey().addReferences(ifowgo.getVariables(entry.getValue()));
		}
	}

	private NormalVariable buildVariable(IFOWorldGeneratorObject ifowgo, String name, String expression) {
		final Set<Variable> referencedVariables = new HashSet<Variable>();
		for (Variable variable : ifowgo.getVariables()) {
			if (IFOUtils.hasMatch("\\b\\Q" + variable.getName() + "\\E\\b", expression)) {
				referencedVariables.add(variable);
			}
		}
		final ExpressionBuilder builder = getExpressionBuilder(expression);
		for (Variable variable : ifowgo.getVariables()) {
			builder.withVariableNames(variable.getName());
		}
		try {
			final Calculable rawValue = builder.build();
			final NormalVariable variable = new NormalVariable(name, rawValue);
			variable.addReferences(referencedVariables);
			return variable;
		} catch (Exception ex) {
			Logger.getLogger(IFOManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private void buildLists(IFOWorldGeneratorObject ifowgo, ConfigurationNode listsNode) {
		final Set<String> listNames = listsNode.getKeys(false);
		final Map<NormalList, Set<String>> references = new HashMap<NormalList, Set<String>>();
		for (String listName : listNames) {
			final ConfigurationNode listNode = listsNode.getNode(listName);
			final NormalVariable size = buildVariable(ifowgo, "size", listNode.getNode("size").getString());
			final String incrementExpression = listNode.getNode("increment").getString();
			final NormalVariable increment;
			if (incrementExpression != null) {
				increment = buildVariable(ifowgo, "increment", incrementExpression);
			} else {
				increment = null;
			}
			final String expression = listNode.getNode("value").getString();
			final Set<Variable> referencedVariables = new HashSet<Variable>();
			for (Variable variable : ifowgo.getVariables()) {
				final String variableRegex = "\\b\\Q" + variable.getName() + "\\E\\b";
				if (IFOUtils.hasMatch(variableRegex, expression)) {
					referencedVariables.add(variable);
				}
			}
			final Set<String> referencedListNames = new HashSet<String>();
			for (String ln : listNames) {
				if (IFOUtils.hasMatch("\\b\\Q" + ln + "\\E\\b", expression)) {
					referencedListNames.add(ln);
				}
			}
			try {
				final ExpressionBuilder builder = getExpressionBuilder(expression);
				for (Variable referencedVariable : referencedVariables) {
					builder.withVariableNames(referencedVariable.getName());
				}
				builder.withVariableNames(referencedListNames.toArray(new String[referencedListNames.size()]));
				final Calculable rawValue = builder.build();
				final NormalList list;
				if (increment == null) {
					list = new NormalList(listName, rawValue, size);
				} else {
					list = new IncrementedList(listName, rawValue, size, increment);
				}
				ifowgo.addList(listName, list);
				list.addVariableReferences(referencedVariables);
				references.put(list, referencedListNames);
			} catch (Exception ex) {
				Logger.getLogger(IFOManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		for (Entry<NormalList, Set<String>> entry : references.entrySet()) {
			entry.getKey().addListReferences(ifowgo.getLists(entry.getValue()));
		}
	}

	private void buildPickers(IFOWorldGeneratorObject ifowgo, ConfigurationNode pickers) {
		for (String pickerKey : pickers.getKeys(false)) {
			final ConfigurationNode pickerNode = pickers.getNode(pickerKey);
			final MaterialPicker picker = MaterialPickers.get(pickerKey, pickerNode.getNode("type").getString());
			picker.load(pickerNode.getNode("properties"));
			ifowgo.addPicker(picker);
		}
	}

	public Collection<IFOWorldGeneratorObject> getIFOs() {
		return ifowgos.values();
	}

	public IFOWorldGeneratorObject getIFO(String name) {
		return ifowgos.get(name);
	}

	public static void addConstant(String name, double value) {
		CONSTANTS.put(name.toUpperCase(), value);
	}

	public static void addFunction(String name, CustomFunction function) {
		FUNCTIONS.put(name, function);
	}

	public static TObjectDoubleMap<String> getConstants() {
		return CONSTANTS;
	}

	public static Map<String, CustomFunction> getFunctions() {
		return FUNCTIONS;
	}

	private static String replaceConstants(String expression) {
		final TObjectDoubleIterator<String> iterator = CONSTANTS.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			expression = expression.replaceAll("\\Q" + iterator.key() + "\\E", Double.toString(iterator.value()));
		}
		return expression;
	}

	public static ExpressionBuilder getExpressionBuilder(String expression) {
		final Set<CustomFunction> usedFunctions = new HashSet<CustomFunction>();
		for (Entry<String, CustomFunction> entry : FUNCTIONS.entrySet()) {
			if (IFOUtils.hasMatch("\\b\\Q" + entry.getKey() + "\\E\\b", expression)) {
				usedFunctions.add(entry.getValue());
			}
		}
		return new ExpressionBuilder(replaceConstants(expression)).withCustomFunctions(usedFunctions);
	}
}
