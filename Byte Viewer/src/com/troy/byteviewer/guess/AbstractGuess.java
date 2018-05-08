package com.troy.byteviewer.guess;

import java.awt.Color;
import java.util.List;

import com.troy.empireserialization.io.in.Input;

public abstract class AbstractGuess {

	private long maxOffset;
	private List<AnnotatedSection> guesses;

	private static final Color[] ROTATION = { Color.MAGENTA, Color.ORANGE, Color.LIGHT_GRAY, Color.PINK, Color.RED };
	private static int colorIndex;

	public void read(Input input, long minimunIndex) {
		long toRead = maxOffset - minimunIndex;
		if (toRead <= 0)
			return;
		maxOffset += readImpl(input, toRead, guesses);
	}

	/**
	 * Reads for guesses and adds them to the list
	 * 
	 * @param input The input to read from
	 * @param minBytes The minimum number of bytes to read
	 * @param guesses The list of guesses to add to
	 * @return The number of actual bytes read (must be > {@code minBytes})
	 */
	public abstract long readImpl(Input input, long minBytes, List<AnnotatedSection> guesses);

	protected Color nextColor() {
		colorIndex %= ROTATION.length;
		return ROTATION[colorIndex++];
	}

}
