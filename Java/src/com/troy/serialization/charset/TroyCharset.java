package com.troy.serialization.charset;

public interface TroyCharset {
	/**
	 * Decodes bytes from a source byte array to a char array
	 * @param src The source of encoded bytes to draw from
	 * @param dest The resulting array to write to
	 * @param srcOffset The offset to start reading from in the source array
	 * @param destOffset The offset on where to start writing to into dest
	 * @param chars The number of characters to decode
	 * @return the number of character written into dest by this method
	 */
	public int decode(final byte[] src, final char[] dest, int srcOffset, int destOffset, final int chars, boolean checkForErrors);
	
	/**
	 * Encodes bytes from a char array into a byte array
	 * @param src The source of characters to  draw from
	 * @param dest The byte array to write the encoded characters to
	 * @param srcOffset The offset to start reading from in the source array
	 * @param destOffset The offset on where to start writing to into dest
	 * @param chars The number of characters to encode
	 * @return the number of bytes written into dest by this method
	 */
	public int encode(final char[] src, final byte[] dest, int srcOffset, int destOffset, final int chars, boolean checkForErrors);
	
	public float getMinCharactersPerByte();
	
	default public float getBytesPerCharacter() {
		return 1.0f / getMinCharactersPerByte();
	}
	
	void init();
	
	public int getCharsetCode();
	
	public int encode(char c, int index, boolean checkForErrors);
	
	public char decode(int encoded, boolean checkForErrors);

	public String name();
}
