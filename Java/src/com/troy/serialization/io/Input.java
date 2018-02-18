package com.troy.serialization.io;

import com.troy.serialization.charset.TroyCharset;
import com.troy.serialization.exception.*;

public interface Input extends TroyIO {
	/**
	 * Reads the next byte in this input. This method will block until input is available if reading from a stream.
	 * 
	 * @return The next byte
	 * @throws EndOfInputException If the end of input was reached while attempting to read the next byte
	 * @throws AlreadyClosedException If the underlying input was already closed
	 */
	public byte readByte();

	/**
	 * Reads the next two big-endian bytes and composes them into a 16 bit short.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next two bytes as a short
	 * @throws EndOfInputException If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException If the underlying input was already closed
	 */
	public short readShort();

	/**
	 * Reads the next four big-endian bytes and composes them into a 32 bit integer.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next four bytes as an int
	 * @throws EndOfInputException If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException If the underlying input was already closed
	 */
	public int readInt();

	/**
	 * Reads the next eight big-endian bytes and composes them into a 64 bit long.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next eight bytes as a long
	 * @throws EndOfInputException If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException If the underlying input was already closed
	 */
	public long readLong();

	/**
	 * Reads the next four big-endian bytes and composes them into an IEEE 32 bit floating point number.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next four bytes as a float
	 * @throws EndOfInputException If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException If the underlying input was already closed
	 */
	public float readFloat();

	/**
	 * Reads the next eight big-endian bytes and composes them into an IEEE 64 bit floating point number.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next eight bytes as a double
	 * @throws EndOfInputException If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException If the underlying input was already closed
	 */
	public double readDouble();

	/**
	 * Reads the next two big-endian bytes and composes them into a 16 bit character.<br>
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return The next two bytes as a char
	 * @throws EndOfInputException If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException If the underlying input was already closed
	 */
	public char readChar();

	/**
	 * Reads a string using a charset and the character count
	 * This method will block until input is available if reading from a stream.
	 * 
	 * @return A string based on the number of characters and the charset to use to decode
	 * @throws EndOfInputException If the end of input was reached while attempting to read the next bytes
	 * @throws AlreadyClosedException If the underlying input was already closed
	 */
	public boolean readBoolean();

	public String readString(TroyCharset charset, int characters);

}
