package com.troy.serialization.io;

import java.util.Arrays;

import com.troy.serialization.exception.*;

public class ByteArrayOutput extends AbstractOutput {

	private static final int NATIVE_ARRAY_COPY_THRESH_HOLD = 16;

	private byte[] buffer;
	private byte position;

	public ByteArrayOutput() {
		this(20);// A Hotspot array header is 12 bytes so 20 + 12 = 32 therefore no bytes are wasted due to multiple of eight packing
	}

	public ByteArrayOutput(int initalSize) {
		if (initalSize <= 0)
			throw new IllegalArgumentException("Inital size cannot be <= 0!");
		this.buffer = new byte[initalSize];
	}

	@Override
	public void writeByteImpl(byte b) {
		try {
			buffer[position++] = b;
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		}
	}

	@Override
	public boolean hasBuffer() {
		return true;
	}

	@Override
	public int getBufferPosition() {
		return position;
	}

	@Override
	public byte[] getBuffer() {
		return buffer;
	}

	@Override
	public void require(long bytes) {
		try {
			if (position + bytes > buffer.length) {
				buffer = Arrays.copyOf(buffer, (int) (buffer.length * 2.5 + 1));
			}
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		}
	}

	@Override
	public void close() {
		buffer = null;// Help GC
		position = -1;
	}

	@Override
	public void flush() {
		// Nop nothing to flush
	}

	@Override
	public void resetMappedOutputImpl(AbstractMappedIO out, long minSize) {
		// We don't need to worry about minSize because require already ensured that we have enough space in the buffer
		
		out.offset = position;
	}

	@Override
	public AbstractMappedIO newMappedOutput(long minSize) {
		
		return new AbstractMappedIO(0, 0, buffer.length);
	}

	@Override
	public void unmapOutputImpl(AbstractMappedIO out, long numBytesWritten) {
		if (NATIVE_ARRAY_COPY_THRESH_HOLD > numBytesWritten) {

		} else {

		}
	}

}
