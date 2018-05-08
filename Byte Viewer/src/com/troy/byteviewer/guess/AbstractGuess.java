package com.troy.byteviewer.guess;

import java.awt.Color;
import java.util.List;

import com.troy.empireserialization.io.in.Input;

public abstract class AbstractGuess {

	private long maxOffset;
	private List<AnnotatedSection> guesses;

	private static final Color[] ROTATION = { Color.MAGENTA, Color.ORANGE, Color.LIGHT_GRAY, Color.PINK, Color.RED };
	private static int colorIndex;

	public void read(Input input) {
		maxOffset += readImpl(input, guesses);
	}

	/**
	 * Reads for guesses
	 * @param input
	 * @param guesses
	 * @return
	 */
	public abstract long readImpl(Input input, List<AnnotatedSection> guesses);

	protected Color nextColor() {
		colorIndex %= ROTATION.length;
		return ROTATION[colorIndex++];
	}

}
