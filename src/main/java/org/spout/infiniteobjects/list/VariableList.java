package org.spout.infiniteobjects.list;

import de.congrace.exp4j.Calculable;
import java.util.ArrayList;
import java.util.List;
import org.spout.infiniteobjects.IFOWorldGeneratorObject;
import org.spout.infiniteobjects.variable.Variable;

public class VariableList {
	private final IFOWorldGeneratorObject owner;
	private final String name;
	// the expression used to calculateValue the value
	private final Calculable rawValue;
	// the cached values
	private final double[] values;
	// referenced variables and variables to obtain values for calculation
	private final List<Variable> referencedVariables = new ArrayList<Variable>();
	private final List<VariableList> referencedLists = new ArrayList<VariableList>();

	public VariableList(IFOWorldGeneratorObject owner, String name, Calculable rawValue, int size) {
		this.owner = owner;
		this.name = name;
		this.rawValue = rawValue;
		values = new double[size];
	}

	public void calculateValues() {
		for (Variable ref : referencedVariables) {
			rawValue.setVariable(ref.getName(), ref.getValue());
		}
		for (int i = 0; i < values.length; i++) {
			for (VariableList ref : referencedLists) {
				rawValue.setVariable(ref.getName(), ref.getValue(i));
			}
			values[i] = rawValue.calculate();
		}
	}

	public double getValue(int index) {
		return values[index];
	}

	public String getName() {
		return name;
	}
}
