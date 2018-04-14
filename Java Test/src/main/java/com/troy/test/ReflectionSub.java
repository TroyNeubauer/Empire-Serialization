package com.troy.test;

public class ReflectionSub<E> extends ReflectionSuper<Integer, E> {
	
	private String name;

	public ReflectionSub(Integer t, E e, String name) {
		super(t, e);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
