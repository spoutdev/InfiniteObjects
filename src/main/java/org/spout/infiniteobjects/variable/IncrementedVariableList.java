package org.spout.infiniteobjects.variable;

import de.congrace.exp4j.Calculable;
import org.spout.infiniteobjects.IFOWorldGeneratorObject;

public class IncrementedVariableList extends VariableList {
	private final Variable increment;

	public IncrementedVariableList(IFOWorldGeneratorObject owner, String name,
			Calculable rawValue, Variable size, Variable increment) {
		super(owner, name, rawValue, size);
		this.increment = increment;
	}

	@Override
	public void calculateValues() {
		super.calculateValues();
		double incr = 0;
		for (int i = 0; i < values.length; i++) {
			values[i] += incr;
			increment.calculateValue();
			incr += increment.getValue();
		}
	}
}
