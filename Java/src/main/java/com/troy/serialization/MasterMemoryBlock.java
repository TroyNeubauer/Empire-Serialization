package com.troy.serialization;

import com.troy.serialization.util.*;
import sun.misc.Unsafe;

public class MasterMemoryBlock implements NativeMemoryBlock {
	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	protected long address, capacity, position;

	@Override
	public long address() {
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
		return null;
	}

	@Override
	public void free() {
		if (address != 0)
			unsafe.freeMemory(address);
		address = 0;
	}

	@Override
	public void resize(long bytes) {
		
	}

}
