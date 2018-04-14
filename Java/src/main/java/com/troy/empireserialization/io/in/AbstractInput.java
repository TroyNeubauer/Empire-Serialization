package com.troy.empireserialization.io.in;

import java.nio.*;

import com.troy.empireserialization.EmpireConstants;
import com.troy.empireserialization.util.*;

public abstract class AbstractInput implements Input {

	private boolean bigEndian = true;

	public abstract boolean hasBuffer();

	public abstract int getBufferPosition();

	public abstract byte[] getBuffer();

	public abstract void require(long bytes);

	@Override
	public void setByteOrder(ByteOrder byteOrder) {
		bigEndian = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) ^ (byteOrder == ByteOrder.BIG_ENDIAN);
	}

	@Override
	public ByteOrder getByteOrder() {
		return bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
	}

	@Override
	public byte readByte() {
		require(Byte.BYTES);
		return readByteImpl();
	}

	@Override
	public short readShort() {
		require(Short.BYTES);
		if (bigEndian) {
			return (short) (readByteImpl() << 8 | readByteImpl());
		} else {
			return (short) (readByteImpl() | readByteImpl() << 8);
		}
	}

	@Override
	public int readInt() {
		require(Integer.BYTES);
		if (bigEndian) {
			return (int) (readByteImpl() << 24 | readByteImpl() << 16 | readByteImpl() << 8 | readByteImpl());
		} else {
			return (int) (readByteImpl() | readByteImpl() << 8 | readByteImpl() << 16 | readByteImpl() << 24);
		}
	}

	@Override
	public long readLong() {
		require(Long.BYTES);
		if (bigEndian) {
			return (long) (readByteImpl() << 56 | readByteImpl() << 48 | readByteImpl() << 40 | readByteImpl() << 32
					| readByteImpl() << 24 | readByteImpl() << 16 | readByteImpl() << 8 | readByteImpl());
		} else {
			return (long) (readByteImpl() | readByteImpl() << 8 | readByteImpl() << 16 | readByteImpl() << 24
					| readByteImpl() << 32 | readByteImpl() << 40 | readByteImpl() << 48 | readByteImpl() << 56);
		}
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
		if (bigEndian) {
			return (char) (readByteImpl() << 8 | readByteImpl());
		} else {
			return (char) (readByteImpl() | readByteImpl() << 8);
		}
	}

	@Override
	public short readVLEShort() {
		int b = readByte();
		int result = b & EmpireConstants.VLE_MASK;
		if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
			b = readByte();
			result |= (b & EmpireConstants.VLE_MASK) << 7;
			if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
				b = readByte();
				result |= (b & EmpireConstants.VLE_MASK) << 14;
			}
		}
		return (short) result;
	}

	@Override
	public int readVLEInt() {
		int b = readByte();
		int result = b & EmpireConstants.VLE_MASK;
		if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
			b = readByte();
			result |= (b & EmpireConstants.VLE_MASK) << 7;
			if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
				b = readByte();
				result |= (b & EmpireConstants.VLE_MASK) << 14;
				if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
					b = readByte();
					result |= (b & EmpireConstants.VLE_MASK) << 21;
					if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
						b = readByte();
						result |= (b & EmpireConstants.VLE_MASK) << 28;
					}
				}
			}
		}
		return result;
	}

	@Override
	public long readVLELong() {
		long b = readByte();
		long result = b & EmpireConstants.VLE_MASK;
		if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
			b = readByte();
			result |= (b & EmpireConstants.VLE_MASK) << 7;
			if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
				b = readByte();
				result |= (b & EmpireConstants.VLE_MASK) << 14;
				if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
					b = readByte();
					result |= (b & EmpireConstants.VLE_MASK) << 21;
					if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
						b = readByte();
						result |= (b & EmpireConstants.VLE_MASK) << 28;
						if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
							b = readByte();
							result |= (b & EmpireConstants.VLE_MASK) << 35;
							if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
								b = readByte();
								result |= (b & EmpireConstants.VLE_MASK) << 42;
								if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
									b = readByte();
									result |= (b & EmpireConstants.VLE_MASK) << 49;
									if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
										b = readByte();
										result |= (b & EmpireConstants.VLE_MASK) << 56;
										if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
											b = readByte();
											result |= (b & EmpireConstants.VLE_MASK) << 63;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	@Override
	public char readVLEChar() {
		int b = readByte();
		int result = b & EmpireConstants.VLE_MASK;
		if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
			b = readByte();
			result |= (b & EmpireConstants.VLE_MASK) << 7;
			if ((b & EmpireConstants.NEXT_BYTE_VLE) != 0) {
				b = readByte();
				result |= (b & EmpireConstants.VLE_MASK) << 14;
			}
		}
		return (char) result;
	}

	@Override
	public boolean readBoolean() {
		require(1);
		return readByteImpl() != 0;
	}

}
