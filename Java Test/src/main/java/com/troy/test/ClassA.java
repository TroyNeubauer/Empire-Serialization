package com.troy.test;

import java.util.ArrayList;
import java.util.List;

public class ClassA {
	private String name;
	private int integer;
	private ClassB b;
	private List<String> randomStrings;

	private static final String[] RANDOM_STRINGS = { "one", "two", "empire", "serialization",
			"NULL CHARACTER TEST: " + '\0', "wierd unicode test" + ((char) 0x19EA) };

	public ClassA(String name, int integer) {
		this.name = name;
		this.integer = integer;
		this.b = new ClassB((int) (Math.random() * 10), (int) (Math.random() * 10) - 10);
		this.randomStrings = new ArrayList<String>();
		for (int i = 0; i < Math.random() * RANDOM_STRINGS.length; i++)
			randomStrings.add(RANDOM_STRINGS[(int) (Math.random() * RANDOM_STRINGS.length)]);

		for (int i = 0; i < 10; i++)
			randomStrings.add(Double.toString(Math.random()));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInteger() {
		return integer;
	}

	public void setInteger(int integer) {
		this.integer = integer;
	}

	public ClassB getB() {
		return b;
	}

	public void setB(ClassB b) {
		this.b = b;
	}

	public List<String> getRandomStrings() {
		return randomStrings;
	}

	public void setRandomStrings(List<String> randomStrings) {
		this.randomStrings = randomStrings;
	}

}
