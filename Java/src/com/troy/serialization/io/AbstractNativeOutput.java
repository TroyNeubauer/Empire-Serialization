package com.troy.serialization.io;

import com.troy.serialization.util.InternalLog;

import sun.misc.Cleaner;

public abstract class AbstractNativeOutput<Deallocator extends Runnable> extends AbstractOutput {

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
			InternalLog.log("Releasing native output " + this);
		}
		
	}
	
	protected Deallocator getDeallocator() {
		return deallocator;
	}

	@Override
	public void close() {
		cleaner.clean();
	}

}
