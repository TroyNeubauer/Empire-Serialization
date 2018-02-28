package com.troy.serialization.io;

import java.io.*;
import java.util.Objects;

import com.troy.serialization.exception.*;

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
		// Nop we don't need to tell the stream anything
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
	public void resetMappedOutputImpl(AbstractMappedIO out, long minSize) {
	}

	@Override
	public AbstractMappedIO newMappedOutput(long minSize) {
		return new AbstractMappedIO(0, 0, 0);
	}

	@Override
	public void unmapOutputImpl(AbstractMappedIO out, long numBytesWritten) {
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

}
