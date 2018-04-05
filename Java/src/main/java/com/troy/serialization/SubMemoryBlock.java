package com.troy.serialization;

import com.troy.serialization.util.*;

import sun.misc.*;

public class SubMemoryBlock implements NativeMemoryBlock {
	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	private NativeMemoryBlock parent;
	private long offset, capacity, position;

	public SubMemoryBlock(NativeMemoryBlock parent, long offset, long length) {
		this.parent = parent;
		this.offset = offset;
		this.capacity = length;
	}

	@Override
	public long address() {
		return parent.address() + offset;
	}

	@Override
	public long capacity() {
		return capacity;
	}

	@Override
	public long position() {
		return position;
	}

	@Override
	public void setPosition(long position) {
		this.position = position;

	}

	@Override
	public NativeMemoryBlock subset(long offset, long length) {
		if (offset < 0 || length < 0)
			throw new IllegalArgumentException();
		if (offset + length > capacity)
			throw new IllegalArgumentException("Attempting to create a subset from memory that is out of bounds capacity: " + capacity
					+ " Requested offset: " + offset + " Requested length " + length);

		return new SubMemoryBlock(this, offset, length);
	}

	@Override
	public void free() {
		//Nothing as we do not own this memory, our parent does
	}

	@Override
	public void resize(long bytes) {
		this.capacity = bytes;
		parent.resize(bytes + offset);
	}

	@Override
	public String toString() {
		return "SubMemoryBlock [parent=" + parent + ", offset=" + offset + ", capacity=" + capacity + ", position=" + position + "]";
	}

	@Override
	public void checkOffset(long offset) {
		//Add this offset so that the parent receives an offset relative to them, not us
		parent.checkOffset(offset + this.offset);
	}
	
	

}
