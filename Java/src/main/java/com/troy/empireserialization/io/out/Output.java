package com.troy.empireserialization.io.out;

import java.io.*;

import com.troy.empireserialization.io.*;

/**
 * An interface for writing primitives to a linear output
 * 
 * @author Troy Neubauer
 *
 */
public interface Output extends EmpireIO, ArrayOutput, Flushable {

	/**
	 * Writes a byte to the output without checking for overflow with require(). This function should be called by every
	 * other write function to implement writing larger sized types.
	 * 
	 * @param b
	 *            The byte to write
	 ** @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	void writeByteImpl(byte b);

	/**
	 * Writes a byte to this output
	 * 
	 * @param b
	 *            The byte to write
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeByte(byte b);

	/**
	 * Writes a byte to this output<br>
	 * Note, this method takes in an int, this is solely for convince. This method ignores the 24 high bits of the
	 * argument
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeByte(int b);

	/**
	 * Writes a 16 bit integer to this output
	 * 
	 * @param b
	 *            The short to write
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 *
	 */
	public void writeShort(short b);

	/**
	 * Writes a 32 bit integer to this output
	 * 
	 * @param b
	 *            The int to write
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeInt(int b);

	/**
	 * Writes a 64 bit integer to this output
	 * 
	 * @param b
	 *            The long to write
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeLong(long b);

	/**
	 * Writes a byte to this output, interpreting the short as if it was an unsigned byte.
	 * 
	 * @param b
	 *            The byte to write
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeUnsignedByte(short b);

	/**
	 * Writes a short to this output, interpreting the int as if it was an unsigned short.
	 * 
	 * @param b
	 *            The short to write
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeUnsignedShort(int b);

	/**
	 * Writes an int to this output, interpreting the long as if it was an unsigned int.
	 * 
	 * @param b
	 *            The int to write
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 *
	 */
	public void writeUnsignedInt(long b);

	/**
	 * Writes a 32 bit floating point number to this output
	 * 
	 * @param b
	 *            The float to write
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeFloat(float b);

	/**
	 * Writes a 64 bit floating point number to this output
	 * 
	 * @param b
	 *            The double to write
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeDouble(double b);

	/**
	 * Writes a 16 bit unicode character to this output
	 * 
	 * @param b
	 *            The char to write
	 */
	public void writeChar(char b);

	/**
	 * Writes a 1 byte boolean to this output
	 * 
	 * @param b
	 *            The boolean to write
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeBoolean(boolean b);

	/**
	 * Writes a variable length encoded 16 bit big endian integer to this output
	 * 
	 * @param b
	 *            The short to write using variable length encoding
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeVLEShort(short b);

	/**
	 * Writes a variable length encoded 32 bit big endian integer to this output
	 * 
	 * @param b
	 *            The int to write using variable length encoding
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeVLEInt(int b);

	/**
	 * Writes a variable length encoded 64 bit big endian integer to this output
	 * 
	 * @param b
	 *            The long to write using variable length encoding
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeVLELong(long b);

	/**
	 * Writes a variable length encoded 16 bit big unicode character to this output
	 * 
	 * @param b
	 *            The char to write using variable length encoding
	 *
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void writeVLEChar(char b);

	/**
	 * Flushes this output and forces any buffered output bytes to be written out. The general contract of
	 * <code>flush</code> is that calling it is an indication that, if any bytes previously written have been buffered
	 * by the implementation of the output stream, such bytes should immediately be written to their intended
	 * destination.
	 * <p>
	 * If the intended destination of this stream is an abstraction provided by the underlying operating system, for
	 * example a file, then flushing the stream guarantees only that bytes previously written to the stream are passed
	 * to the operating system for writing; it does not guarantee that they are actually written to a physical device
	 * such as a disk drive.
	 * <p>
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void flush();

	public default void writeBytes(byte[] src) {
		writeBytes(src, 0, src.length);
	}

	public void writeBytes(byte[] src, int offset, int bytes);
	
	/**
	 * Returns {@code true} if this is internally backed by some native IO, {@code false} otherwise. 
	 * If this method returns true, using {@link #map(long)} will be very efficient and desired.
	 * If this method returns false, it doesn't mean that {@link #map(long)} shouldn't be used, 
	 * (in most cases for large data sets it will be faster) it only means to serve as a hint for optimization
	 * 
	 * @return Weather or not this Output is backed by some native IO.
	 */
	public boolean isNative();

}
