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
package org.spout.infiniteobjects.variable;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.spout.infiniteobjects.IFOManager;
import org.spout.infiniteobjects.IFOWorldGeneratorObject;
import org.spout.infiniteobjects.util.IFOUtils;

public class VariableBuilder {
	private final IFOWorldGeneratorObject owner;
	private final String name;
	private final String expression;
	private final List<String> referencedVariableNames = new ArrayList<String>();
	private Variable build;

	public VariableBuilder(IFOWorldGeneratorObject owner, String name, String expression) {
		this.owner = owner;
		this.name = name;
		this.expression = IFOManager.replaceConstants(expression);
		referencedVariableNames.addAll(IFOUtils.findVariables(this.expression));
	}

	public void build() {
		final ExpressionBuilder builder = IFOManager.getExpressionBuilder(expression);
		builder.withVariableNames(referencedVariableNames.toArray(new String[referencedVariableNames.size()]));
		try {
			Calculable calculable = builder.build();
			build = new Variable(owner, name, calculable);
		} catch (UnknownFunctionException ex) {
			Logger.getLogger(VariableBuilder.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnparsableExpressionException ex) {
			Logger.getLogger(VariableBuilder.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Variable getBuild() {
		return build;
	}

	public List<String> getReferencedVariableNames() {
		return referencedVariableNames;
	}
}
