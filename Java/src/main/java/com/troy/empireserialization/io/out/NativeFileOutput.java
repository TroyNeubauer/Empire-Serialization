package com.troy.empireserialization.io.out;

import static com.troy.empireserialization.util.NativeUtils.booleanToFWrite;
import static com.troy.empireserialization.util.NativeUtils.booleansToFWrite;
import static com.troy.empireserialization.util.NativeUtils.byteToFWrite;
import static com.troy.empireserialization.util.NativeUtils.bytesToFWrite;
import static com.troy.empireserialization.util.NativeUtils.charToFWrite;
import static com.troy.empireserialization.util.NativeUtils.charsToFWrite;
import static com.troy.empireserialization.util.NativeUtils.doubleToFWrite;
import static com.troy.empireserialization.util.NativeUtils.doublesToFWrite;
import static com.troy.empireserialization.util.NativeUtils.fclose;
import static com.troy.empireserialization.util.NativeUtils.fflush;
import static com.troy.empireserialization.util.NativeUtils.floatToFWrite;
import static com.troy.empireserialization.util.NativeUtils.floatsToFWrite;
import static com.troy.empireserialization.util.NativeUtils.fopen;
import static com.troy.empireserialization.util.NativeUtils.fputc;
import static com.troy.empireserialization.util.NativeUtils.intToFWrite;
import static com.troy.empireserialization.util.NativeUtils.intsToFWrite;
import static com.troy.empireserialization.util.NativeUtils.longToFWrite;
import static com.troy.empireserialization.util.NativeUtils.longsToFWrite;
import static com.troy.empireserialization.util.NativeUtils.nativeToFWrite;
import static com.troy.empireserialization.util.NativeUtils.shortToFWrite;
import static com.troy.empireserialization.util.NativeUtils.shortsToFWrite;

import java.io.File;
import java.nio.file.Path;

import com.troy.empireserialization.exception.EmpireSerializationIOException;
import com.troy.empireserialization.memory.MasterMemoryBlock;
import com.troy.empireserialization.memory.NativeMemoryBlock;
import com.troy.empireserialization.util.SerializationUtils;

public class NativeFileOutput extends AbstractNativeOutput<com.troy.empireserialization.io.out.NativeFileOutput.Deallocator> {

	static {
		SerializationUtils.init();
	}

	private MasterMemoryBlock block;


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
		byteToFWrite(fd, b);
	}

	@Override
	public void writeShort(short s) {
		shortToFWrite(fd, s, swapEndinessInNative());
	}

	@Override
	public void writeInt(int i) {
		intToFWrite(fd, i, swapEndinessInNative());
	}

	@Override
	public void writeLong(long l) {
		longToFWrite(fd, l, swapEndinessInNative());
	}

	@Override
	public void writeFloat(float f) {
		floatToFWrite(fd, f, swapEndinessInNative());
	}

	@Override
	public void writeDouble(double d) {
		doubleToFWrite(fd, d, swapEndinessInNative());
	}

	@Override
	public void writeChar(char c) {
		charToFWrite(fd, c, swapEndinessInNative());
	}
	
	@Override
	public void writeBoolean(boolean b) {
		booleanToFWrite(fd, b);
	}

	@Override
	public void flush() {
		fflush(fd);
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
		bytesToFWrite(fd, src, offset, bytes);
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
		booleansToFWrite(fd, src, offset, bytes);
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
