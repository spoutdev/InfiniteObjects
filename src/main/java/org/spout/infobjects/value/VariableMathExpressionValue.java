/*
 * This file is part of InfiniteObjects.
 *
 * Copyright (c) 2012 Spout LLC <http://www.spout.org/>
 * InfiniteObjects is licensed under the Spout License Version 1.
 *
 * InfiniteObjects is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * InfiniteObjects is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license,
 * including the MIT license.
 */
package org.spout.infobjects.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.congrace.exp4j.constant.Constants;
import de.congrace.exp4j.exception.UnknownFunctionException;
import de.congrace.exp4j.exception.UnparsableExpressionException;
import de.congrace.exp4j.expression.Calculable;
import de.congrace.exp4j.expression.ExpressionBuilder;
import de.congrace.exp4j.function.Functions;

import org.spout.infobjects.variable.VariableSource;

/**
 * A variable math expression value. This is an extension of {@link MathExpressionValue} which adds
 * support for variables on top of random functions. To provide values for the variables, {@link org.spout.infobjects.variable.VariableSource}s
 * must be added using {@link #addVariableSources(org.spout.infobjects.variable.VariableSource[])}.
 * When reevaluating the expression, this value will fetch the variable values from these sources.
 * If a variable cannot be found, the value will be zero.
 */
public class VariableMathExpressionValue extends MathExpressionValue {
	protected static final Pattern VARIABLE_PATTERN = Pattern.compile("[a-zA-Z_]\\w*");
	private final Set<VariableSource> variableSources = new HashSet<VariableSource>();

	/**
	 * Constructs a new variable math value from the expression. This constructor will find the
	 * variables and declare them on its own.
	 *
	 * @param expression The expression for this value
	 * @throws UnknownFunctionException If the expression has one or more undeclared function
	 * @throws UnparsableExpressionException If the expression cannot be parsed
	 */
	public VariableMathExpressionValue(String expression)
			throws UnknownFunctionException, UnparsableExpressionException {
		this(new ExpressionBuilder(expression));
	}

	/**
	 * Constructs a new variable math value from the expression. This constructor will find the
	 * variables and declare them on its own.
	 *
	 * @param expressionBuilder The expression builder for this value
	 * @throws UnknownFunctionException If the expression has one or more undeclared function
	 * @throws UnparsableExpressionException If the expression cannot be parsed
	 */
	public VariableMathExpressionValue(ExpressionBuilder expressionBuilder)
			throws UnknownFunctionException, UnparsableExpressionException {
		super(expressionBuilder.withVariableNames(findVariables(expressionBuilder.getExpression())));
	}

	/**
	 * Constructs a new variable math expression value from the calculable form of the expression.
	 *
	 * @param calculable The calculable form of the expression
	 */
	public VariableMathExpressionValue(Calculable calculable) {
		super(calculable);
	}

	/**
	 * Reevaluates the math expression, updating the values of the variable from the variable
	 * sources.
	 *
	 * @throws IllegalStateException If no variable sources have been added
	 */
	@Override
	public void calculate() {
		if (variableSources.isEmpty()) {
			throw new IllegalStateException("No variable sources");
		}
		for (String variableName : calculable.getVariableNames()) {
			for (VariableSource source : variableSources) {
				if (source.hasVariable(variableName)) {
					calculable.setVariable(variableName, source.getVariable(variableName).getValue());
					break;
				}
			}
		}
		super.calculate();
	}

	/**
	 * Adds the variable sources to this variable math expression.
	 *
	 * @param sources The variable sources to add
	 */
	public void addVariableSources(VariableSource... sources) {
		addVariableSources(Arrays.asList(sources));
	}

	/**
	 * Adds the variable sources to this variable math expression.
	 *
	 * @param sources The variable sources to add as a collection
	 */
	public void addVariableSources(Collection<VariableSource> sources) {
		variableSources.addAll(sources);
	}

	/**
	 * Returns the string representation of the value.
	 *
	 * @return The string form of the value
	 */
	@Override
	public String toString() {
		return "VariableMathExpressionValue{" + "value=" + getValue() + '}';
	}

	private static List<String> findVariables(String expression) {
		final List<String> matches = new ArrayList<String>();
		final Matcher matcher = VARIABLE_PATTERN.matcher(expression);
		while (matcher.find()) {
			final String match = matcher.group();
			if (!Functions.isFunction(match)
					&& !Constants.isConstant(match)) {
				matches.add(match);
			}
		}
		return matches;
	}
}
