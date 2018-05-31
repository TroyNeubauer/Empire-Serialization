package com.troy.empireserialization.cache;

public class IntKeyCache<E> extends Cache<IntKey<E>>{
	public IntKeyCache() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	public IntKeyCache(int initalCapacity) {
		this(initalCapacity, DEFAULT_LOAD_FACTOR);
	}

	public IntKeyCache(int initalCapacity, double loadFactor) {
		super(initalCapacity, loadFactor);

	}

	public void add(int key, E value) {
		if ((double) size / table.length >= loadFactor) {
			resize(size * 2);
		}
		int index = hash(key);
		IntKey<E> se = new IntKey<E>(key, value, null);
		if (table[index] == null) {
			table[index] = se;
		} else {
			IntKey<E> entry = table[index];
			while (entry.next != null) {
				if (entry.key == key)
					return;
				entry = entry.next;
			}
			if (entry.key == key)
				return;
			entry.next = se;
		}
		size++;
	}

	public IntKey<E> get(int key) {
		// Get a positive index using mod
		int index = hash(key);
		IntKey<E> entry = table[index];
		if (entry != null) {
			while (entry.next != null) {
				if (entry.key == key)
					return entry;
				entry = entry.next;
			}
		}
		return null;
	}

	protected void resize(int newSize) {
		IntKey<E>[] oldStrings = table;
		this.table = new IntKey[newSize];
		if (oldStrings != null) {
			for (int i = 0; i < oldStrings.length; i++) {
				IntKey<E> entry = oldStrings[i];
				while (entry != null) {
					add(entry.key, entry.value);
					entry = entry.next;
				}
			}
		}
	}
}
