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
package org.spout.infobjects.material;

import java.util.Map;

import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.util.Named;

import org.spout.infobjects.util.TypeFactory;

public abstract class MaterialSetter implements Named {
	private static final TypeFactory<MaterialSetter> SETTERS = new TypeFactory<MaterialSetter>(String.class);
	private final String name;

	static {
		register("simple", SimpleSetter.class);
		register("random-simple", RandomSimpleSetter.class);
		register("inner-outer", InnerOuterSetter.class);
		register("random-inner-outer", RandomInnerOuterSetter.class);
	}

	public MaterialSetter(String name) {
		this.name = name;
	}

	public abstract void configure(Map<String, String> properties);

	public void setMaterial(Point pos, boolean outer) {
		setMaterial(pos.getWorld(), pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), outer);
	}

	public abstract void setMaterial(World world, int x, int y, int z, boolean outer);

	@Override
	public String getName() {
		return name;
	}

	public static void register(String type, Class<? extends MaterialSetter> setter) {
		SETTERS.register(type, setter);
	}

	public static MaterialSetter newMaterialSetter(String type, String name) {
		return SETTERS.newInstance(type, name);
	}
}
