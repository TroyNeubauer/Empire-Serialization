package com.troy.empireserialization.io.out;

import java.io.*;
import java.util.*;

import com.troy.empireserialization.*;
import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.util.*;

public class OutputStreamOutput extends AbstractOutput {

	private OutputStream out;
	private MasterMemoryBlock block;

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
			throw new EmpireSerializationIOException(e);
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
			throw new EmpireSerializationIOException(e);
		}
	}

	@Override
	public void flush() {
		try {
			out.flush();
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new EmpireSerializationIOException(e);
		}
	}

	@Override
	public void writeBytes(byte[] src, int offset, int bytes) {
		try {
			out.write(src, offset, bytes);
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new EmpireSerializationIOException(e);
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

	@Override
	public NativeMemoryBlock map(long bytes) {
		if (block == null)
			block = MasterMemoryBlock.allocate(Math.max(bytes, NativeUtils.DEFAULT_NATIVE_SIZE));
		else {
			block.require(bytes);
		}
		return block;
	}

	@Override
	public void unmap(NativeMemoryBlock block) {
		if(block.position() > Integer.MAX_VALUE) NativeUtils.throwByteIndexOutOfBounds();
		int size = (int) block.position();
		byte[] temp = ByteArrayPool.aquire(size);
		NativeUtils.nativeToBytes(temp, block.address(), 0, size, false);
		try {
			out.write(temp, 0, size);
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new EmpireSerializationIOException(e);
		} finally {
			ByteArrayPool.restore(temp);
		}
		block.setPosition(0);
	}



}
