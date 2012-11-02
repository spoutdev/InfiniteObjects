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
import org.junit.Before;
import org.junit.Test;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.yaml.YamlConfiguration;
import org.spout.infobjects.value.CalculableValue;
import org.spout.infobjects.value.Value;
import org.spout.infobjects.value.ValueParser;

public class ValueParserTest {
	@Before
	public void before() throws ClassNotFoundException {
		Class.forName("org.spout.infobjects.function.RandomFunction");
	}

	@Test
	public void test() throws ConfigurationException {
		final YamlConfiguration config = new YamlConfiguration(new File("src/test/resources/ValueParserTest.yml"));
		config.load();
		for (String key : config.getKeys(false)) {
			Value value = ValueParser.parse(config.getNode(key).getString());
			if (value == null) {
				System.out.println("INVALID");
			} else {
				System.out.print(value.getClass().getSimpleName() + ": ");
				if (value instanceof CalculableValue) {
					((CalculableValue) value).calculate();
				}
				System.out.println(value.getValue());
			}
		}
	}
}
