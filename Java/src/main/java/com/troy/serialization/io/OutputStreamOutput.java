package com.troy.serialization.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import com.troy.serialization.exception.AlreadyClosedException;
import com.troy.serialization.exception.NoBufferException;
import com.troy.serialization.exception.TroySerializationIOException;
import com.troy.serialization.util.MiscUtil;

import sun.misc.Unsafe;

public class OutputStreamOutput extends AbstractOutput {

	private OutputStream out;

	public OutputStreamOutput(OutputStream out) {
		this.out = Objects.requireNonNull(out);
	}

	@Override
	public void writeByteImpl(byte b) {
		try {
			out.write(b);
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new TroySerializationIOException(e);
		}
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

	@Override
	public void require(long bytes) {
		// Nop
	}
	
	@Override
	public void addRequired() {
		// Nop
	}

	@Override
	public void close() {
		try {
			out.close();
			out = null;
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new TroySerializationIOException(e);
		}
	}

	@Override
	public void flush() {
		try {
			out.flush();
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new TroySerializationIOException(e);
		}
	}

	@Override
	public void resetMappedOutputImpl(MappedIO out, long minSize) {
		out.offset = 0;
		if (out.capacity < minSize) {
			Unsafe unsafe = MiscUtil.getUnsafe();
			if (out.address != 0)
				unsafe.freeMemory(out.address);
			out.address = unsafe.allocateMemory(minSize);
			out.capacity = minSize;
		}
	}

	@Override
	public MappedIO newMappedOutput(long minSize) {
		long address = MiscUtil.getUnsafe().allocateMemory(minSize);
		return new MappedIO(address, 0, minSize) {

			@Override
			public void require(long bytes) {
				
			}
			
		};
	}

	@Override
	public void unmapOutputImpl(MappedIO out, long numBytesWritten) {
	}

	@Override
	public void writeBytes(byte[] src, int offset, int bytes) {
		try {
			out.write(src, offset, bytes);
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new TroySerializationIOException(e);
		}
	}
	
	@Override
	public void writeShorts(short[] src, int offset, int bytes) {
	}

	@Override
	public void writeInts(int[] src, int offset, int bytes) {
	}

	@Override
	public void writeLongs(long[] src, int offset, int bytes) {
	}

	@Override
	public void writeFloats(float[] src, int offset, int bytes) {
	}

	@Override
	public void writeDoubles(double[] src, int offset, int bytes) {
	}

	@Override
	public void writeChars(char[] src, int offset, int bytes) {
	}

	@Override
	public void writeBooleans(boolean[] src, int offset, int bytes) {
	}

	@Override
	public void writeBooleansCompact(boolean[] src, int offset, int bytes) {
	}



}
