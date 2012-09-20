package org.spout.infiniteobjects.function;

import de.congrace.exp4j.CustomFunction;
import de.congrace.exp4j.InvalidCustomFunctionException;
import java.util.Random;

public abstract class RandomFunction extends CustomFunction {
	protected Random random = new Random();

	public RandomFunction(String name, int argumentCount) throws InvalidCustomFunctionException {
		super(name, argumentCount);
	}

	public RandomFunction(String name) throws InvalidCustomFunctionException {
		super(name);
	}

	public void setRandom(Random random) {
		this.random = random;
	}
}
