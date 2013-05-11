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
package org.spout.infobjects.condition;

import java.util.Set;
import org.spout.api.material.BlockMaterial;

/**
 * An enum with the modes a condition can have when checking the condition volume for materials. The
 * include mode means the condition should return true only if all the materials are present in the
 * volume. The exclude mode means it should check that none are present.
 */
public enum ConditionMode {
	INCLUDE, EXCLUDE;

	/**
	 * Runs the check for a material according to the mode. If the mode is include, this method will
	 * return false if the material is in the provided set. If it is exclude, it will return false
	 * if it is not.
	 *
	 * @param material The material to check
	 * @param materials The material set to check in
	 * @return True or false depending on the mode and the presence or absence of the material in
	 * the set
	 */
	public boolean check(BlockMaterial material, Set<BlockMaterial> materials) {
		switch (this) {
			case INCLUDE:
				return materials.contains(material);
			case EXCLUDE:
				return !materials.contains(material);
			default:
				return false;
		}
	}
}