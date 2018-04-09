package com.troy.empireserialization.charset;

import com.troy.empireserialization.*;
import com.troy.empireserialization.io.in.*;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.util.*;

public interface EmpireCharset {
	/**
	 * Decodes bytes from a source byte array to a char array
	 * 
	 * @param src
	 *            The source of encoded bytes to draw from
	 * @param dest
	 *            The resulting array to write to
	 * @param destOffset
	 *            The offset on where to start writing to into dest
	 * @param chars
	 *            The number of characters to decode
	 */
	public void decode(Input src, final char[] dest, int destOffset, final int chars);

	/**
	 * Encodes bytes from a char array into an output
	 * 
	 * @param src
	 *            The source of characters to draw from
	 * @param dest
	 *            The destination to write to
	 * @param srcOffset
	 *            The offset to start reading from in the source array
	 * @param chars
	 *            The number of characters to encode
	 */
	public default void encode(final char[] src, Output dest, int srcOffset, final int chars, int info) {
		if (NativeUtils.NATIVES_ENABLED && chars > NativeUtils.MIN_NATIVE_THRESHOLD) {
			long size = (long) (chars / getMinCharactersPerByte() + 1);
			dest.require(size);
			NativeMemoryBlock block = dest.map(size);
			int bytesEncoded = nEncodeImpl(src, block.address(), srcOffset, chars, info);
			block.setPosition(bytesEncoded);
			dest.unmap(block);
		} else {
			encodeImpl(src, dest, srcOffset, chars, info);
		}
	}
	
	/**
	 * Encodes a subset of the given character array to the desired output
	 * @see #encode(char[], Output, int, int)
	 */
	public void encodeImpl(final char[] src, Output dest, int srcOffset, final int chars, int info);

	/**
	 * Encodes a subset of the given character array to the desired output using a native alternative
	 * @see #encode(char[], Output, int, int)
	 */
	public abstract int nEncodeImpl(char[] src, long dest, int srcOffset, int chars, int info);

	public char[] getDecodingCache();

	public int[] getEncodingCache();

	public float getMinCharactersPerByte();

	/*
	 * public int encodeNative(final char[] src, Output dest, int srcOffset, final int chars, boolean checkForErrors) {
	 * NativeM
	 * 
	 * return 0; }
	 */

	public byte getOpCode();

	default public float getBytesPerCharacter() {
		return 1.0f / getMinCharactersPerByte();
	}

	public int getCharsetCode();

	public String name();
}
