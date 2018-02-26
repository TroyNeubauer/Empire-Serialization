package com.troy.serialization.io;

import java.io.Closeable;

import com.troy.serialization.exception.*;

public interface TroyIO extends Closeable {

	/**
	 * @return {@code true} if this IO has a java based byte array to buffer data, {@code false} otherwise
	 */
	public boolean hasBuffer();

	/**
	 * Returns the current offset used with the internal buffer. If no buffer is present, a {@link NoBufferException} will be thrown.
	 * 
	 * @return The offset in bytes if the buffer used
	 * @throws NoBufferException If no buffer is present
	 * @see #hasBuffer()
	 */
	public int getBufferPosition();

	/**
	 * Returns the byte array backing this IO. If no buffer is present, a {@link NoBufferException} will be thrown.
	 * 
	 * @return The buffer used by this IO
	 * @throws NoBufferException If no buffer is present
	 * @see #hasBuffer()
	 */
	public byte[] getBuffer();

	/**
	 * Ensures that a certain amount of bytes can be written to or read from this IO. If this IO is an output with a buffer, this method ensures that
	 * there is enough space to store x amount of bytes after the current position before overflowing the buffer. If this IO is an input, this method
	 * will block until x bytes are available, or throw an {@link EndOfInputException} if x bytes cannot be read before hitting the end of stream.
	 * 
	 * @param bytes The number of bytes to require this IO to have
	 */
	public void require(long bytes);

	/**
	 * Closes any resources used by this object. If any IO related are called after calling this method, an {@link AlreadyClosedException} will be
	 * thrown. Calling this method after the IO is already closed will have no effect.
	 */
	public void close();
}
