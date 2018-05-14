package com.troy.empireserialization.cache;

public class IntStringValue {
	public int key;
	public String value;
	public IntStringValue next;

	public IntStringValue(int key, String value, IntStringValue next) {
		this.key = key;
		this.value = value;
		this.next = next;
	}
}
