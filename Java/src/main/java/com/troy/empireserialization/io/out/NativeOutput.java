package com.troy.empireserialization.io.out;

import com.troy.empireserialization.*;
import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.util.*;

import sun.misc.Unsafe;

public class NativeOutput extends AbstractNativeOutput<com.troy.empireserialization.io.out.NativeOutput.Deallocator> {

	static {
		SerializationUtils.init();
	}

	private static final Unsafe unsafe = MiscUtil.getUnsafe();
	private static final long DEFAULT_CAPACITY = 128;

	/**
	 * Provides the native implemenentation for this output
	 */
	private MasterMemoryBlock impl;
	private long requested;

	public NativeOutput() {
		this(DEFAULT_CAPACITY);
	}

	class Deallocator implements Runnable {

		public Deallocator() {
		}

		@Override
		public void run() {
			if (impl != null) {
				impl.free();
			}
		}
	}

	public NativeOutput(long capacity) {
		this.impl = MasterMemoryBlock.allocate(capacity);
		setDeallocator(new Deallocator());
	}

	@Override
	public void writeByteImpl(byte b) {
		unsafe.putByte(impl.address + impl.position++, b);
	}

	@Override
	public void flush() {
		// Nop
	}

	@Override
	public boolean hasBuffer() {
		return impl.position < Integer.MAX_VALUE;
	}

	@Override
	public int getBufferPosition() {
		if (impl.position >= Integer.MAX_VALUE)
			throw new NoBufferException();
		return (int) impl.position;
	}

	@Override
	public byte[] getBuffer() {
		throw new NoBufferException();
	}

	@Override
	public void require(long bytes) {
		impl.require(bytes);
		requested = bytes;
	}

	@Override
	public void addRequired() {
		impl.position += requested;
	}

	@Override
	public void writeBytes(byte[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Short.BYTES);
			NativeUtils.bytesToNative(impl.address + impl.position, src, offset, elements);
			addRequired();
		} else {
			super.writeBytes(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeShorts(short[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Short.BYTES);
			NativeUtils.shortsToNative(impl.address + impl.position, src, offset, elements, swapEndinessInNative());
			addRequired();
		} else {
			super.writeShorts(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeInts(int[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Integer.BYTES);
			NativeUtils.intsToNative(impl.address + impl.position, src, offset, elements, swapEndinessInNative());
			addRequired();
		} else {
			super.writeInts(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeLongs(long[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Long.BYTES);
			NativeUtils.longsToNative(impl.address + impl.position, src, offset, elements, swapEndinessInNative());
			addRequired();
		} else {
			super.writeLongs(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeFloats(float[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Float.BYTES);
			NativeUtils.floatsToNative(impl.address + impl.position, src, offset, elements, swapEndinessInNative());
			addRequired();
		} else {
			super.writeFloats(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeDoubles(double[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Double.BYTES);
			NativeUtils.doublesToNative(impl.address + impl.position, src, offset, elements, swapEndinessInNative());
			addRequired();
		} else {
			super.writeDoubles(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeChars(char[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Character.BYTES);
			NativeUtils.charsToNative(impl.address + impl.position, src, offset, elements, swapEndinessInNative());
			addRequired();
		} else {
			super.writeChars(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeBooleans(boolean[] src, int offset, int elements) {
		if (NativeUtils.NATIVES_ENABLED) {
			require(elements * Integer.BYTES);
			NativeUtils.booleansToNative(impl.address + impl.position, src, offset, elements);
			addRequired();
		} else {
			super.writeBooleans(src, offset, elements);// The superclass increments position so we're ok without addRequired();
		}
	}

	@Override
	public void writeBooleansCompact(boolean[] src, int offset, int elements) {
		super.writeBooleansCompact(src, offset, elements);// The superclass increments position so we're ok without addRequired();
	}

	@Override
	public NativeMemoryBlock map(long bytes) {
		return impl.subset(impl.position, bytes);
	}

	@Override
	public void unmap(NativeMemoryBlock block) {
		// No copying needed since the block that we "mapped" was just a portion of the greater block.
		// Only add to position
		impl.position += block.position();
	}
	
	/**
	 * Returns the address of the buffer used by this out
	 */
	public long address() {
		return impl.address();
	}

}
