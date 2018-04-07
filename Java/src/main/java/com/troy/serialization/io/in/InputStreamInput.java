package com.troy.serialization.io.in;

import java.io.*;
import java.util.*;

import com.troy.serialization.*;
import com.troy.serialization.exception.*;
import com.troy.serialization.util.*;

public class InputStreamInput extends AbstractInput {

	private InputStream in;

	public InputStreamInput(InputStream in) {
		this.in = Objects.requireNonNull(in);
	}

	@Override
	public byte readByteImpl() {
		try {
			return (byte) in.read();
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new TroySerializationIOException(e);
		}
	}

	@Override
	public void addRequired() {
		// Nothing because java.io.InputStream.read handles everything for us
	}

	@Override
	public void close() {
		try {
			in.close();
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new TroySerializationIOException(e);
		}
		in = null;
	}

	@Override
	public NativeMemoryBlock map(long bytes) {
		if(bytes > Integer.MAX_VALUE) NativeUtils.throwByteIndexOutOfBounds();
		return null;
	}

	@Override
	public void unmap(NativeMemoryBlock block) {
		block.setPosition(0);
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
		// Nothing because java.io.InputStream.read handles everything for us
	}

}
