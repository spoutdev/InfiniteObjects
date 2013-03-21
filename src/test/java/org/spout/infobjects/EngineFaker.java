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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.powermock.api.mockito.PowerMockito;

import org.spout.api.Engine;
import org.spout.api.FileSystem;
import org.spout.api.Platform;
import org.spout.api.Spout;

public class EngineFaker {
	private static final Engine ENGINE;

	static {
		final Engine engine = PowerMockito.mock(Engine.class);
		final FileSystem filesystem = PowerMockito.mock(FileSystem.class);
		try {
			PowerMockito.when(engine, Engine.class.getMethod("getPlatform", (Class[]) null)).
					withNoArguments().thenReturn(Platform.SERVER);
			PowerMockito.when(engine, Engine.class.getMethod("getLogger", (Class[]) null)).
					withNoArguments().thenReturn(new Logger("InfObjects.Tests", null) {
				@Override
				public void log(Level level, String string) {
					System.out.println("[" + level.getLocalizedName() + "] " + string);
				}
			});
			PowerMockito.stub(Engine.class.getMethod("getFilesystem", (Class[]) null)).toReturn(filesystem);
			PowerMockito.stub(FileSystem.class.getMethod("getResource", new Class[]{String.class})).toReturn(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (engine == null) {
			throw new NullPointerException("Engine is null");
		}
		if (engine.getPlatform() == null) {
			throw new NullPointerException("Platform is null");
		}
		if (engine.getLogger() == null) {
			throw new NullPointerException("Logger is null");
		}
		Spout.setEngine(engine);
		ENGINE = engine;
	}

	public static Engine setupEngine() {
		return ENGINE;
	}
}
