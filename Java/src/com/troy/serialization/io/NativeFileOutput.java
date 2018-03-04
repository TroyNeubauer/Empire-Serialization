package com.troy.serialization.io;

import java.io.File;
import java.nio.file.Path;

import com.troy.serialization.exception.TroySerializationIOException;
import com.troy.serialization.util.SerializationUtils;

public class NativeFileOutput extends AbstractNativeOutput {

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

	private class Deallocator implements Runnable {
		private long fd;

		public Deallocator(long fd) {
			this.fd = fd;
		}

		@Override
		public void run() {
			if (fd != 0) {
				fclose(fd);
				fd = 0;
			}
		}
	}

	public NativeFileOutput(String file) {
		fd = fopen(file, "wb");
		if (fd == 0)
			throw new TroySerializationIOException("Unable to open file \"" + file + "\" for writing");
		if (fd == SerializationUtils.OUT_OF_MEMORY)
			throw new OutOfMemoryError("Unable to open file " + file + " because the creating the name in native code failed");
		setDeallocator(new Deallocator(fd));
	}

	private static native long fopen(String file, String access);

	private static native void fclose(long fd);

	private static native void fflush(long fd);

	private static native int fputc(byte c, long fd);

	@Override
	public void writeByteImpl(byte b) {
		fputc(b, fd);
	}

	@Override
	public void flush() {
		fflush(fd);
	}

	@Override
	public void writeBytes(byte[] src, int offset, int bytes) {
	}

	@Override
	public boolean hasBuffer() {
		return false;
	}

	@Override
	public int getBufferPosition() {
		return 0;
	}

	@Override
	public byte[] getBuffer() {
		return null;
	}

	@Override
	public void require(long bytes) {
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
