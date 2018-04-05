package com.troy.serialization;

public interface NativeMemoryBlock {
	/**
	 * Returns the address of the start of the memory block
	 */
	public long address();

	/**
	 * Returns the number of bytes this buffer currently has allocated for it
	 */
	public long capacity();

	/**
	 * Returns the number of bytes consumed by this buffer. Set using{@link #setPosition(long)}
	 */
	public long position();

	/**
	 * Sets the number of consumed byte for this memory block
	 * 
	 * @param position
	 *            The new position
	 */
	public void setPosition(long position);

	/**
	 * Returns the number of bytes left between the position and the end of this buffer
	 */
	public default long remaining() {
		return capacity() - position();
	}

	/**
	 * Returns a subset of this memory block
	 * 
	 * @param offset
	 *            The number of bytes into this block that the new block should start at
	 * @param length
	 *            The number of bytes in the resulting buffer
	 */
	public NativeMemoryBlock subset(long offset, long length);

	/**
	 * Frees the native memory used by this block if this object owns it. If this object represents a subset of another
	 * block of memory, no action is taken
	 */
	public void free();

	/**
	 * Ensures that there is at least x bytes between this block's position and its capacity, resizing the buffer if the
	 * condition is not met.
	 * 
	 * @param bytes
	 *            The number of bytes to require
	 */
	public default void require(long bytes) {
		if (position() + bytes > capacity()) {
			resize(capacity() * 2);
		}
	}

	/**
	 * Resizes this buffer to a new size so that it can hold at exactly x bytes after this method call
	 * 
	 * @param bytes
	 *            The new capacity of this buffer
	 */
	void resize(long bytes);
	
	/**
	 * Throws a runtime exception if the offset passed in is out of the range of this buffer
	 * @param offset The offset to check
	 * @throws RuntimeException If the offset is out of range
	 */
	public void checkOffset(long offset);
}
