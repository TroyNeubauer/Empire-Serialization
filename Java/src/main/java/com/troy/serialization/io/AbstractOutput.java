package com.troy.serialization.io;

import java.nio.*;

import com.troy.serialization.exception.*;

public abstract class AbstractOutput implements Output {

	//Weather or not were writing in big endian
	protected boolean bigEndian = true;
	private boolean alreadyMapped = false;

	protected static final int NEXT_BYTE_VLE = 0b10000000, VLE_MASK = 0b01111111;

	public AbstractOutput() {
	}

	//format:off
	// System Big Endian 	| Writer BE 	| flip endianness in native code
	// 		0 					1				1
	// 		0 					0				0
	// 		1 					1				0
	// 		1 					0				1
	//This is Xor!						format:on

	/**
	 * 
	 * @return {@code true} if the endianness of the underlying system and the desired endianness are different, in which
	 *         case values written in native code need to be swapped. {@code false} otherwise
	 */
	protected boolean swapEndinessInNative() {
		return bigEndian ^ (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
	}

	@Override
	public void writeByte(int b) {
		require(Byte.BYTES);
		writeByteImpl((byte) b);
	}

	@Override
	public void writeByte(byte b) {
		require(Byte.BYTES);
		writeByteImpl(b);
	}

	@Override
	public void writeShort(short b) {
		require(Short.BYTES);
		if (bigEndian) {
			writeByteImpl((byte) ((b >> 8) & 0xFF));
			writeByteImpl((byte) ((b >> 0) & 0xFF));
		} else {
			writeByteImpl((byte) ((b >> 0) & 0xFF));
			writeByteImpl((byte) ((b >> 8) & 0xFF));
		}
	}

	@Override
	public void writeInt(int b) {
		require(Integer.BYTES);
		if (bigEndian) {
			writeByteImpl((byte) ((b >> 24) & 0xFF));
			writeByteImpl((byte) ((b >> 16) & 0xFF));
			writeByteImpl((byte) ((b >> 8) & 0xFF));
			writeByteImpl((byte) ((b >> 0) & 0xFF));
		} else {
			writeByteImpl((byte) ((b >> 0) & 0xFF));
			writeByteImpl((byte) ((b >> 8) & 0xFF));
			writeByteImpl((byte) ((b >> 16) & 0xFF));
			writeByteImpl((byte) ((b >> 24) & 0xFF));
		}
	}

	@Override
	public void writeLong(long b) {
		require(Long.BYTES);
		if (bigEndian) {
			writeByteImpl((byte) ((b >> 56) & 0xFF));
			writeByteImpl((byte) ((b >> 48) & 0xFF));
			writeByteImpl((byte) ((b >> 40) & 0xFF));
			writeByteImpl((byte) ((b >> 32) & 0xFF));
			writeByteImpl((byte) ((b >> 24) & 0xFF));
			writeByteImpl((byte) ((b >> 16) & 0xFF));
			writeByteImpl((byte) ((b >> 8) & 0xFF));
			writeByteImpl((byte) ((b >> 0) & 0xFF));
		} else {
			writeByteImpl((byte) ((b >> 0) & 0xFF));
			writeByteImpl((byte) ((b >> 8) & 0xFF));
			writeByteImpl((byte) ((b >> 16) & 0xFF));
			writeByteImpl((byte) ((b >> 24) & 0xFF));
			writeByteImpl((byte) ((b >> 32) & 0xFF));
			writeByteImpl((byte) ((b >> 40) & 0xFF));
			writeByteImpl((byte) ((b >> 48) & 0xFF));
			writeByteImpl((byte) ((b >> 56) & 0xFF));
		}
	}

	@Override
	public void writeUnsignedByte(short b) {
		if (b > 0xFFL || b < 0)
			throw new IllegalArgumentException("The value " + b + " is out of range for an unsigned byte");
		writeByte(b);
	}

	@Override
	public void writeUnsignedShort(int b) {
		if (b > 0xFFFF || b < 0)
			throw new IllegalArgumentException("The value " + b + " is out of range for an unsigned short");
		writeShort((short) b);
	}

	@Override
	public void writeUnsignedInt(long b) {
		if (b > 0xFFFFFFFFL || b < 0)
			throw new IllegalArgumentException("The value " + b + " is out of range for an unsigned int");
		writeInt((int) b);
	}

	@Override
	public void writeFloat(float b) {
		writeInt(Float.floatToRawIntBits(b));
	}

	@Override
	public void writeDouble(double b) {
		writeLong(Double.doubleToRawLongBits(b));
	}

	@Override
	public void writeChar(char b) {
		require(Character.BYTES);
		if (bigEndian) {
			writeByteImpl((byte) ((b >> 0) & 0xFF));
			writeByteImpl((byte) ((b >> 8) & 0xFF));
		} else {
			writeByteImpl((byte) ((b >> 8) & 0xFF));
			writeByteImpl((byte) ((b >> 0) & 0xFF));
		}
	}

	@Override
	public void writeBoolean(boolean b) {
		require(1);
		writeByteImpl((byte) (b ? 1 : 0));
	}

	@Override
	public void writeVLEShort(short s) {
		if (s >>> 7 == 0) {
			require(1);
			writeByteImpl((byte) s);
		} else if (s >>> 14 == 0) {
			require(2);
			writeByteImpl((byte) (s >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (s & VLE_MASK));
		} else {
			require(3);
			writeByteImpl((byte) (s >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (s >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (s & VLE_MASK));
		}
	}

	@Override
	public void writeVLEInt(int i) {
		if (i >>> 7 == 0) {
			require(1);
			writeByteImpl((byte) i);
		} else if (i >>> 14 == 0) {
			require(2);
			writeByteImpl((byte) (i >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (i & VLE_MASK));
		} else if (i >>> 21 == 0) {
			require(3);
			writeByteImpl((byte) (i >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (i >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (i & VLE_MASK));
		} else if (i >>> 28 == 0) {
			require(4);
			writeByteImpl((byte) (i >>> 21 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (i >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (i >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (i & VLE_MASK));
		} else {
			require(5);
			writeByteImpl((byte) (i >>> 28 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (i >>> 21 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (i >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (i >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (i & VLE_MASK));
		}
	}

	@Override
	public void writeVLELong(long l) {
		if (l >>> 7 == 0) {
			require(1);
			writeByteImpl((byte) l);
		} else if (l >>> 14 == 0) {
			require(2);
			writeByteImpl((byte) (l >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l & VLE_MASK));
		} else if (l >>> 21 == 0) {
			require(3);
			writeByteImpl((byte) (l >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l & VLE_MASK));
		} else if (l >>> 28 == 0) {
			require(4);
			writeByteImpl((byte) (l >>> 21 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l & VLE_MASK));
		} else if (l >>> 35 == 0) {
			require(5);
			writeByteImpl((byte) (l >>> 28 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 21 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l & VLE_MASK));
		} else if (l >>> 42 == 0) {
			require(6);
			writeByteImpl((byte) (l >>> 35 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 28 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 21 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l & VLE_MASK));
		} else if (l >>> 49 == 0) {
			require(7);
			writeByteImpl((byte) (l >>> 42 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 35 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 28 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 21 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l & VLE_MASK));
		} else if (l >>> 56 == 0) {
			require(8);
			writeByteImpl((byte) (l >>> 49 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 42 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 35 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 28 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 21 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l & VLE_MASK));
		} else {
			require(9);
			writeByteImpl((byte) (l >>> 56 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 49 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 42 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 35 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 28 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 21 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (l & VLE_MASK));
		}
	}

	@Override
	public void writeVLEChar(char c) {
		if (c >>> 7 == 0) {
			require(1);
			writeByteImpl((byte) c);
		} else if (c >>> 14 == 0) {
			require(2);
			writeByteImpl((byte) (c >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (c & VLE_MASK));
		} else {
			require(3);
			writeByteImpl((byte) (c >>> 14 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (c >>> 7 | NEXT_BYTE_VLE));
			writeByteImpl((byte) (c & VLE_MASK));
		}
	}

	@Override
	public void writeBytes(byte[] src, int offset, int elements) {
		require(elements * Byte.BYTES);
		for (int i = offset; i < offset + elements; i++) {
			writeByteImpl(src[i]);
		}
	}

	@Override
	public void writeShorts(short[] src, int offset, int elements) {
		require(elements * Short.BYTES);
		for (int i = offset; i < offset + elements; i++) {
			short b = src[i];
			if (bigEndian) {
				writeByteImpl((byte) ((b >> 8) & 0xFF));
				writeByteImpl((byte) ((b >> 0) & 0xFF));
			} else {
				writeByteImpl((byte) ((b >> 0) & 0xFF));
				writeByteImpl((byte) ((b >> 8) & 0xFF));
			}
		}
	}

	@Override
	public void writeInts(int[] src, int offset, int elements) {
		require(elements * Integer.BYTES);
		for (int i = offset; i < offset + elements; i++) {
			int b = src[i];
			if (bigEndian) {
				writeByteImpl((byte) ((b >> 24) & 0xFF));
				writeByteImpl((byte) ((b >> 16) & 0xFF));
				writeByteImpl((byte) ((b >> 8) & 0xFF));
				writeByteImpl((byte) ((b >> 0) & 0xFF));
			} else {
				writeByteImpl((byte) ((b >> 0) & 0xFF));
				writeByteImpl((byte) ((b >> 8) & 0xFF));
				writeByteImpl((byte) ((b >> 16) & 0xFF));
				writeByteImpl((byte) ((b >> 24) & 0xFF));
			}
		}
	}

	@Override
	public void writeLongs(long[] src, int offset, int elements) {
		require(elements * Long.BYTES);
		for (int i = offset; i < offset + elements; i++) {
			long b = src[i];
			if (bigEndian) {
				writeByteImpl((byte) ((b >> 56) & 0xFF));
				writeByteImpl((byte) ((b >> 48) & 0xFF));
				writeByteImpl((byte) ((b >> 40) & 0xFF));
				writeByteImpl((byte) ((b >> 32) & 0xFF));
				writeByteImpl((byte) ((b >> 24) & 0xFF));
				writeByteImpl((byte) ((b >> 16) & 0xFF));
				writeByteImpl((byte) ((b >> 8) & 0xFF));
				writeByteImpl((byte) ((b >> 0) & 0xFF));
			} else {
				writeByteImpl((byte) ((b >> 0) & 0xFF));
				writeByteImpl((byte) ((b >> 8) & 0xFF));
				writeByteImpl((byte) ((b >> 16) & 0xFF));
				writeByteImpl((byte) ((b >> 24) & 0xFF));
				writeByteImpl((byte) ((b >> 32) & 0xFF));
				writeByteImpl((byte) ((b >> 40) & 0xFF));
				writeByteImpl((byte) ((b >> 48) & 0xFF));
				writeByteImpl((byte) ((b >> 56) & 0xFF));
			}
		}
	}

	@Override
	public void writeFloats(float[] src, int offset, int elements) {

	}

	@Override
	public void writeDoubles(double[] src, int offset, int elements) {

	}

	@Override
	public void writeChars(char[] src, int offset, int elements) {

	}

	@Override
	public void writeBooleans(boolean[] src, int offset, int elements) {

	}

	@Override
	public void writeBooleansCompact(boolean[] src, int offset, int elements) {

	}

	@Override
	public void setByteOrder(ByteOrder byteOrder) {
		bigEndian = byteOrder == ByteOrder.BIG_ENDIAN;
	}

	@Override
	public ByteOrder getByteOrder() {
		return bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
	}

}
