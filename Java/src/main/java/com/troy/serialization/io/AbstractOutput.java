package com.troy.serialization.io;

import java.nio.ByteOrder;

import com.troy.serialization.exception.AlreadyMappedException;
import com.troy.serialization.util.TroyStreamSettings;

public abstract class AbstractOutput implements Output {
	protected TroyStreamSettings settings;
	protected MappedIO mapped;
	protected boolean swapEndianess = ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN;

	protected static final int NEXT_BYTE_VLE = 0b10000000, VLE_MASK = 0b01111111;
	
	public AbstractOutput() {
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
		writeByteImpl((byte) ((b >> 8) & 0xFF));
		writeByteImpl((byte) ((b >> 0) & 0xFF));
	}

	@Override
	public void writeInt(int b) {
		require(Integer.BYTES);
		writeByteImpl((byte) ((b >> 24) & 0xFF));
		writeByteImpl((byte) ((b >> 16) & 0xFF));
		writeByteImpl((byte) ((b >> 8) & 0xFF));
		writeByteImpl((byte) ((b >> 0) & 0xFF));
	}

	@Override
	public void writeLong(long b) {
		require(Long.BYTES);
		writeByteImpl((byte) ((b >> 56) & 0xFF));
		writeByteImpl((byte) ((b >> 48) & 0xFF));
		writeByteImpl((byte) ((b >> 40) & 0xFF));
		writeByteImpl((byte) ((b >> 32) & 0xFF));
		writeByteImpl((byte) ((b >> 24) & 0xFF));
		writeByteImpl((byte) ((b >> 16) & 0xFF));
		writeByteImpl((byte) ((b >> 8) & 0xFF));
		writeByteImpl((byte) ((b >> 0) & 0xFF));
	}
	
	@Override
	public void writeUnsignedByte(short b) {
		if(b > 0xFFL || b < 0) throw new IllegalArgumentException("The value " + b + " is out of range for an unsigned byte");
		writeByte(b);
	}

	@Override
	public void writeUnsignedShort(int b) {
		if(b > 0xFFFFL || b < 0) throw new IllegalArgumentException("The value " + b + " is out of range for an unsigned short");
		writeShort((short) b);
	}

	@Override
	public void writeUnsignedInt(long b) {
		if(b > 0xFFFFFFFFL || b < 0) throw new IllegalArgumentException("The value " + b + " is out of range for an unsigned int");
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
		writeByteImpl((byte) ((b >> 8) & 0xFF));
		writeByteImpl((byte) ((b >> 0) & 0xFF));
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
			writeByteImpl((byte) (i >>> 14| NEXT_BYTE_VLE));
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
			writeByteImpl((byte) (l >>> 14| NEXT_BYTE_VLE));
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
	public void writeBytes(byte[] src, int offset, int bytes) {
		require(bytes);
		for(int i = offset; i < offset + bytes; i++) {
			writeByteImpl(src[i]);
		}
	}

	@Override
	public void writeShorts(short[] src, int offset, int elements) {
		
	}


	@Override
	public void writeInts(int[] src, int offset, int elements) {
		
	}


	@Override
	public void writeLongs(long[] src, int offset, int elements) {
		
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



	public void setSettings(TroyStreamSettings settings) {
		this.settings = settings;
	}

	/**
	 * Resets a mapped output so that it can be used again. 
	 * Also ensures that the native block of memory has at least minSize bytes remaining
	 * 
	 * @param out The mapped output to reset
	 * @param minSize The number of bytes
	 */
	public abstract void resetMappedOutputImpl(MappedIO out, long minSize);

	/**
	 * Called once (the first time the user calls {@link #mapOutput(long)}) to create a mapped object to use
	 * @param minSize The minimum number of bytes that the buffer must provide
	 * @return A new mapped IO to use in relation with this output
	 */
	public abstract MappedIO newMappedOutput(long minSize);

	/**
	 * Called when the user calls {@link #unmapOutput(MappedIO, long)} to commit all changes to the underlying output
	 * @param out The output to unmap
	 * @param numBytesWritten The number of bytes written to the mapped out since the caller received the mapped output object
	 */
	public abstract void unmapOutputImpl(MappedIO out, long numBytesWritten);

	@Override
	public MappedIO mapOutput(long bytes) {
		require(bytes);
		if (mapped == null) {
			mapped = newMappedOutput(bytes);
			return mapped;
		} else {
			if (mapped.offset == -1) {
				throw new AlreadyMappedException("Cannot map output " + this);
			}
			resetMappedOutputImpl(mapped, bytes);
			return mapped;
		}
	}

	@Override
	public void unmapOutput(MappedIO mappedOutput, long numBytesWritten) {
		// No need to require more bytes here because the buffer is already sized to the number of bytes the user
		// requested when un mapping and numBytesWritten *should* be less than that
		if (mappedOutput != mapped)
			throw new IllegalArgumentException("Mapped output is not current the mapped output for this output!");
		// Copy data
		unmapOutputImpl(mappedOutput, numBytesWritten);
		mappedOutput.offset = -1;
	}

	public TroyStreamSettings getSettings() {
		return settings;
	}

}
