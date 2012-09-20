package org.spout.infiniteobjects.variable;

import java.util.Collections;
import java.util.List;

public class StaticVariable implements Variable {
	private final String name;
	private final double value;

	public StaticVariable(String name, double value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void calculate() {
	}

	@Override
	public double getValue() {
		return value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Variable> getReferences() {
		return Collections.emptyList();
	}

	@Override
	public String toString() {
		return name;
	}
}
