package com.troy.empireserialization.cache;

public class IntKey<E> {
	public int key;
	public E value;
	public IntKey<E> next;

	public IntKey(int key, E value, IntKey<E> next) {
		this.key = key;
		this.value = value;
		this.next = next;
	}
}
