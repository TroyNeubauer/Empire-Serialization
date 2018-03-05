package com.troy.serialization.io;

public abstract class AbstractInput implements Input {

	public abstract boolean hasBuffer();

	public abstract int getBufferPosition();

	public abstract byte[] getBuffer();

	public abstract void require(long bytes);

	@Override
	public byte readByte() {
		require(Byte.BYTES);
		return readByteImpl();
	}

	@Override
	public short readShort() {
		require(Short.BYTES);
		return (short) (readByteImpl() << 8 | readByteImpl());
	}

	@Override
	public int readInt() {
		require(Integer.BYTES);
		return (int) (readByteImpl() << 24 | readByteImpl() << 16 | readByteImpl() << 8 | readByteImpl());
	}

	@Override
	public long readLong() {
		require(Long.BYTES);
		return (long) (readByteImpl() << 56 | readByteImpl() << 48 | readByteImpl() << 40 | readByteImpl() << 32 | readByteImpl() << 24 | readByteImpl() << 16 | readByteImpl() << 8 | readByteImpl());
	}

	@Override
	public float readFloat() {
		require(Float.BYTES);
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public double readDouble() {
		require(Double.BYTES);
		return Double.longBitsToDouble(readLong());
	}

	@Override
	public char readChar() {
		require(Character.BYTES);
		return (char) (readByteImpl() << 8 | readByteImpl());
	}

	@Override
	public boolean readBoolean() {
		require(1);
		return readByteImpl() != 0;
	}

}
