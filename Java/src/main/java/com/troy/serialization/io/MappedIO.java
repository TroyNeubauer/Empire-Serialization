package com.troy.serialization.io;

import java.io.*;

import com.troy.serialization.util.*;
import sun.misc.Unsafe;

/**
 * A small structure that contains information about where a version of an io is stored in native memory
 * 
 * @author Troy Neubauer
 * @see Input
 * @see Output
 * @see Input#mapInput(long)
 * @see Output#mapOutput(long)
 */
public abstract class MappedIO implements Closeable {
	protected static final Unsafe unsafe = MiscUtil.getUnsafe();
	/**
	 * The address of the buffer
	 */
	protected long address;

	/**
	 * The offset from address that the receiver should write to. Always positive or zero
	 */
	protected long offset;

	/**
	 * The number of validly accessible bytes in this buffer
	 */
	protected long capacity;

	public MappedIO(long address, long offset, long capacity) {
		this.address = address;
		this.offset = offset;
		this.capacity = capacity;
	}
	
	/**
	 * Ensures that there are at least x bytes available in this output
	 * @param bytes The number of bytes to require
	 */
	public abstract void require(long bytes);

	/**
	 * Sets the offset for this mapped output. This is protected because the offset should be only set by code managing mapped buffers. Not the user.
	 * 
	 * @param offset The new offset
	 */
	protected void setOffset(long offset) {
		this.offset = offset;
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
	
	@Override
	public void close() {
		if(address != 0) {
			unsafe.freeMemory(address);
			address = 0;
			capacity = -1;
		}
	}

	@Override
	public String toString() {
		return "MappedIO [address=0x" + StringFormatter.toHexString(address) + ", offset=" + offset + ", capacity=" + capacity + "]";
	}
	
	
	

}
