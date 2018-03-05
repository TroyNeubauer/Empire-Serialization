package com.troy.serialization.util;

public abstract class Cache<Entry> {
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
	
	public abstract Entry get(Object key);
	

}
