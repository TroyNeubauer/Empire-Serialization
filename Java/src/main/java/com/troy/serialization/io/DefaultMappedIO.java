package com.troy.serialization.io;

public class DefaultMappedIO extends MappedIO {

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
