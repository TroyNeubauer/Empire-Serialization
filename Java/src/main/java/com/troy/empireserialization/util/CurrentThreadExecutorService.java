package com.troy.empireserialization.util;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class CurrentThreadExecutorService extends AbstractExecutorService {

	@Override
	public void shutdown() {
		
	}

	@Override
	public List<Runnable> shutdownNow() {
		return null;
	}

	@Override
	public boolean isShutdown() {
		return true;
	}

	@Override
	public boolean isTerminated() {
		return true;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return true;
	}

	@Override
	public void execute(Runnable command) {
		command.run();
	}

}
