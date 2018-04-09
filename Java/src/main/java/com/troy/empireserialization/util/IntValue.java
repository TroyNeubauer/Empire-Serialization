package com.troy.empireserialization.util;

public class IntValue<E> {
	public E key;
	public int value;
	public IntValue<E> next;

	public IntValue(E key, int value, IntValue<E> next) {
		this.key = key;
		this.value = value;
		this.next = next;
	}

}