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

public class ValueParser {
	private static final String RANDOM_INT_VALUE_REGEX = "ranI\\=.*";
	private static final String RANDOM_DOUBLE_VALUE_REGEX = "ranF\\=.*";
	private static final String RANDOM_MATH_EXP_VALUE_REGEX = ".*ran[IF]\\(.*";
	private static final String VARIABLE_MATH_EXP_VALUE_REGEX = ".*[a-zA-Z_][.&&[^\\(]]*";

	public static Value parse(String exp) {
		exp = exp.trim();
		if (exp.matches(RANDOM_INT_VALUE_REGEX)) {
			return new RandomIntValue(exp);
		} else if (exp.matches(RANDOM_DOUBLE_VALUE_REGEX)) {
			return new RandomDoubleValue(exp);
		} else if (exp.matches(RANDOM_MATH_EXP_VALUE_REGEX)) {
			if (exp.matches(VARIABLE_MATH_EXP_VALUE_REGEX)) {
				try {
					return new VariableMathExpressionValue(exp);
				} catch (Exception ex) {
					return null;
				}
			}
			try {
				return new MathExpressionValue(exp);
			} catch (Exception ex) {
				return null;
			}
		}
		try {
			return new DoubleValue(Double.parseDouble(exp));
		} catch (NumberFormatException nfe) {
			try {
				return new DoubleValue(exp);
			} catch (Exception ex) {
				return null;
			}
		}
	}
}
