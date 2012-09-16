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
package org.spout.infiniteobjects;

import de.congrace.exp4j.Calculable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.congrace.exp4j.CustomFunction;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.InvalidCustomFunctionException;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import java.util.HashSet;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.spout.infiniteobjects.function.RandomFloatFunction;
import org.spout.infiniteobjects.function.RandomIntFunction;
import org.spout.infiniteobjects.variable.Variable;

public class IFOManager {
	private static final List<CustomFunction> FUNCTIONS = new ArrayList<CustomFunction>();
	private static final Map<String, Double> CONSTANTS = new HashMap<String, Double>();
	private final File folder;
	private static final Map<String, IFOWorldGeneratorObject> ifowgos = new HashMap<String, IFOWorldGeneratorObject>();

	static {
		try {
			FUNCTIONS.add(new RandomIntFunction());
			FUNCTIONS.add(new RandomFloatFunction());
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
		buildVariables(ifowgo, config.getNode("variables"));
		buildLists(ifowgo, config.getNode("lists"));
		return ifowgo;
	}

	private void buildVariables(IFOWorldGeneratorObject ifowgo, ConfigurationNode variableNode) {
		final Set<String> variableNames = variableNode.getKeys(false);
		final Map<Variable, Set<String>> references = new HashMap<Variable, Set<String>>();
		for (String variableName : variableNames) {
			final String expression = variableNode.getNode(variableName).getString();
			final Set<String> referencedVariableNames = new HashSet<String>();
			for (String vn : variableNames) {
				if (expression.contains(vn)) {
					referencedVariableNames.add(vn);
				}
			}
			try {
				final Calculable rawValue = getExpressionBuilder(expression).
						withVariableNames(referencedVariableNames.toArray(new String[referencedVariableNames.size()])).
						build();
				final Variable variable = new Variable(ifowgo, variableName, rawValue);
				ifowgo.addVariable(variable);
				references.put(variable, referencedVariableNames);
			} catch (UnknownFunctionException ex) {
				Logger.getLogger(IFOManager.class.getName()).log(Level.SEVERE, null, ex);
			} catch (UnparsableExpressionException ex) {
				Logger.getLogger(IFOManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		for (Entry<Variable, Set<String>> entry : references.entrySet()) {
			entry.getKey().addReferences(ifowgo.getVariables(entry.getValue()));
		}
	}

	private void buildLists(IFOWorldGeneratorObject ifowgo, ConfigurationNode lists) {
	}

	public Collection<IFOWorldGeneratorObject> getIFOs() {
		return ifowgos.values();
	}

	public IFOWorldGeneratorObject getIFO(String name) {
		return ifowgos.get(name);
	}

	public static void addConstant(String name, double value) {
		CONSTANTS.put(name, value);
	}

	public static Map<String, Double> getConstantMap() {
		return CONSTANTS;
	}

	public static String replaceConstants(String expression) {
		for (Entry<String, Double> entry : CONSTANTS.entrySet()) {
			expression = expression.replaceAll("\\Q" + entry.getKey() + "\\E", entry.getValue().toString());
		}
		return expression;
	}

	public static ExpressionBuilder getExpressionBuilder(String expression) {
		return new ExpressionBuilder(replaceConstants(expression)).withCustomFunctions(FUNCTIONS);
	}
}
