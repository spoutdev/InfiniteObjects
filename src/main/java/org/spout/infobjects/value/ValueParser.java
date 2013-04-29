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
package org.spout.infobjects.value;

import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.congrace.exp4j.constant.Constants;
import de.congrace.exp4j.function.Functions;

import org.spout.infobjects.variable.VariableSource;
import org.spout.infobjects.exception.ValueParsingException;

public class ValueParser {
	private static final String RANDOM_INT_VALUE_REGEX = "ranI\\=.*";
	private static final String RANDOM_DOUBLE_VALUE_REGEX = "ranF\\=.*";
	private static final String RANDOM_MATH_EXP_VALUE_REGEX = ".*ran[IF]\\(.*";

	static {
		// Load all included resources so they can register themselves
		try {
			Class.forName("org.spout.infobjects.function.RandomIntFunction");
			Class.forName("org.spout.infobjects.function.RandomDoubleFunction");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ValueParser.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Attempts to parse a string into {@link Value}. An exception is thrown if this fails. This
	 * method may return any of the following: {@link DoubleValue}, {@link MathExpressionValue},
	 * {@link RandomDoubleValue}, {@link RandomIntValue} or {@link VariableMathExpressionValue}.
	 * {@link org.spout.infobjects.variable.VariableSource}s may be passed to this method for
	 * parsing {@link VariableMathExpressionValue}s.
	 *
	 * @param expression The expression to attempt to parse
	 * @param sources Optional variable sources for parsing variable math expression values
	 * @return The parsed value
	 * @throws ValueParsingException If the value parsing fails
	 */
	public static Value parse(String expression, VariableSource... sources) {
		if (expression == null || expression.trim().equals("")) {
			throw new ValueParsingException("Value can not be null or empty");
		}
		expression = expression.trim();
		if (expression.matches(RANDOM_INT_VALUE_REGEX)) {
			return new RandomIntValue(expression);
		} else if (expression.matches(RANDOM_DOUBLE_VALUE_REGEX)) {
			return new RandomDoubleValue(expression);
		} else if (hasVariable(expression)) {
			try {
				final VariableMathExpressionValue varMathExpValue = new VariableMathExpressionValue(expression);
				varMathExpValue.addVariableSources(sources);
				return varMathExpValue;
			} catch (Exception ex) {
				throw new ValueParsingException(expression, ex);
			}
		} else if (expression.matches(RANDOM_MATH_EXP_VALUE_REGEX)) {
			try {
				return new MathExpressionValue(expression);
			} catch (Exception ex) {
				throw new ValueParsingException(expression, ex);
			}
		}
		try {
			return new DoubleValue(expression);
		} catch (Exception ex) {
			throw new ValueParsingException(expression, ex);
		}
	}

	/**
	 * Attempts to parse all of the expressions in the list.
	 *
	 * @param expressions The list of expression to attempt to parse
	 * @param sources Optional variable sources for parsing variable math expression values
	 * @return The parsed values as a list
	 * @throws ValueParsingException If the value parsing fails for any expression
	 * @see #parse(java.lang.String, org.spout.infobjects.variable.VariableSource[])
	 */
	public static List<Value> parse(List<String> expressions, VariableSource... sources) {
		final List<Value> values = new ArrayList<Value>();
		for (String expression : expressions) {
			values.add(parse(expression, sources));
		}
		return values;
	}

	/**
	 * Attempts to parse all of the expressions in the set.
	 *
	 * @param expressions The set of expression to attempt to parse
	 * @param sources Optional variable sources for parsing variable math expression values
	 * @return The parsed values as a set
	 * @throws ValueParsingException If the value parsing fails for any expression
	 * @see #parse(java.lang.String, org.spout.infobjects.variable.VariableSource[])
	 */
	public static Set<Value> parse(Set<String> expressions, VariableSource... sources) {
		final Set<Value> values = new HashSet<Value>();
		for (String expression : expressions) {
			values.add(parse(expression, sources));
		}
		return values;
	}

	/**
	 * Attempts to parse all of the expressions values in the map. This parses the values only, the
	 * keys will not be changed.
	 *
	 * @param expressions The map of expression to attempt to parse
	 * @param sources Optional variable sources for parsing variable math expression values
	 * @return The parsed values as a map with the same mapping
	 * @throws ValueParsingException If the value parsing fails for any expression
	 * @see #parse(java.lang.String, org.spout.infobjects.variable.VariableSource[])
	 */
	public static Map<String, Value> parse(Map<String, String> expressions, VariableSource... sources) {
		final Map<String, Value> values = new HashMap<String, Value>();
		for (Map.Entry<String, String> entry : expressions.entrySet()) {
			values.put(entry.getKey(), parse(entry.getValue(), sources));
		}
		return values;
	}

	private static boolean hasVariable(String expression) {
		final Matcher matcher =
				VariableMathExpressionValue.VARIABLE_PATTERN.matcher(expression);
		while (matcher.find()) {
			final String find = matcher.group();
			if (!Functions.isFunction(find)
					&& !Constants.isConstant(find)) {
				return true;
			}
		}
		return false;
	}
}
