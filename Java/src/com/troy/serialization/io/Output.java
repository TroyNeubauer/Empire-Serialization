package com.troy.serialization.io;

/**
 * Allows for writing primitives to a linear output
 * @author Troy Neubauer
 *
 */
public interface Output extends TroyIO {

	/**
	 * Writes a byte to the output without checking for overflow with require(). This function should be called by every write function after
	 * checking for overflow
	 * 
	 * @param b The byte to write
	 */
	void writeByteImpl(byte b);

	/**
	 * Writes a byte to this output
	 * 
	 * @param b The byte to write
	 */
	public void writeByte(byte b);

	/**
	 * Writes a 16 bit big endian integer to this output
	 * 
	 * @param b The short to write
	 */
	public void writeShort(short b);

	/**
	 * Writes a 32 bit big endian integer to this output
	 * 
	 * @param b The int to write
	 */
	public void writeInt(int b);

	/**
	 * Writes a 64 bit big endian integer to this output
	 * 
	 * @param b The long to write
	 */
	public void writeLong(long b);

	/**
	 * Writes a 32 bit big endian floating point number to this output
	 * 
	 * @param b The float to write
	 */
	public void writeFloat(float b);

	/**
	 * Writes a 64 bit big endian floating point number to this output
	 * 
	 * @param b The double to write
	 */
	public void writeDouble(double b);

	/**
	 * Writes a 16 bit big endian unicode character to this output
	 * 
	 * @param b The char to write
	 */
	public void writeChar(char b);

	/**
	 * Writes a 1 byte boolean to this output
	 * 
	 * @param b The boolean to write
	 */
	public void writeBoolean(boolean b);

	/**
	 * Writes a variable length encoded 16 bit big endian integer to this output
	 * @param b The short to write using variable length encoding
	 */
	public void writeVLEShort(short b);

	/**
	 * Writes a variable length encoded 32 bit big endian integer to this output
	 * @param b The int to write using variable length encoding
	 */
	public void writeVLEInt(int b);

	/**
	 * Writes a variable length encoded 64 bit big endian integer to this output
	 * @param b The long to write using variable length encoding
	 */
	public void writeVLELong(long b);

	/**
	 * Writes a variable length encoded 16 bit big unicode character to this output
	 * @param b The char to write using variable length encoding
	 */
	public void writeVLEChar(char b);

	/**
	 * Returns a MappedOutput object which contains a pointer to 
	 * @param bytes
	 * @return
	 */
	public AbstractMappedIO mapOutput(long bytes);

	public void unmapOutput(AbstractMappedIO mappedOutput, long numBytesWritten);

	/**
	 * Flushes this output and forces any buffered output bytes to be written out. The general contract of <code>flush</code> is that calling it is
	 * an indication that, if any bytes previously written have been buffered by the implementation of the output stream, such bytes should
	 * immediately be written to their intended destination.
	 * <p>
	 * If the intended destination of this stream is an abstraction provided by the underlying operating system, for example a file, then flushing
	 * the stream guarantees only that bytes previously written to the stream are passed to the operating system for writing; it does not guarantee
	 * that they are actually written to a physical device such as a disk drive.
	 * <p>
	 */
	public void flush();

}
