package com.troy.serialization.io.out;

import java.io.File;
import java.nio.file.Path;

import com.troy.serialization.*;
import com.troy.serialization.exception.NoBufferException;
import com.troy.serialization.exception.TroySerializationIOException;
import com.troy.serialization.util.*;

public class NativeFileOutput extends AbstractNativeOutput<com.troy.serialization.io.out.NativeFileOutput.Deallocator> {

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
				NativeUtils.fclose(fd);
				fd = 0;
			}
			if (block != null) {
				block.free();
				block = null;
			}
		}
	}

	public NativeFileOutput(String file) {
		fd = NativeUtils.fopen(file, "wb");
		if (fd == 0)
			throw new TroySerializationIOException("Unable to open file \"" + file + "\" for writing");
		if (fd == SerializationUtils.OUT_OF_MEMORY)
			throw new OutOfMemoryError("Unable to open file " + file + " because the creating the name in native code failed");
		setDeallocator(new Deallocator(fd));
	}

	@Override
	public void writeByteImpl(byte b) {
		NativeUtils.fputc(b, fd);
	}

	@Override
	public void flush() {
		NativeUtils.fflush(fd);
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
		NativeUtils.bytesToFWrite(fd, src, offset, bytes, bigEndian);
	}

	@Override
	public void writeShorts(short[] src, int offset, int bytes) {
		NativeUtils.shortsToFWrite(fd, src, offset, bytes, bigEndian);
	}

	@Override
	public void writeInts(int[] src, int offset, int bytes) {
		NativeUtils.intsToFWrite(fd, src, offset, bytes, bigEndian);
	}

	@Override
	public void writeLongs(long[] src, int offset, int bytes) {
		NativeUtils.longsToFWrite(fd, src, offset, bytes, bigEndian);
	}

	@Override
	public void writeFloats(float[] src, int offset, int bytes) {
		NativeUtils.floatsToFWrite(fd, src, offset, bytes, bigEndian);
	}

	@Override
	public void writeDoubles(double[] src, int offset, int bytes) {
		NativeUtils.doublesToFWrite(fd, src, offset, bytes, bigEndian);
	}

	@Override
	public void writeChars(char[] src, int offset, int bytes) {
		NativeUtils.charsToFWrite(fd, src, offset, bytes, bigEndian);
	}

	@Override
	public void writeBooleans(boolean[] src, int offset, int bytes) {
		NativeUtils.booleansToFWrite(fd, src, offset, bytes, bigEndian);

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
		NativeUtils.nativeToFWrite(fd, block.address(), block.position());
	}

}
