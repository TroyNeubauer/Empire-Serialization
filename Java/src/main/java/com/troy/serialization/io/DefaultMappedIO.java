package com.troy.serialization.io;

import com.troy.serialization.util.*;

public class DefaultMappedIO extends MappedIO {

	public static DefaultMappedIO create(long capacity) {
		return new DefaultMappedIO(MiscUtil.getUnsafe().allocateMemory(capacity), 0, capacity);
	}
	
	public DefaultMappedIO(long address, long offset, long capacity) {
		super(address, offset, capacity);
	}

	@Override
	public void require(long bytes) {
		if(bytes + offset > capacity) {
			address = MappedIO.unsafe.reallocateMemory(address, capacity * 2);
		}
	}

}
