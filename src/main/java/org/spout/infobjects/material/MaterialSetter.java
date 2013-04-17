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

/**
 * An abstract material setter. Provides the name and some overloads for the {@link #setMaterial(org.spout.api.geo.World, int, int, int, boolean)}
 * method. Register your own material setter with {@link #register(java.lang.String, java.lang.Class)}
 * so the iWGO loader can recognize it. Make sure there's at least one constructor with the same
 * arguments as the one for this class, as it's the one that will be called for construction.
 */
public abstract class MaterialSetter implements Named {
	private static final TypeFactory<MaterialSetter> SETTERS = new TypeFactory<MaterialSetter>(String.class);
	private final String name;

	/**
	 * Constructs a new material setter from its name.
	 *
	 * @param name The name of the material setter
	 */
	public MaterialSetter(String name) {
		this.name = name;
	}

	/**
	 * Configures the material setter with the provided properties, passed as a string, string map.
	 * The values for the map are often parseable into {@link org.spout.infobjects.value.Value}s,
	 * although this depends on the implementation. This method is called during iWGO loading.
	 *
	 * @param properties The properties as a string, string map
	 */
	public abstract void configure(Map<String, String> properties);

	/**
	 * Sets a material at the point. The material depends on the material setter implementation and
	 * configuration and the value of the outer parameter. If a material is being set at the outer
	 * edge of a shape, the parameter should be true, else, false. Material setters might provide
	 * different materials for the outside and the inside of a shape.
	 *
	 * @param pos The position at which to set the material
	 * @param outer Whether or not the material is outside or inside the shape
	 */
	public void setMaterial(Point pos, boolean outer) {
		setMaterial(pos.getWorld(), pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), outer);
	}

	/**
	 * Sets a material at the coordinates in the provided world. The material depends on the
	 * material setter implementation and configuration and the value of the outer parameter. If a
	 * material is being set at the outer edge of a shape, the parameter should be true, else,
	 * false. Material setters might provide different materials for the outside and the inside of a
	 * shape.
	 *
	 * @param world The world to set the material in
	 * @param x The x coordinate of the world position
	 * @param y The y coordinate of the world position
	 * @param z The z coordinate of the world position
	 * @param outer Whether or not the material is outside or inside the shape
	 */
	public abstract void setMaterial(World world, int x, int y, int z, boolean outer);

	/**
	 * Gets the name of the material setter.
	 *
	 * @return The name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Registers a material setter type. The name of the type is as declared in the iWGO
	 * configuration. Registering is necessary for the iWGO loader to recognize and load the
	 * material setter. The type must be unique.
	 *
	 * @param type The type of the material setter, also its name
	 * @param setter The class of the material setter to register
	 */
	public static void register(String type, Class<? extends MaterialSetter> setter) {
		SETTERS.register(type, setter);
	}

	/**
	 * Creates a new instance of the desired material setter type via reflection. The type is the
	 * same as the one registered. The name will be passed to the constructor.
	 *
	 * @param type The type of material setter to create
	 * @param name The name to be passed to the constructor
	 * @return A new material setter of the desired type
	 */
	public static MaterialSetter newMaterialSetter(String type, String name) {
		return SETTERS.newInstance(type, name);
	}
}
