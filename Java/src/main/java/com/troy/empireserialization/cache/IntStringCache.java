package com.troy.empireserialization.cache;

public class IntStringCache extends Cache<IntStringValue> {

	public IntStringCache() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	public IntStringCache(int initalCapacity) {
		this(initalCapacity, DEFAULT_LOAD_FACTOR);
	}

	public IntStringCache(int initalCapacity, double loadFactor) {
		super(initalCapacity, loadFactor);

	}

	public void add(int key, String value) {
		if ((double) size / table.length >= loadFactor) {
			resize(size * 2);
		}
		int index = hash(key);
		IntStringValue se = new IntStringValue(key, value, null);
		if (table[index] == null) {
			table[index] = se;
		} else {
			IntStringValue entry = table[index];
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

	public IntStringValue get(int key) {
		// Get a positive index using mod
		int index = hash(key);
		IntStringValue entry = table[index];
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
		IntStringValue[] oldStrings = table;
		this.table = new IntStringValue[newSize];
		if (oldStrings != null) {
			for (int i = 0; i < oldStrings.length; i++) {
				IntStringValue entry = oldStrings[i];
				while (entry != null) {
					add(entry.key, entry.value);
					entry = entry.next;
				}
			}
		}
	}
}
