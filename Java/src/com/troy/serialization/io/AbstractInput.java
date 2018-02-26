package com.troy.serialization.io;

import com.troy.serialization.charset.TroyCharset;

public abstract class AbstractInput implements Input {

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

	public abstract void require(long bytes);

	@Override
	public byte readByte() {

		return 0;
	}

	@Override
	public short readShort() {

		return 0;
	}

	@Override
	public int readInt() {

		return 0;
	}

	@Override
	public long readLong() {

		return 0;
	}

	@Override
	public float readFloat() {

		return 0;
	}

	@Override
	public double readDouble() {

		return 0;
	}

	@Override
	public char readChar() {

		return 0;
	}

	@Override
	public boolean readBoolean() {
		return false;
	}


}
