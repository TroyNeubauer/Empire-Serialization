package com.troy.empireserialization.cache;

public abstract class ObjectKeyCache<Entry, Key> extends Cache<Entry> {

	public ObjectKeyCache(int initalCapacity, double loadFactor) {
		super(initalCapacity, loadFactor);
	}
	
	public abstract Entry get(Key key);

}
