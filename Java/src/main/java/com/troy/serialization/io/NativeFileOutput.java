package com.troy.serialization.io;

import java.io.File;
import java.nio.file.Path;

import com.troy.serialization.exception.NoBufferException;
import com.troy.serialization.exception.TroySerializationIOException;
import com.troy.serialization.util.NativeUtils;
import com.troy.serialization.util.SerializationUtils;

public class NativeFileOutput extends AbstractNativeOutput<com.troy.serialization.io.NativeFileOutput.Deallocator> {

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
			throw new OutOfMemoryError(
					"Unable to open file " + file + " because the creating the name in native code failed");
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
		//fwrite does everything for us so we don't need to insure anything
	}
	
	@Override
	public void addRequired() {
		//Do nothing
	}

	@Override
	public void resetMappedOutputImpl(MappedIO out, long minSize) {
	}

	@Override
	public MappedIO newMappedOutput(long minSize) {
		return null;
	}

	@Override
	public void unmapOutputImpl(MappedIO out, long numBytesWritten) {
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

}
