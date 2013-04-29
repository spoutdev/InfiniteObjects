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

import java.util.Random;

import de.congrace.exp4j.exception.UnknownFunctionException;
import de.congrace.exp4j.exception.UnparsableExpressionException;
import de.congrace.exp4j.expression.Calculable;
import de.congrace.exp4j.expression.ExpressionBuilder;

import org.spout.infobjects.function.RandomDoubleFunction;
import org.spout.infobjects.function.RandomIntFunction;
import org.spout.infobjects.util.RandomOwner;

/**
 * Represents a value defined by a random mathematical expression. Unlike using {@link DoubleValue}
 * with the {@link Double#Double(java.lang.String)} constructor, this class conserves the
 * mathematical expression as a {@link de.congrace.exp4j.expression.Calculable} and recalculates it
 * for each {@link #calculate()} call. This is ideal for expression with random functions, and this
 * class has been designed for such use.
 */
public class MathExpressionValue implements Value, RandomOwner {
	private final RandomIntFunction randomIntFunction = new RandomIntFunction();
	private final RandomDoubleFunction randomFloatFunction = new RandomDoubleFunction();
	protected final Calculable calculable;
	private double value;

	/**
	 * Constructs a new math expression value from the expression string.
	 *
	 * @param expression The expression for this value
	 * @throws UnknownFunctionException If the expression has one or more undeclared function
	 * @throws UnparsableExpressionException If the expression cannot be parsed
	 */
	public MathExpressionValue(String expression)
			throws UnknownFunctionException, UnparsableExpressionException {
		this(new ExpressionBuilder(expression));
	}

	/**
	 * Constructs a new math expression value from the expression builder. Use this constructor if
	 * you whish to add any unregistered custom function and set values for any variables.
	 *
	 * @param expressionBuilder The expression builder for this value
	 * @throws UnknownFunctionException If the expression has one or more undeclared function
	 * @throws UnparsableExpressionException If the expression cannot be parsed
	 */
	public MathExpressionValue(ExpressionBuilder expressionBuilder)
			throws UnknownFunctionException, UnparsableExpressionException {
		calculable = expressionBuilder.withCustomFunctions(randomIntFunction, randomFloatFunction).build();
	}

	/**
	 * Constructs a new math expression value from the calculable form of the expression.
	 *
	 * @param calculable The calculable form of the expression
	 */
	public MathExpressionValue(Calculable calculable) {
		this.calculable = calculable;
	}

	/**
	 * Gets the real value of the mathematical expression.
	 *
	 * @return The real value
	 */
	@Override
	public double getValue() {
		return value;
	}

	/**
	 * Reevaluates the math expression.
	 */
	@Override
	public void calculate() {
		value = calculable.calculate();
	}

	/**
	 * Sets the randoms for the {@link org.spout.infobjects.function.RandomIntFunction} and
	 * {@link org.spout.infobjects.function.RandomDoubleFunction} functions if any are present in
	 * the expression.
	 *
	 * @param random
	 */
	@Override
	public void setRandom(Random random) {
		randomIntFunction.setRandom(random);
		randomFloatFunction.setRandom(random);
	}

	/**
	 * Returns the string representation of the value.
	 *
	 * @return The string form of the value
	 */
	@Override
	public String toString() {
		return "MathExpressionValue{" + "value=" + value + '}';
	}
}
