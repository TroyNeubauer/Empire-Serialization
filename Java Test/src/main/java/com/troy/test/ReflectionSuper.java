package com.troy.test;

public class ReflectionSuper<T, E> {
	private T t;
	private E e;

	public ReflectionSuper(T t, E e) {
		super();
		this.t = t;
		this.e = e;
	}

	public T getT() {
		return t;
	}

	public E getE() {
		return e;
	}

}
