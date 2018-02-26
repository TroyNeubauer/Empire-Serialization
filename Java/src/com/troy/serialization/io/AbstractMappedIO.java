package com.troy.serialization.io;

/**
 * A small structure that contains information about where a version of an io is stored in native memory
 * 
 * @author Troy Neubauer
 * @see Input
 * @see Output
 * @see Input#mapInput(long)
 * @see Output#mapOutput(long)
 */
public class AbstractMappedIO {
	/**
	 * The address of the buffer
	 */
	protected long address;

	/**
	 * The index of the first valid byte. Always positive, zero or negative one. A value of negative one indicates that this mapped output object is
	 * invalid, and not attached to any output.
	 */
	protected long offset;

	protected long capacity;

	public AbstractMappedIO(long address, long offset, long capacity) {
		this.address = address;
		this.offset = offset;
		this.capacity = capacity;
	}

	public long getAddress() {
		return address;
	}

	public long getOffset() {
		return offset;
	}

	public long getCapacity() {
		return capacity;
	}

	public boolean isValid() {
		return offset >= 0;
	}

	/**
	 * Sets the offset for this mapped output. This is protected because the offset should be only set by code managing mapped buffers. Not the user.
	 * 
	 * @param offset The new offset
	 */
	protected void setOffset(int offset) {
		this.offset = offset;
	}

}
