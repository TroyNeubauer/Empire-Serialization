package com.troy.serialization.io;

import com.troy.serialization.exception.*;
import com.troyberry.util.MiscUtil;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class NativeOutput extends AbstractOutput {

	private static final Unsafe unsafe = MiscUtil.getUnsafe();
	private static final long DEFAULT_CAPACITY = 128;

	private long address;
	private long capacity, size;

	public NativeOutput() {
		this(DEFAULT_CAPACITY);
	}

	public NativeOutput(long capacity) {
		this.address = unsafe.allocateMemory(capacity);
		this.capacity = capacity;
		this.size = 0;
	}

	@Override
	public void writeByteImpl(byte b) {
		unsafe.putByte(address + size++, b);
	}

	@Override
	public void flush() {
		// Nop
	}

	@Override
	public boolean hasBuffer() {
		return true;
	}

	@Override
	public int getBufferPosition() {
		if (size > Integer.MAX_VALUE)
			throw new IllegalStateException("The native buffer currently holds more bytes than the maxium value of an integer");
		return (int) size;
	}

	@Override
	public byte[] getBuffer() {
		if (size > Integer.MAX_VALUE)
			throw new IllegalStateException("The native buffer currently holds more bytes than the maxium value of an integer");
		byte[] array = ngetBuffer(address, (int) capacity);
		if (array == null)
			throw new OutOfMemoryError();
		return array;
	}

	private static native byte[] ngetBuffer(long address, int capacity);

	@Override
	public void require(long bytes) {
		if (bytes + size > capacity) {
			long newSize = (bytes / 2 + 1) / 2;// Round up to next power of two
			address = unsafe.reallocateMemory(address, newSize);
		}
	}

	@Override
	public void close() {
		if (address == -1)
			throw new AlreadyClosedException();
		unsafe.freeMemory(address);
		address = -1;
		capacity = 0;
		size = 0;
	}

	@Override
	public void resetMappedOutputImpl(AbstractMappedIO out, long minSize) {
	}

	@Override
	public AbstractMappedIO newMappedOutput(long minSize) {
		return null;
	}

	@Override
	public void unmapOutputImpl(AbstractMappedIO out, long numBytesWritten) {
	}
}
