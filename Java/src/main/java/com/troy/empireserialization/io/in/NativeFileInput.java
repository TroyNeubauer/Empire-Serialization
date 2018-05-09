package com.troy.empireserialization.io.in;

import static com.troy.empireserialization.util.NativeUtils.fclose;
import static com.troy.empireserialization.util.NativeUtils.fopen;

import java.io.File;
import java.nio.file.Path;

import com.troy.empireserialization.exception.EmpireSerializationIOException;
import com.troy.empireserialization.memory.MasterMemoryBlock;
import com.troy.empireserialization.memory.NativeMemoryBlock;
import com.troy.empireserialization.util.NativeUtils;
import com.troy.empireserialization.util.SerializationUtils;

public class NativeFileInput extends AbstractNativeInput<com.troy.empireserialization.io.in.NativeFileInput.Deallocator> {

	static {
		SerializationUtils.init();
	}

	private long fd;
	private MasterMemoryBlock block;

	public NativeFileInput(File file) {
		this(file.getAbsolutePath());
	}

	public NativeFileInput(Path file) {
		this(file.toString());
	}

	public NativeFileInput(String file) {
		fd = fopen(file, "rb");
		if (fd == 0)
			throw new EmpireSerializationIOException("Unable to open file \"" + file + "\" for writing");
		if (fd == SerializationUtils.OUT_OF_MEMORY)
			throw new OutOfMemoryError("Unable to open file " + file + " because the creating the name in native code failed");
		setDeallocator(new Deallocator(fd));
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

	@Override
	public byte readByteImpl() {
		return 0;
	}

	@Override
	public long remaining() {
		return 0;
	}

	@Override
	public void readBytes(byte[] dest, int offset, int count) {
		NativeUtils.fReadToBytes(fd, dest, offset, count);
	}
	

	@Override
	public void addRequired() {
		// TODO Auto-generated method stub

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
		//Nothing.
	}

	@Override
	public void require(long bytes) {
		//fread does everything for us
	}

}
