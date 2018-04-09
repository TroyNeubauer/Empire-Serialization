package com.troy.empireserialization.util;

public class IntCache<E> extends Cache<IntValue<E>> {

	public IntCache() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	public IntCache(int initalCapacity) {
		this(initalCapacity, DEFAULT_LOAD_FACTOR);
	}

	public IntCache(int initalCapacity, double loadFactor) {
		super(initalCapacity, loadFactor);

	}

	public void add(E key, int value) {
		if ((double) size / table.length >= loadFactor) {
			resize(size * 2);
		}
		int index = hash(key);
		IntValue<E> se = new IntValue<E>(key, value, null);
		if (table[index] == null) {
			table[index] = se;
		} else {
			IntValue<E> entry = table[index];
			while (entry.next != null) {
				if (entry.equals(key))
					return;
				entry = entry.next;
			}
			if (entry.equals(key))
				return;
			entry.next = se;
		}
		size++;
	}

	public IntValue<E> get(Object key) {
		int index = key.hashCode() % table.length;
		IntValue<E> entry = table[index];
		if (entry != null) {
			while (entry.next != null) {
				if (entry.equals(key))
					return entry;
				entry = entry.next;
			}
		}

		return null;
	}

	protected void resize(int newSize) {
		IntValue<E>[] oldStrings = table;
		size = newSize;
		this.table = new IntValue[newSize];
		if (oldStrings != null) {
			for (int i = 0; i < oldStrings.length; i++) {
				IntValue<E> entry = oldStrings[i];
				while (entry != null) {
					add(entry.key, entry.value);
					entry = entry.next;
				}
			}
		}
	}
}
