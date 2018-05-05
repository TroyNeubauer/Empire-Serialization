package com.troy.empireserialization.io;

import java.io.*;
import java.nio.*;

import com.troy.empireserialization.*;
import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.memory.NativeMemoryBlock;

/**
 * The super interface for all streams that read or write primitives. This class provides an abstraction for
 * reading/writing all primitive types, byte arrays, and variable length encoded numbers. The size in bytes of basic
 * primitives is equal to their native java size (byte=1, short=2, char=2, int=4, float=4, long=8, double=8). A boolean
 * on its own is one byte where a value of zero indicates false, and any other value indicates true.
 * 
 * <p>
 * <h2>Variable Length Encoding</h2>
 * Variable length encoding is the practice of encoding numbers using a variable amount bytes to ideally achieve 
 * a smaller end size, compared to native, fixed size encoding. Although this may cause larger numbers to be encoded 
 * using more bytes, numbers less than 128 and greater than -1 will be encoded using one byte as opposed to 1, 2, 4, or 8 bytes.
 * Variable length encoding works be using the most significant bit of each byte as a marker for identifying when a particular
 * Primitive ends. A byte with the most significant bit (MSB) set to one tells the deserializer that the next byte is also a part of
 * the next primitive. A MSB of zero tells the deserializer that the current primitive ends. 
 * 
 * @author Troy Neubauer
 *
 */
public interface EmpireIO extends Closeable {

	/**
	 * @return {@code true} if this IO has a java based byte array to buffer data, {@code false} otherwise. <br>
	 *         This method should be used to verify that a buffer is present before calling methods that require a
	 *         buffer like {@link #getBufferPosition()} and {@link #getBuffer()}
	 */
	public boolean hasBuffer();

	/**
	 * Returns the current offset used with the internal buffer. If no buffer is present, a {@link NoBufferException}
	 * will be thrown.
	 * 
	 * @return The offset in bytes if the buffer buffer is present
	 * @throws NoBufferException
	 *             If the implemenentation of this IO does not buffered by a java byte array
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 * @see #hasBuffer()
	 */
	public int getBufferPosition();
	
	/**
	 * Sets the current offset used with the internal buffer. If no buffer is present, a {@link NoBufferException}
	 * will be thrown.
	 * 
	 * @throws NoBufferException
	 *             If the implemenentation of this IO does not buffered by a java byte array
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 * @see #hasBuffer()
	 */
	public void setBufferPosition(int newPosition);

	/**
	 * Returns the byte array backing this IO. If no buffer is present, a {@link NoBufferException} will be thrown.
	 * 
	 * @return The buffer used by this IO
	 * @throws NoBufferException
	 *             If the implemenentation of this IO does not buffered by a java byte array
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 * 
	 * @see #hasBuffer()
	 */
	public byte[] getBuffer();

	/**
	 * Ensures that a certain amount of bytes can be written to or read from this IO. If this IO is an output with a
	 * buffer, this method ensures that there is enough space to store x amount of bytes after the current position
	 * before overflowing the buffer. If this IO is an input, this method will block until x bytes are available, or
	 * throw an {@link EndOfInputException} the end of stream is reached before x bytes are available.
	 * 
	 * @param bytes
	 *            The number of bytes to require this IO to have
	 * @throws EndOfInputException
	 *             If this IO is an input end the of stream is reached before x bytes are available
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 */
	public void require(long bytes);

	/**
	 * Increments the internal pointer used for buffering data by the number of bytes specified in the last call to
	 * {@link #require(long)}. <br>
	 * If the implemenentation of this IO doesn't use an internal buffer, this method does nothing. <br>
	 * This method should be used when bytes are written or read from this IO without incrementing internal offsets. For
	 * example, native methods.
	 */
	public void addRequired();

	/**
	 * Closes any resources used by this object. If any IO related are called after calling this method, an
	 * {@link AlreadyClosedException} will be thrown. Calling this method after the IO is already closed will have no
	 * effect.
	 * 
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void close();

	/**
	 * Sets the byte order for this IO to use.
	 * 
	 * @param byteOrder
	 *            The new byte order to use
	 */
	public void setByteOrder(ByteOrder byteOrder);

	/**
	 * Returns the byte order that this IO is currently using
	 * 
	 * @return The current byte order
	 */
	public ByteOrder getByteOrder();

	/**
	 * Mapping an IO involves creating a temporary native buffer containing empty space to write to (if this IO is an
	 * output), or space filled with the data (if this IO is an input). Returns a temporary buffer that can be used in
	 * native code. An IO can only be mapped at one instance in time. Use {@link NativeMemoryBlock#position()} to
	 * indicate how many bytes Call {@link #unmap(NativeMemoryBlock)} once reading/writing to the buffer is finished to
	 * commit all changed back to the IO
	 * 
	 * @param bytes
	 *            The number of bytes to require the buffer to have
	 * @return A native block of memory that can be used to speed up operations
	 * 
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public NativeMemoryBlock map(long bytes);

	/**
	 * Commits all changes back to the IO after use is complete {@link NativeMemoryBlock#setPosition(long)} should be
	 * used to convey how many bytes were consumed. Native code should increment the position by how many bytes were
	 * consumed as this method will only commit those changes
	 * 
	 * @param block
	 *            The block to unmap
	 * 
	 * 
	 * @throws EmpireSerializationIOException
	 *             If an I/O exception occurs
	 * @throws AlreadyClosedException
	 *             If {@link #close()} has already been called on this IO.
	 */
	public void unmap(NativeMemoryBlock block);

	/**
	 * 
	 * @return {@code true} if the endianness of the underlying system and the desired endianness are different, in
	 *         which case values written in native code need to be swapped. {@code false} otherwise
	 */
	default boolean swapEndinessInNative() {
		return (getByteOrder() == ByteOrder.BIG_ENDIAN) ^ (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
	}

}
