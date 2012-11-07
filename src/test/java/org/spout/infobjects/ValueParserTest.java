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

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.spout.infobjects.util.IWGOUtils;
import org.spout.infobjects.value.DoubleValue;
import org.spout.infobjects.value.MathExpressionValue;
import org.spout.infobjects.value.RandomDoubleValue;
import org.spout.infobjects.value.RandomIntValue;
import org.spout.infobjects.value.ValueParser;
import org.spout.infobjects.value.VariableMathExpressionValue;
import org.spout.infobjects.variable.SimpleVariableSource;
import org.spout.infobjects.variable.Variable;

public class ValueParserTest {
	private static final Random VALUE_RANDOM = new Random();
	// just a double to parse
	private static final String DOUBLE_VALUE = "42";
	private static final double EXPECTED_DOUBLE = 42;
	// a math expression to evaluate
	private static final String DOUBLE_EXP_VALUE = "sin(3/4 * PI) * 10 + 2";
	private static final double EXPECTED_DOUBLE_EXP = 9.071067690849304;
	// a random int function
	private static final String RANDOM_INT_VALUE = "ranI=3-10";
	private static final double EXPECTED_RANDOM_INT;
	// a random float function
	private static final String RANDOM_FLOAT_VALUE = "ranF=18.9-36.4";
	private static final double EXPECTED_RANDOM_FLOAT;
	// a random math expression to evaluate
	private static final String RANDOM_MATH_EXP_VALUE = "3 * ranF(3, 54) + 10";
	private static final double EXPECTED_RANDOM_MATH_EXP;
	// a variable expression to evaluate
	private static final String VARIABLE_MATH_EXP_VALUE = "ranI(0, 5) + double * PI";
	private static final double EXPECTED_VARIABLE_MATH_EXP;

	static {
		final long seed = new Random().nextLong();
		VALUE_RANDOM.setSeed(seed);
		final Random expectedRandom = new Random(seed);
		EXPECTED_RANDOM_INT = IWGOUtils.nextInt(expectedRandom, 3, 10);
		EXPECTED_RANDOM_FLOAT = IWGOUtils.nextDouble(expectedRandom, 18.9, 36.4);
		EXPECTED_RANDOM_MATH_EXP = 3 * IWGOUtils.nextDouble(expectedRandom, 3, 54) + 10;
		EXPECTED_VARIABLE_MATH_EXP = IWGOUtils.nextInt(expectedRandom, 0, 5) + EXPECTED_DOUBLE * Math.PI;
	}

	@Before
	public void before() throws ClassNotFoundException {
		Class.forName("org.spout.infobjects.function.RandomFunction");
	}

	@Test
	public void test() {
		final DoubleValue doubleValue = (DoubleValue) ValueParser.parse(DOUBLE_VALUE);
		Assert.assertEquals(doubleValue.getValue(), EXPECTED_DOUBLE, 0);

		final DoubleValue doubleExpValue = (DoubleValue) ValueParser.parse(DOUBLE_EXP_VALUE);
		Assert.assertEquals(doubleExpValue.getValue(), EXPECTED_DOUBLE_EXP, 0);

		final RandomIntValue randomIntValue = (RandomIntValue) ValueParser.parse(RANDOM_INT_VALUE);
		randomIntValue.setRandom(VALUE_RANDOM);
		randomIntValue.calculate();
		Assert.assertEquals(randomIntValue.getValue(), EXPECTED_RANDOM_INT, 0);

		final RandomDoubleValue randomFloatValue = (RandomDoubleValue) ValueParser.parse(RANDOM_FLOAT_VALUE);
		randomFloatValue.setRandom(VALUE_RANDOM);
		randomFloatValue.calculate();
		Assert.assertEquals(randomFloatValue.getValue(), EXPECTED_RANDOM_FLOAT, 0);

		final MathExpressionValue mathExpValue = (MathExpressionValue) ValueParser.parse(RANDOM_MATH_EXP_VALUE);
		mathExpValue.setRandom(VALUE_RANDOM);
		mathExpValue.calculate();
		Assert.assertEquals(mathExpValue.getValue(), EXPECTED_RANDOM_MATH_EXP, 0);

		final VariableMathExpressionValue mathVarExpValue = (VariableMathExpressionValue) ValueParser.parse(VARIABLE_MATH_EXP_VALUE);
		final SimpleVariableSource source = new SimpleVariableSource(new Variable("double", doubleValue));
		mathVarExpValue.setVariableSource(source);
		mathVarExpValue.setRandom(VALUE_RANDOM);
		mathVarExpValue.calculate();
		Assert.assertEquals(mathVarExpValue.getValue(), EXPECTED_VARIABLE_MATH_EXP, 0);
	}
}
