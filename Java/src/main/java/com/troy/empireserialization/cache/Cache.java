package com.troy.empireserialization.cache;

public abstract class Cache<Entry, E> {
	protected static final double DEFAULT_LOAD_FACTOR = 0.5;
	protected static final int DEFAULT_CAPACITY = 16;

	protected Entry[] table;
	protected double loadFactor;
	protected int size = 0;

	public Cache(int initalCapacity, double loadFactor) {
		this.loadFactor = loadFactor;
		resize(initalCapacity);
	}

	protected abstract void resize(int newSize);
	
	public abstract Entry get(E key);

	public int hash(Object key) {
		return key.hashCode() % table.length;
	}
	
	public int size() {
		return size;
	}

}
