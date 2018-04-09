package com.troy.empireserialization.io.out;

import java.io.File;
import java.nio.file.Path;

import com.troy.empireserialization.*;
import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.util.*;

import static com.troy.empireserialization.util.NativeUtils.*;

public class NativeFileOutput extends AbstractNativeOutput<com.troy.empireserialization.io.out.NativeFileOutput.Deallocator> {

	private MasterMemoryBlock block;

	static {
		SerializationUtils.init();
	}

	private long fd;

	public NativeFileOutput(File file) {
		this(file.getPath());
	}

	public NativeFileOutput(Path path) {
		this(path.toAbsolutePath().toString());
	}

	class Deallocator implements Runnable {
		private long fd;
		private MasterMemoryBlock block;

		public Deallocator(long fd) {
			this.fd = fd;
		}

		@Override
		public void run() {
			if (fd != 0) {
				fclose(fd);
				fd = 0;
			}
			if (block != null) {
				block.free();
				block = null;
			}
		}
	}

	public NativeFileOutput(String file) {
		fd = fopen(file, "wb");
		if (fd == 0)
			throw new EmpireSerializationIOException("Unable to open file \"" + file + "\" for writing");
		if (fd == SerializationUtils.OUT_OF_MEMORY)
			throw new OutOfMemoryError("Unable to open file " + file + " because the creating the name in native code failed");
		setDeallocator(new Deallocator(fd));
	}

	@Override
	public void writeByteImpl(byte b) {
		fputc(b, fd);
	}

	@Override
	public void writeByte(byte b) {
		require(Byte.BYTES);
		byteToFWrite(fd, b, false);
		addRequired();
	}

	@Override
	public void writeShort(short s) {
		require(Short.BYTES);
		shortToFWrite(fd, s, swapEndinessInNative());
		addRequired();
	}

	@Override
	public void writeInt(int i) {
		require(Integer.BYTES);
		intToFWrite(fd, i, swapEndinessInNative());
		addRequired();
	}

	@Override
	public void writeLong(long l) {
		require(Long.BYTES);
		longToFWrite(fd, l, swapEndinessInNative());
		addRequired();
	}

	@Override
	public void writeFloat(float f) {
		require(Float.BYTES);
		floatToFWrite(fd, f, swapEndinessInNative());
		addRequired();
	}

	@Override
	public void writeDouble(double d) {
		require(Double.BYTES);
		doubleToFWrite(fd, d, swapEndinessInNative());
		addRequired();
	}

	@Override
	public void writeChar(char c) {
		require(Character.BYTES);
		charToFWrite(fd, c, swapEndinessInNative());
		addRequired();
	}
	
	@Override
	public void writeBoolean(boolean b) {
		require(1);
		booleanToFWrite(fd, b, false);
		addRequired();
	}

	@Override
	public void flush() {
		fflush(fd);
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
		// fwrite does everything for us so we don't need to insure anything
	}

	@Override
	public void addRequired() {
		// Do nothing
	}

	@Override
	public void writeBytes(byte[] src, int offset, int bytes) {
		bytesToFWrite(fd, src, offset, bytes, swapEndinessInNative());
	}

	@Override
	public void writeShorts(short[] src, int offset, int bytes) {
		shortsToFWrite(fd, src, offset, bytes, swapEndinessInNative());
	}

	@Override
	public void writeInts(int[] src, int offset, int bytes) {
		intsToFWrite(fd, src, offset, bytes, swapEndinessInNative());
	}

	@Override
	public void writeLongs(long[] src, int offset, int bytes) {
		longsToFWrite(fd, src, offset, bytes, swapEndinessInNative());
	}

	@Override
	public void writeFloats(float[] src, int offset, int bytes) {
		floatsToFWrite(fd, src, offset, bytes, swapEndinessInNative());
	}

	@Override
	public void writeDoubles(double[] src, int offset, int bytes) {
		doublesToFWrite(fd, src, offset, bytes, swapEndinessInNative());
	}

	@Override
	public void writeChars(char[] src, int offset, int bytes) {
		charsToFWrite(fd, src, offset, bytes, swapEndinessInNative());
	}

	@Override
	public void writeBooleans(boolean[] src, int offset, int bytes) {
		booleansToFWrite(fd, src, offset, bytes, swapEndinessInNative());
	}

	@Override
	public void writeBooleansCompact(boolean[] src, int offset, int bytes) {
	}

	@Override
	public NativeMemoryBlock map(long bytes) {
		if (block == null) {
			block = MasterMemoryBlock.allocate(bytes);
			getDeallocator().block = this.block;
		} else {
			// Reset it
			block.setPosition(0);
			block.require(bytes);
		}
		return block;
	}

	@Override
	public void unmap(NativeMemoryBlock block) {
		nativeToFWrite(fd, block.address(), block.position());
	}

}
