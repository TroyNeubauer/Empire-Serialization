package com.troy.empireserialization.cache;

public class IntValueCache<E> extends ObjectKeyCache<IntValue<E>, E> {

	public IntValueCache() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	public IntValueCache(int initalCapacity) {
		this(initalCapacity, DEFAULT_LOAD_FACTOR);
	}

	public IntValueCache(int initalCapacity, double loadFactor) {
		super(initalCapacity, loadFactor);

	}

	public void add(E key, int value) {
		if ((double) size >= loadFactor * table.length) {
			resize(size * 2);
		}
		int index = hash(key);
		IntValue<E> se = new IntValue<E>(key, value, null);
		if (table[index] == null) {
			table[index] = se;
		} else {
			IntValue<E> entry = table[index];
			while (entry.next != null) {
				if (entry.key.equals(key))
					return;
				entry = entry.next;
			}
			if (entry.key.equals(key))
				return;
			entry.next = se;
		}
		size++;
	}

	public IntValue<E> get(E key) {
		// Get a positive index using mod
		int index = hash(key);
		IntValue<E> entry = table[index];
		if (entry != null) {
			while (entry.next != null) {
				if (entry.key.equals(key))
					return entry;
				entry = entry.next;
			}
		}
		if (entry == null)
			return null;
		if (entry.key.equals(key))
			return entry;
		return null;
	}

	protected void resize(int newSize) {
		IntValue<E>[] oldTable = table;
		this.table = new IntValue[newSize];
		if (oldTable != null) {
			for (int i = 0; i < oldTable.length; i++) {
				IntValue<E> entry = oldTable[i];
				while (entry != null) {
					add(entry.key, entry.value);
					entry = entry.next;
				}
			}
		}
	}
}
