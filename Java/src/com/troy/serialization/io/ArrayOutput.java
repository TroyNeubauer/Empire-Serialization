package com.troy.serialization.io;

public interface ArrayOutput {

	public void writeBytes(byte[] src, int offset, int bytes);

	public void writeShorts(short[] src, int offset, int bytes);

	public void writeInts(int[] src, int offset, int bytes);

	public void writeLongs(long[] src, int offset, int bytes);

	public void writeFloats(float[] src, int offset, int bytes);

	public void writeDoubles(double[] src, int offset, int bytes);

	public void writeChars(char[] src, int offset, int bytes);

	public void writeBooleans(boolean[] src, int offset, int bytes);

	public void writeBooleansCompact(boolean[] src, int offset, int bytes);

	public default void writeBytes(byte[] src) {
		writeBytes(src, 0, src.length);
	}

	public default void writeShorts(short[] src) {
		writeShorts(src, 0, src.length);
	}

	public default void writeInts(int[] src) {
		writeInts(src, 0, src.length);
	}

	public default void writeLongs(long[] src) {
		writeLongs(src, 0, src.length);
	}

	public default void writeFloats(float[] src) {
		writeFloats(src, 0, src.length);
	}

	public default void writeDoubles(double[] src) {
		writeDoubles(src, 0, src.length);
	}

	public default void writeChars(char[] src) {
		writeChars(src, 0, src.length);
	}

	public default void writeBooleans(boolean[] src) {
		writeBooleans(src, 0, src.length);
	}

	public default void writeBooleansCompact(boolean[] src) {
		writeBooleansCompact(src, 0, src.length);
	}
}
