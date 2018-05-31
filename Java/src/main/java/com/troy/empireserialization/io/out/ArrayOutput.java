package com.troy.empireserialization.io.out;

/**
 * An interface that allows writing all primitive array types to an output. 
 * @author Troy Neubauer
 *
 */
public interface ArrayOutput {

	public void writeBytes(byte[] src, int offset, int elements);

	public void writeShorts(short[] src, int offset, int elements);

	public void writeInts(int[] src, int offset, int elements);

	public void writeLongs(long[] src, int offset, int elements);

	public void writeFloats(float[] src, int offset, int elements);

	public void writeDoubles(double[] src, int offset, int elements);

	public void writeChars(char[] src, int offset, int elements);

	public void writeBooleans(boolean[] src, int offset, int elements);

	public void writeBooleansCompact(boolean[] src, int offset, int elements);
	
	
	
	
	public void writeBytes(Byte[] src, int offset, int elements);

	public void writeShorts(Short[] src, int offset, int elements);

	public void writeInts(Integer[] src, int offset, int elements);

	public void writeLongs(Long[] src, int offset, int elements);

	public void writeFloats(Float[] src, int offset, int elements);

	public void writeDoubles(Double[] src, int offset, int elements);

	public void writeChars(Character[] src, int offset, int elements);

	public void writeBooleans(Boolean[] src, int offset, int elements);

	public void writeBooleansCompact(Boolean[] src, int offset, int elements);
	

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


	public default void writeBytes(Byte[] src) {
		writeBytes(src, 0, src.length);
	}

	public default void writeShorts(Short[] src) {
		writeShorts(src, 0, src.length);
	}

	public default void writeInts(Integer[] src) {
		writeInts(src, 0, src.length);
	}

	public default void writeLongs(Long[] src) {
		writeLongs(src, 0, src.length);
	}

	public default void writeFloats(Float[] src) {
		writeFloats(src, 0, src.length);
	}

	public default void writeDoubles(Double[] src) {
		writeDoubles(src, 0, src.length);
	}

	public default void writeChars(Character[] src) {
		writeChars(src, 0, src.length);
	}

	public default void writeBooleans(Boolean[] src) {
		writeBooleans(src, 0, src.length);
	}

	public default void writeBooleansCompact(Boolean[] src) {
		writeBooleansCompact(src, 0, src.length);
	}

}
