package com.troy.empireserialization.io.in;

import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.io.*;

/**
 * Allows for reading primitive types from a linear input source.
 * 
 * @author Troy Neubauer
 *
 */
public interface Input extends EmpireIO {
	/**
	 * Reads the next byte in this input without checking for errors. This method should be used as the base method to
	 * implement reading primitives. This method should be called any number of times to retrieve the required data for
	 * reading higher level primitives after checking for errors.
	 * 
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next byte
	 */
	byte readByteImpl();

	/**
	 * Reads the next byte in this input. This method will block until input is available if reading from a stream.
	 * 
	 * @return The next byte
	 * @throws EndOfInputException
	 *             If the end of input was reached while attempting to read the next byte
	 * @throws AlreadyClosedException
	 *             If the underlying input was already closed
	 */
	public byte readByte();

	/**
	 * Reads the next two big-endian bytes and composes them into a 16 bit short.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next two bytes as a short
	 * @throws EndOfInputException
	 *             If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException
	 *             If the underlying input was already closed
	 */
	public short readShort();

	/**
	 * Reads the next four big-endian bytes and composes them into a 32 bit integer.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next four bytes as an int
	 * @throws EndOfInputException
	 *             If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException
	 *             If the underlying input was already closed
	 */
	public int readInt();

	/**
	 * Reads the next eight big-endian bytes and composes them into a 64 bit long.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next eight bytes as a long
	 * @throws EndOfInputException
	 *             If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException
	 *             If the underlying input was already closed
	 */
	public long readLong();

	/**
	 * Reads the next four big-endian bytes and composes them into an IEEE 32 bit floating point number.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next four bytes as a float
	 * @throws EndOfInputException
	 *             If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException
	 *             If the underlying input was already closed
	 */
	public float readFloat();

	/**
	 * Reads the next eight big-endian bytes and composes them into an IEEE 64 bit floating point number.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next eight bytes as a double
	 * @throws EndOfInputException
	 *             If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException
	 *             If the underlying input was already closed
	 */
	public double readDouble();

	/**
	 * Reads the next two big-endian bytes and composes them into a 16 bit character.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next two bytes as a char
	 * @throws EndOfInputException
	 *             If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException
	 *             If the underlying input was already closed
	 */
	public char readChar();

	/**
	 * Reads a string using a charset and the character count This method will block until input is available if reading
	 * from a stream.
	 * 
	 * @return A string based on the number of characters and the charset to use to decode
	 * @throws EndOfInputException
	 *             If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException
	 *             If the underlying input was already closed
	 */
	public boolean readBoolean();

	public short readVLEShort();

	public int readVLEInt();

	public long readVLELong();

	public char readVLEChar();

	/**
	 * Returns an estimate as to number of bytes that can be read (or skipped over) from this input without blocking. A
	 * single read of this many bytes will not block, and neither will reading fewer bytes.
	 *
	 * <p>
	 * Note that while some implementations of {@code Input} will return the total number of bytes in the stream, many
	 * will not. Especially when dealing with web sockets, where the number of bytes remaining depends on the other
	 * side. It is never correct to use the return value of this method to allocate a buffer intended to hold all data
	 * in this stream.
	 * 
	 * @return An estimate as to number of bytes that can be read (or skipped over) from this input without blocking
	 */
	public long remaining();

	public void readBytes(byte[] dest, int offset, int count);

	public default byte[] readBytes(int length) {
		byte[] dest = new byte[length];
		readBytes(dest, 0, length);
		return dest;
	}

}
