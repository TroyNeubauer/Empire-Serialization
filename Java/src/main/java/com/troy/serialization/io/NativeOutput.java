package com.troy.serialization.io;

import com.troy.serialization.exception.NoBufferException;
import com.troy.serialization.util.MiscUtil;
import com.troy.serialization.util.NativeUtils;
import com.troy.serialization.util.SerializationUtils;

import sun.misc.Unsafe;

public class NativeOutput extends AbstractNativeOutput<com.troy.serialization.io.NativeOutput.Deallocator> {

	static {
		SerializationUtils.init();
	}

	private static final Unsafe unsafe = MiscUtil.getUnsafe();
	private static final long DEFAULT_CAPACITY = 128;

	private long address, position;
	private long capacity, size, requested;

	public NativeOutput() {
		this(DEFAULT_CAPACITY);
	}

	class Deallocator implements Runnable {
		private long addressToFree;

		public Deallocator(long address) {
			this.addressToFree = address;
		}

		@Override
		public void run() {
			if (addressToFree != 0) {
				unsafe.freeMemory(addressToFree);
				addressToFree = 0;
			}
		}
	}

	public NativeOutput(long capacity) {
		this.address = unsafe.allocateMemory(capacity);
		this.capacity = capacity;
		this.size = 0;
		setDeallocator(new Deallocator(address));
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
		return false;
	}

	@Override
	public int getBufferPosition() {
		throw new NoBufferException();
	}

	@Override
	public byte[] getBuffer() {
		throw new NoBufferException();
	}

	public byte[] toByteArray() {
		if (capacity > Integer.MAX_VALUE)
			throw new RuntimeException("Cannot represent this buffer as a byte array. The capacity is to big");
		return ngetBuffer(address, (int) capacity);
	}

	public static native byte[] ngetBuffer(long address, int capacity);

	@Override
	public void require(long bytes) {
		if (bytes + size > capacity) {
			long newSize = (bytes / 2 + 1) * 2;// Round up to next power of two
			address = unsafe.reallocateMemory(address, newSize);
			getDeallocator().addressToFree = this.address;
			capacity = newSize;
		}
		requested = bytes;
	}

	@Override
	public void addRequired() {
		position += requested;
	}

	@Override
	public void resetMappedOutputImpl(MappedIO out, long minSize) {
		//Do nothing since
	}
	
	@Override
	public MappedIO newMappedOutput(long minSize) {
		require(minSize);
		return new DefaultMappedIO(address, size, capacity);
	}

	@Override
	public void unmapOutputImpl(MappedIO out, long numBytesWritten) {
		require(numBytesWritten);
		// FIXME actually copy bytes
		// nmemcpy(address, position, out.address, out.offset, numBytesWritten);
		size += numBytesWritten;
	}

	@Override
	public void writeBytes(byte[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Short.BYTES);
			NativeUtils.bytesToNative(address + position, src, offset, elements, swapEndianess);
			addRequired();
		} else {
			super.writeBytes(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeShorts(short[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Short.BYTES);
			NativeUtils.shortsToNative(address + position, src, offset, elements, swapEndianess);
			addRequired();
		} else {
			super.writeShorts(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeInts(int[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Integer.BYTES);
			NativeUtils.intsToNative(address + position, src, offset, elements, swapEndianess);
			addRequired();
		} else {
			super.writeInts(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeLongs(long[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Long.BYTES);
			NativeUtils.longsToNative(address + position, src, offset, elements, swapEndianess);
			addRequired();
		} else {
			super.writeLongs(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeFloats(float[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Float.BYTES);
			NativeUtils.floatsToNative(address + position, src, offset, elements, swapEndianess);
			addRequired();
		} else {
			super.writeFloats(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeDoubles(double[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Double.BYTES);
			NativeUtils.doublesToNative(address + position, src, offset, elements, swapEndianess);
			addRequired();
		} else {
			super.writeDoubles(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeChars(char[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Character.BYTES);
			NativeUtils.charsToNative(address + position, src, offset, elements, swapEndianess);
			addRequired();
		} else {
			super.writeChars(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeBooleans(boolean[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Integer.BYTES);
			NativeUtils.booleansToNative(address + position, src, offset, elements, swapEndianess);
			addRequired();
		} else {
			super.writeBooleans(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeBooleansCompact(boolean[] src, int offset, int elements) {
	}

}
