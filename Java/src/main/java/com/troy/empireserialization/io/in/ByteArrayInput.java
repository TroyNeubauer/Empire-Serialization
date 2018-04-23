package com.troy.empireserialization.io.in;

import com.troy.empireserialization.*;
import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.memory.MasterMemoryBlock;
import com.troy.empireserialization.memory.NativeMemoryBlock;
import com.troy.empireserialization.util.*;

public class ByteArrayInput extends AbstractInput {

	private int position;
	private byte[] buffer;

	private int required;
	private MasterMemoryBlock block;

	@Override
	public byte readByteImpl() {
		try {
			return buffer[position++];
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		}
	}

	@Override
	public void addRequired() {
		position += required;
	}

	@Override
	public void close() {
		buffer = null;// Help GC
	}

	@Override
	public NativeMemoryBlock map(long bytes) {
		//Will throw an exception if there aren't x unconsumed bytes
		require(bytes);
		if(block == null) {
			block = MasterMemoryBlock.allocate(bytes);
		} else {
			block.require(bytes);
		}
		block.setPosition(0);
		NativeUtils.bytesToNative(block.address(), buffer, position, (int) bytes);
		return block;
	}

	@Override
	public void unmap(NativeMemoryBlock block) {
		this.position += block.position();
	}

	@Override
	public boolean hasBuffer() {
		return true;
	}

	@Override
	public int getBufferPosition() {
		return position;
	}

	@Override
	public byte[] getBuffer() {
		if(buffer == null) throw new AlreadyClosedException();
		return buffer;
	}

	@Override
	public void require(long bytes) {
		if (bytes > buffer.length - position)
			throw new EndOfInputException();
		required = (int) bytes;
	}

	@Override
	public long remaining() {
		return buffer.length - position;
	}

	@Override
	public void readBytes(byte[] dest, int offset, int count) {
		// TODO Auto-generated method stub
		
	}

}
