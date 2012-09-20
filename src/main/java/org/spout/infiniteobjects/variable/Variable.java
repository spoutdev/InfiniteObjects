package org.spout.infiniteobjects.variable;

import java.util.List;

import org.spout.api.util.Named;

public interface Variable extends Named {
	public void calculate();

	public double getValue();
	
	public List<Variable> getReferences();
}
