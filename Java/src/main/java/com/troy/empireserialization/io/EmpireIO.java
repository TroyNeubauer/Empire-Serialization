package com.troy.empireserialization.io;

import java.io.Closeable;
import java.nio.*;

import com.troy.empireserialization.*;
import com.troy.empireserialization.exception.*;

public interface EmpireIO extends Closeable {

	/**
	 * @return {@code true} if this IO has a java based byte array to buffer data, {@code false} otherwise. <br>
	 *         This method should be used to verify that a buffer is present before calling methods that require a buffer
	 *         like {@link #getBufferPosition()} and {@link #getBuffer()}
	 */
	public boolean hasBuffer();

	/**
	 * Returns the current offset used with the internal buffer. If no buffer is present, a {@link NoBufferException} will
	 * be thrown.
	 * 
	 * @return The offset in bytes if the buffer used
	 * @throws NoBufferException
	 *             If the implemenentation of this IO does not buffered by a java byte array
	 * @see #hasBuffer()
	 */
	public int getBufferPosition();

	/**
	 * Returns the byte array backing this IO. If no buffer is present, a {@link NoBufferException} will be thrown.
	 * 
	 * @return The buffer used by this IO
	 * @throws NoBufferException
	 *             If the implemenentation of this IO does not buffered by a java byte array
	 * @see #hasBuffer()
	 */
	public byte[] getBuffer();

	/**
	 * Ensures that a certain amount of bytes can be written to or read from this IO. If this IO is an output with a buffer,
	 * this method ensures that there is enough space to store x amount of bytes after the current position before
	 * overflowing the buffer. If this IO is an input, this method will block until x bytes are available, or throw an
	 * {@link EndOfInputException} the end of stream is reached before x bytes are available.
	 * 
	 * @param bytes
	 *            The number of bytes to require this IO to have
	 * @throws EndOfInputException
	 *             If this IO is an input end the of stream is reached before x bytes are available
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
	 */
	public void close();

	/**
	 * Sets the byte order for this IO to use.
	 * 
	 * @param byteOrder The new byte order to use
	 */
	public void setByteOrder(ByteOrder byteOrder);

	/**
	 * Returns the byte order that this IO is currently using
	 * @return The current byte order
	 */
	public ByteOrder getByteOrder();
	
	/**
	 * Returns a temporary buffer that can be used in native code. An IO can only be mapped at one instance in time.
	 * Use {@link NativeMemoryBlock#position()} to indicate how many bytes 
	 * Call {@link #unmap(NativeMemoryBlock)} 
	 * once reading/writing to the buffer is finished to commit all changed back to the IO
	 * @param bytes The number of bytes to require the buffer to have
	 * @return A native block of memory that can be used to speed up operations
	 */
	public NativeMemoryBlock map(long bytes);
	
	/**
	 * Commits all changes back to the IO after use is complete
	 * @param block The block to unmap
	 */
	public void unmap(NativeMemoryBlock block);

}
