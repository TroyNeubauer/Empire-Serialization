package com.troy.serialization.io;

import com.troy.serialization.exception.AlreadyMappedException;
import com.troy.serialization.util.TroyStreamSettings;

public abstract class AbstractOutput implements Output {
	private TroyStreamSettings settings;
	private AbstractMappedIO out;

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
	public void writeVLEShort(short b) {
	}

	@Override
	public void writeVLEInt(int b) {
	}

	@Override
	public void writeVLELong(long b) {
	}

	@Override
	public void writeVLEChar(char b) {
	}

	public void setSettings(TroyStreamSettings settings) {
		this.settings = settings;
	}

	/**
	 * Resets a mapped output so that it can be used again
	 * @param out
	 */
	public abstract void resetMappedOutputImpl(AbstractMappedIO out, long minSize);
	
	public abstract AbstractMappedIO newMappedOutput(long minSize);
	
	public abstract void unmapOutputImpl(AbstractMappedIO out, long numBytesWritten);
	
	@Override
	public AbstractMappedIO mapOutput(long bytes) {
		require(bytes);
		if (out == null) {
			out = newMappedOutput(bytes);
			return out;
		} else {
			if (out.offset == -1) {
				throw new AlreadyMappedException("Cannot map output " + this);
			}
			resetMappedOutputImpl(out, bytes);
			return out;
		}
	}

	@Override
	public void unmapOutput(AbstractMappedIO mappedOutput, long numBytesWritten) {
		//No need to require more bytes here because the buffer is already sized to the number of bytes the user 
		//requested when un mapping and numBytesWritten *should* be less than that
		if (mappedOutput != out)
			throw new IllegalArgumentException("Mapped output is not current the mapped output for this output!");
		//Copy data
		unmapOutputImpl(mappedOutput, numBytesWritten);
		mappedOutput.offset = -1;
	}

	public TroyStreamSettings getSettings() {
		return settings;
	}

}
