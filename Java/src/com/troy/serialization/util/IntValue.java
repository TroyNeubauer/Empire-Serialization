package com.troy.serialization.util;

public class IntValue<E> {
	public E key;
	public int value;
	public IntValue next;

	public IntValue(E key, int value, IntValue next) {
		this.key = key;
		this.value = value;
		this.next = next;
	}

}