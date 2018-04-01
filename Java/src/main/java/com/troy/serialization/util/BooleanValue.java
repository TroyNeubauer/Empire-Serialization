package com.troy.serialization.util;

public class BooleanValue<E> {
	public E key;
	public boolean value;
	public BooleanValue<E> next;

	public BooleanValue(E key, boolean value, BooleanValue<E> next) {
		this.key = key;
		this.value = value;
		this.next = next;
	}
}
