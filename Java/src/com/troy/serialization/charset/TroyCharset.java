package com.troy.serialization.charset;

import com.troy.serialization.io.*;

public interface TroyCharset {
	/**
	 * Decodes bytes from a source byte array to a char array
	 * @param src The source of encoded bytes to draw from
	 * @param dest The resulting array to write to
	 * @param destOffset The offset on where to start writing to into dest
	 * @param chars The number of characters to decode
	 * @return the number of bytes read by this method
	 */
	public int decode(Input src, final char[] dest, int destOffset, final int chars, boolean checkForErrors);
	
	/**
	 * Encodes bytes from a char array into an output
	 * @param src The source of characters to draw from
	 * @param dest The destination to write to
	 * @param srcOffset The offset to start reading from in the source array
	 * @param chars The number of characters to encode
	 * @return the number of bytes written into dest by this method
	 */
	public int encode(final char[] src, Output dest, int srcOffset, final int chars, boolean checkForErrors);
	
	public float getMinCharactersPerByte();
	
	default public float getBytesPerCharacter() {
		return 1.0f / getMinCharactersPerByte();
	}
	
	void init();
	
	public int getCharsetCode();

	public String name();
}
