package com.troy.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassA implements Serializable {
	private String name;
	private int integer;
	private ClassB b;
	private List<String> randomStrings;

	private static final String[] RANDOM_STRINGS = { "one", "two", "empire", "serialization",
			"NULL CHARACTER TEST: " + '\0', "wierd unicode test" + ((char) 0x19EA) };

	public ClassA(String name, int integer) {
		this.name = name;
		this.integer = integer;
		Random random = new Random(12345L);
		this.b = new ClassB((int) (random.nextDouble() * 10), (int) (random.nextDouble() * 10) - 10);
		this.randomStrings = new ArrayList<String>();
		for (int i = 0; i < random.nextDouble() * RANDOM_STRINGS.length; i++)
			randomStrings.add(RANDOM_STRINGS[(int) (random.nextDouble() * RANDOM_STRINGS.length)]);

		for (int i = 0; i < 10; i++)
			randomStrings.add(Double.toString(random.nextDouble()));
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + integer;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((randomStrings == null) ? 0 : randomStrings.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassA other = (ClassA) obj;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (integer != other.integer)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (randomStrings == null) {
			if (other.randomStrings != null)
				return false;
		} else if (!randomStrings.equals(other.randomStrings))
			return false;
		return true;
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

	@Override
	public String toString() {
		return "ClassA [name=" + name + ", integer=" + integer + ", b=" + b + ", randomStrings=" + randomStrings + "]";
	}
	
	

}
