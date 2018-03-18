package com.troy.serialization.exception;

import com.troy.serialization.charset.TroyCharset;

/**
 * Signals that a character is unsupported by a specific charset and that the charset unable to encode the string into bytes
 * @author Troy Neubauer
 *
 */
public class UnsupportedCharacterException extends RuntimeException {

	public UnsupportedCharacterException(char c, int index, TroyCharset charset) {
		super("Invalid character \'" + c + "\' at index " + index + " for charset " + charset.name());
	}

	public UnsupportedCharacterException(char c, TroyCharset charset) {
		super("Invalid character \'" + c + "\' for charset " + charset.name());
	}

	public UnsupportedCharacterException(TroyCharset charset) {
		super("Invalid character for charset " + charset.name());
	}

}
