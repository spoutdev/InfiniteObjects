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

import de.congrace.exp4j.exception.UnknownFunctionException;
import de.congrace.exp4j.exception.UnparsableExpressionException;
import de.congrace.exp4j.expression.Calculable;
import de.congrace.exp4j.expression.ExpressionBuilder;

/**
 * Represent a constant double value.
 */
public class DoubleValue implements Value {
	private final double value;

	/**
	 * Construct a new double value from it's real double value.
	 *
	 * @param value The real value
	 */
	public DoubleValue(double value) {
		this.value = value;
	}

	/**
	 * Construct a new double value from a mathematical expression. This constructor will evaluate
	 * the expression and use the resulting value as the double value.
	 *
	 * @param expression The expression to evaluate
	 * @throws UnknownFunctionException If the expression has one or more undeclared function
	 * @throws UnparsableExpressionException If the expression cannot be parsed
	 */
	public DoubleValue(String expression) throws UnknownFunctionException, UnparsableExpressionException {
		this(new ExpressionBuilder(expression));
	}

	/**
	 * Constructs a new double value from the expression builder. Use this constructor if you whish
	 * to add any unregistered custom function and set values for any variables.
	 *
	 * @param expressionBuilder The expression builder for this value
	 * @throws UnknownFunctionException If the expression has one or more undeclared function
	 * @throws UnparsableExpressionException If the expression cannot be parsed
	 */
	public DoubleValue(ExpressionBuilder expressionBuilder) throws UnknownFunctionException, UnparsableExpressionException {
		this(expressionBuilder.build());
	}

	/**
	 * Constructs a double value from the real value of calculable form of an expression.
	 *
	 * @param calculable The calculable form of the expression
	 */
	public DoubleValue(Calculable calculable) {
		value = calculable.calculate();
	}

	/**
	 * Gets the real value of this value. The returned double is always the same as this is a
	 * constant value.
	 *
	 * @return The real value of this value
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * Does nothing as this value is constant.
	 */
	@Override
	public void calculate() {
	}

	/**
	 * Returns the string representation of the value.
	 *
	 * @return The string form of the value
	 */
	@Override
	public String toString() {
		return "DoubleValue{" + "value=" + value + '}';
	}
}
