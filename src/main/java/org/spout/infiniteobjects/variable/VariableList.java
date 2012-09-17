package org.spout.infiniteobjects.variable;

import de.congrace.exp4j.Calculable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.spout.infiniteobjects.IFOWorldGeneratorObject;

public class VariableList {
	private final IFOWorldGeneratorObject owner;
	private final String name;
	// the expression used to calculateValue the value
	private final Calculable rawValue;
	private final Variable size;
	// the cached values
	protected double[] values;
	// referenced variables and variables to obtain values for calculation
	private final Set<Variable> referencedVariables = new HashSet<Variable>();
	private final Set<VariableList> referencedLists = new HashSet<VariableList>();

	public VariableList(IFOWorldGeneratorObject owner, String name, Calculable rawValue, Variable size) {
		this.owner = owner;
		this.name = name;
		this.rawValue = rawValue;
		this.size = size;
	}

	public void addVariableReference(Variable variable) {
		referencedVariables.add(variable);
	}

	public void addVariableReferences(Collection<Variable> variables) {
		referencedVariables.addAll(variables);
	}

	public void addListReference(VariableList variable) {
		referencedLists.add(variable);
	}

	public void addListReferences(Collection<VariableList> variables) {
		referencedLists.addAll(variables);
	}

	public Set<VariableList> getReferencedLists() {
		return referencedLists;
	}

	public Set<Variable> getReferencedVariables() {
		return referencedVariables;
	}

	public void calculateValues() {
		size.calculateValue();
		values = new double[(int) size.getValue()];
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

	public int getSize() {
		return values.length;
	}
}
