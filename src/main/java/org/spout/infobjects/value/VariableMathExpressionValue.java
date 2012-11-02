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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.congrace.exp4j.constant.Constants;
import de.congrace.exp4j.exception.UnknownFunctionException;
import de.congrace.exp4j.exception.UnparsableExpressionException;
import de.congrace.exp4j.expression.Calculable;
import de.congrace.exp4j.expression.ExpressionBuilder;
import de.congrace.exp4j.function.Functions;

import org.spout.infobject.variable.VariableSource;

public class VariableMathExpressionValue extends MathExpressionValue {
	private static final Pattern VARIABLE_REGEX = Pattern.compile("\\b[a-zA-Z_]\\w*\\b");
	private VariableSource variableSource;

	public VariableMathExpressionValue(Calculable value) {
		super(value);
	}

	public VariableMathExpressionValue(String expression)
			throws UnknownFunctionException, UnparsableExpressionException {
		super(new ExpressionBuilder(expression).withVariableNames(findVariables(expression)).build());
	}

	@Override
	public void calculate() {
		if (variableSource == null) {
			//throw new IllegalStateException("No variable source set");
			return;
		}
		for (String variableName : calculable.getVariableNames()) {
			calculable.setVariable(variableName, variableSource.getVariable(variableName).getValue());
		}
		super.calculate();
	}

	public void setVariableSource(VariableSource source) {
		this.variableSource = source;
	}

	private static List<String> findVariables(String expression) {
		final List<String> matches = new ArrayList<String>();
		final Matcher matcher = VARIABLE_REGEX.matcher(expression);
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
