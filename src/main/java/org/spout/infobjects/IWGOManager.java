/*
 * This file is part of InfiniteObjects.
 *
 * Copyright (c) 2012 Spout LLC <http://www.spout.org/>
 * InfiniteObjects is licensed under the Spout License Version 1.
 *
 * InfiniteObjects is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * InfiniteObjects is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license,
 * including the MIT license.
 */
package org.spout.infobjects;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.spout.infobjects.exception.IWGOLoadingException;

/**
 * A simple manager for iWGOs located in the same directory. This manager loads all the iWGO in a
 * directory and manages access and loading.
 */
public class IWGOManager {
	private final File directory;
	private final Map<String, IWGO> iwgos = new ConcurrentHashMap<String, IWGO>();

	/**
	 * Constructs a new iWGO manager. It will manage a directory and if wanted can create the
	 * directory if missing. The string path will be converted to a {@link java.io.File}.
	 *
	 * @param dirPath The directory to manage
	 * @param createDir If true, will create the directory if missing
	 */
	public IWGOManager(String dirPath, boolean createDir) {
		this(new File(dirPath), createDir);
	}

	/**
	 * Constructs a new iWGO manager. It will manage a directory and if wanted can create the
	 * directory if missing.
	 *
	 * @param directory The directory to manage
	 * @param createFolder If true, will create the directory if missing
	 */
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

	/**
	 * Loads the iWGO from the files in the directory. Will replace any already loaded versions of
	 * the iWGOs.
	 */
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

	/**
	 * Unloads the iWGO. Clears the map.
	 */
	public void unloadIWGOs() {
		synchronized (iwgos) {
			iwgos.clear();
		}
	}

	/**
	 * Calls {@link #unloadIWGOs()} then calls {@link #loadIWGOs()}.
	 */
	public void reloadIWGOs() {
		unloadIWGOs();
		loadIWGOs();
	}

	/**
	 * Gets an iWGO from it's name.
	 *
	 * @param name The name of the iWGO to lookup
	 * @return The iWGO with the request name, or null if none could be found
	 */
	public IWGO getIWGO(String name) {
		return iwgos.get(name);
	}

	/**
	 * Gets the loaded iWGO as an unmodifiable collection.
	 *
	 * @return The loaded iWGOs
	 */
	public Collection<IWGO> getIWGOs() {
		return Collections.unmodifiableCollection(iwgos.values());
	}

	/**
	 * Gets the loaded iWGO map (mapped as name and iWGO) as an unmodifiable map.
	 *
	 * @return The loaded iWGO map
	 */
	public Map<String, IWGO> getIWGOMap() {
		return Collections.unmodifiableMap(iwgos);
	}
}
