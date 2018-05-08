package com.troy.empireserialization.io.in;

import java.nio.ByteOrder;

import com.troy.empireserialization.exception.NoBufferException;
import com.troy.empireserialization.memory.NativeMemoryBlock;
import com.troy.empireserialization.util.InternalLog;

import sun.misc.Cleaner;

public abstract class AbstractNativeInput<Deallocator extends Runnable> extends AbstractInput {
	private Cleaner cleaner;
	private Deallocator deallocator;

	public void setDeallocator(Deallocator deallocator) {
		cleaner = Cleaner.create(this, new AbstractNativeOutputRunnable());
		this.deallocator = deallocator;
	}

	private class AbstractNativeOutputRunnable implements Runnable {
		@Override
		public void run() {
			deallocator.run();
			InternalLog.log("Releasing native input " + this);
		}

	}

	protected Deallocator getDeallocator() {
		return deallocator;
	}

	@Override
	public void close() {
		cleaner.clean();
	}

	//Let subclasses override these methods if they need to
	@Override
	public void setBufferPosition(int newPosition) {
		throw new NoBufferException();
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
}
