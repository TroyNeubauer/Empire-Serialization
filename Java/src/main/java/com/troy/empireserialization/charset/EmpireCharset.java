package com.troy.empireserialization.charset;

import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.out.Output;
import com.troy.empireserialization.memory.NativeMemoryBlock;
import com.troy.empireserialization.util.NativeUtils;

public interface EmpireCharset {
	/**
	 * Decodes bytes from a source byte array to a char array
	 * 
	 * @param src
	 *            The source of encoded bytes to draw from
	 * @param dest
	 *            The resulting array to write to
	 * @param srcOffset
	 *            The offset on where to start writing to into dest
	 * @param chars
	 *            The number of characters to decode
	 * @return The number of bytes read from src
	 */
	public default long decode(Input src, final char[] dest, int srcOffset, final int chars) {
		if (NativeUtils.NATIVES_ENABLED && chars > NativeUtils.MIN_NATIVE_THRESHOLD) {
			long size = (long) Math.ceil((float) chars / getMinCharactersPerByte());
			NativeMemoryBlock block = src.map(size);
			long bytesEncoded = nDecodeImpl(dest, src, srcOffset, chars);
			block.setPosition(bytesEncoded);
			src.unmap(block);
			return bytesEncoded;
		} else {
			return decodeImpl(dest, src, srcOffset, chars);
		}
	}

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
	 * @return The number of bytes written to dest
	 */
	public default long encode(final char[] src, Output dest, int srcOffset, final int chars, int info) {
		if (NativeUtils.NATIVES_ENABLED && chars > NativeUtils.MIN_NATIVE_THRESHOLD) {
			long size = (long) (chars / getMinCharactersPerByte() + 1);
			dest.require(size);
			NativeMemoryBlock block = dest.map(size);
			int bytesEncoded = nEncodeImpl(src, block.address(), srcOffset, chars, info);
			block.setPosition(bytesEncoded);
			dest.unmap(block);
			return bytesEncoded;
		} else {
			return encodeImpl(src, dest, srcOffset, chars, info);
		}
	}

	/**
	 * Encodes a subset of the given character array to the desired output
	 * 
	 * @see #encode(char[], Output, int, int)
	 */
	public long encodeImpl(final char[] src, Output dest, int srcOffset, final int chars, int info);

	/**
	 * Encodes a subset of the given character array to the desired output
	 * 
	 * @see #encode(char[], Output, int, int)
	 */
	public long decodeImpl(final char[] dest, Input src, int srcOffset, final int chars);

	/**
	 * Encodes a subset of the given character array to the desired output
	 * 
	 * @see #encode(char[], Output, int, int)
	 */
	public long nDecodeImpl(final char[] dest, Input src, int srcOffset, final int chars);

	/**
	 * Encodes a subset of the given character array to the desired output using a
	 * native alternative
	 * 
	 * @see #encode(char[], Output, int, int)
	 */
	public abstract int nEncodeImpl(char[] src, long dest, int srcOffset, int chars, int info);

	public char[] getDecodingCache();

	public int[] getEncodingCache();

	public float getMinCharactersPerByte();

	/*
	 * public int encodeNative(final char[] src, Output dest, int srcOffset, final
	 * int chars, boolean checkForErrors) { NativeM
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
