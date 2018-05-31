package com.troy.empireserialization.cache;

public abstract class Cache<Entry> {
	protected static final double DEFAULT_LOAD_FACTOR = 0.5;
	protected static final int DEFAULT_CAPACITY = 32;

	protected Entry[] table;
	protected double loadFactor;
	protected int size = 0;

	public Cache(int initalCapacity, double loadFactor) {
		this.loadFactor = loadFactor;
		resize(initalCapacity);
	}

	protected abstract void resize(int newSize);

	public int hash(Object key) {
		return key == null ? 0 : ((table.length - 1) & key.hashCode());
	}

	public int size() {
		return size;
	}

}
