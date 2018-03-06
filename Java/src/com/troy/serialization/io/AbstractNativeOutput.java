package com.troy.serialization.io;

import sun.misc.Cleaner;

@SuppressWarnings("restriction")
public abstract class AbstractNativeOutput extends AbstractOutput {

	private Cleaner cleaner;

	public void setDeallocator(Runnable runnable) {
		cleaner = Cleaner.create(this, runnable);
	}

	@Override
	protected void finalize() throws Throwable {
		cleaner.clean();
	}

	@Override
	public void close() {
		cleaner.clean();
	}

}
