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
package org.spout.infobjects.instruction;

import java.util.Random;
import org.spout.infobjects.IWGO;
import org.spout.infobjects.material.MaterialPicker;
import org.spout.infobjects.util.RandomOwner;
import org.spout.infobjects.value.Value;

public class BlockInstruction extends Instruction {
	private MaterialPicker picker;
	private Value x;
	private Value y;
	private Value z;
	private boolean outer;

	public BlockInstruction(IWGO iwgo, String name) {
		super(iwgo, name);
	}

	@Override
	public void execute() {
		picker.setMaterial(getIWGO().transform(x.getValue(), y.getValue(), z.getValue()), outer);
	}

	@Override
	public void randomize() {
		super.randomize();
		x.calculate();
		y.calculate();
		z.calculate();
	}

	@Override
	public void setRandom(Random random) {
		super.setRandom(random);
		if (x instanceof RandomOwner) {
			((RandomOwner) x).setRandom(random);
		}
		if (y instanceof RandomOwner) {
			((RandomOwner) y).setRandom(random);
		}
		if (z instanceof RandomOwner) {
			((RandomOwner) z).setRandom(random);
		}
	}

	public boolean isOuter() {
		return outer;
	}

	public void setOuter(boolean outer) {
		this.outer = outer;
	}

	public MaterialPicker getPicker() {
		return picker;
	}

	public void setPicker(MaterialPicker picker) {
		this.picker = picker;
	}

	public Value getX() {
		return x;
	}

	public void setX(Value x) {
		this.x = x;
	}

	public Value getY() {
		return y;
	}

	public void setY(Value y) {
		this.y = y;
	}

	public Value getZ() {
		return z;
	}

	public void setZ(Value z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "BlockInstruction{picker=" + picker + ", x=" + x + ", y=" + y + ", z=" + z
				+ ", outer=" + outer + '}';
	}
}
