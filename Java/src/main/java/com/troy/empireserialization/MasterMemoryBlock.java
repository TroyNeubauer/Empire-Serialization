package com.troy.empireserialization;

import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.util.*;

import sun.misc.Unsafe;

public class MasterMemoryBlock implements NativeMemoryBlock {
	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	public long address, capacity, position;

	public MasterMemoryBlock(long address, long capacity, long position) {
		this.address = address;
		this.capacity = capacity;
		this.position = position;
	}

	public static MasterMemoryBlock allocate(long bytes) {
		return new MasterMemoryBlock(unsafe.allocateMemory(bytes), bytes, 0);
	}

	@Override
	public long address() {
		if(address == 0) throw new AlreadyClosedException();
		return address;
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
		if (address != 0)
			unsafe.freeMemory(address);
		address = 0;
	}

	@Override
	public void resize(long bytes) {
		if(address == 0) throw new AlreadyClosedException();
		if (bytes > capacity) {
			address = unsafe.reallocateMemory(address, bytes);
			capacity = bytes;
		}
	}

	@Override
	public String toString() {
		return "MasterMemoryBlock [address=" + address + ", capacity=" + capacity + ", position=" + position + "]";
	}

	@Override
	public void checkOffset(long offset) {
		if (offset < 0)
			throw new RuntimeException("Negative offset not allowed! Offset " + offset);
		if (offset > capacity)
			throw new RuntimeException("Offset out of range! Buffer Capacity: " + capacity + " offset " + offset);
	}

}
