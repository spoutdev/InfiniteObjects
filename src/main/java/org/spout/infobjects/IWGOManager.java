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

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.spout.infobjects.exception.IWGOLoadingException;

public class IWGOManager {
	private final File directory;
	private final Map<String, IWGO> iwgos = new ConcurrentHashMap<String, IWGO>();

	public IWGOManager(String folderDir, boolean createFolder) {
		this(new File(folderDir), createFolder);
	}

	public IWGOManager(File directory, boolean createFolder) {
		if (!directory.exists()) {
			if (createFolder) {
				directory.mkdirs();
			} else {
				throw new IllegalArgumentException("File does not exist");
			}
		}
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("File is not a directory");
		}
		this.directory = directory;
	}

	public void loadIWGOs() {
		synchronized (iwgos) {
			for (File file : directory.listFiles()) {
				if (file.isHidden()) {
					continue;
				}
				try {
					final IWGO iwgo = IWGOLoader.loadIWGO(file);
					iwgos.put(iwgo.getName(), iwgo);
				} catch (IWGOLoadingException ex) {
					IWGOLoader.logIWGOLoadingException(ex);
				}
			}
		}
	}

	public void unloadIWGOs() {
		synchronized (iwgos) {
			iwgos.clear();
		}
	}

	public void reloadIWGOs() {
		unloadIWGOs();
		loadIWGOs();
	}

	public IWGO getIWGO(String name) {
		return iwgos.get(name);
	}

	public Collection<IWGO> getIWGOs() {
		return Collections.unmodifiableCollection(iwgos.values());
	}

	public Map<String, IWGO> getIWGOMap() {
		return Collections.unmodifiableMap(iwgos);
	}
}
