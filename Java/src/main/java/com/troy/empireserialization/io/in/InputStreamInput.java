package com.troy.empireserialization.io.in;

import java.io.*;
import java.util.*;

import com.troy.empireserialization.*;
import com.troy.empireserialization.exception.*;
import com.troy.empireserialization.memory.MasterMemoryBlock;
import com.troy.empireserialization.memory.NativeMemoryBlock;
import com.troy.empireserialization.util.*;

public class InputStreamInput extends AbstractInput {

	private InputStream in;

	public InputStreamInput(InputStream in) {
		this.in = Objects.requireNonNull(in);
	}

	@Override
	public byte readByteImpl() {
		try {
			return (byte) in.read();
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new EmpireSerializationIOException(e);
		}
	}

	@Override
	public void addRequired() {
		// Nothing because InputStream.read handles everything for us
	}

	@Override
	public void close() {
		try {
			in.close();
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new EmpireSerializationIOException(e);
		}
		in = null;
	}

	@Override
	public NativeMemoryBlock map(long bytes) {
		byte[] temp = ByteArrayPool.aquire((int) Math.min(bytes, 1048576));
		MasterMemoryBlock block = null;
		try {
			try {
				block = MasterMemoryBlock.allocate(bytes);
				System.out.println("allocated " + bytes + " for the native buffer. Temp buffer " + temp.length + "bytes");
				int read = 0;
				long total = 0;
				while (total < bytes) {
					read = in.read(temp, 0, (int) Math.min(temp.length, bytes - total));
					System.out.println("total: " + total + " just read " + read);
					if (read == -1)
						throw new EndOfInputException();
					NativeUtils.bytesToNative(block.address + total, temp, 0, read);
					System.out.println("Native copy was nominal. Just copied " + read + " bytes");
					total += read;
				}

				return block;
			} catch (NullPointerException e) {
				if (block != null)
					block.free();
				throw new AlreadyClosedException();
			} catch (IOException e) {
				if (block != null)
					block.free();
				throw new EmpireSerializationIOException(e);
			}
		} finally {
			ByteArrayPool.restore(temp);

		}
	}

	@Override
	public void unmap(NativeMemoryBlock block) {
		block.setPosition(0);
		
	}

	@Override
	public boolean hasBuffer() {
		return false;
	}

	@Override
	public int getBufferPosition() {
		throw new NoBufferException();
	}

	@Override
	public byte[] getBuffer() {
		throw new NoBufferException();
	}

	@Override
	public void require(long bytes) {
		// Nothing because java.io.InputStream.read handles everything for us
	}

	@Override
	public long remaining() {
		try {
			return in.available();
		} catch (NullPointerException e) {
			throw new AlreadyClosedException();
		} catch (IOException e) {
			throw new EmpireSerializationIOException(e);
		}
	}

	@Override
	public void readBytes(byte[] dest, int offset, int count) {
		// TODO Auto-generated method stub
		
	}

}
