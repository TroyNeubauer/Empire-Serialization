package com.troy.empireserialization.io.in;

/**
 * An interface that allows writing all primitive array types to an output.
 * 
 * @author Troy Neubauer
 *
 */
public interface ArrayInput {

	public void readBytes(byte[] src, int offset, int elements);

	public void readShorts(short[] src, int offset, int elements);

	public void readInts(int[] src, int offset, int elements);

	public void readLongs(long[] src, int offset, int elements);

	public void readFloats(float[] src, int offset, int elements);

	public void readDoubles(double[] src, int offset, int elements);

	public void readChars(char[] src, int offset, int elements);

	public void readBooleans(boolean[] src, int offset, int elements);

	public void readBooleansCompact(boolean[] src, int offset, int elements);

	public default void readBytes(byte[] src) {
		readBytes(src, 0, src.length);
	}

	public default void readShorts(short[] src) {
		readShorts(src, 0, src.length);
	}

	public default void readInts(int[] src) {
		readInts(src, 0, src.length);
	}

	public default void readLongs(long[] src) {
		readLongs(src, 0, src.length);
	}

	public default void readFloats(float[] src) {
		readFloats(src, 0, src.length);
	}

	public default void readDoubles(double[] src) {
		readDoubles(src, 0, src.length);
	}

	public default void readChars(char[] src) {
		readChars(src, 0, src.length);
	}

	public default void readBooleans(boolean[] src) {
		readBooleans(src, 0, src.length);
	}

	public default void readBooleansCompact(boolean[] src) {
		readBooleansCompact(src, 0, src.length);
	}

}
