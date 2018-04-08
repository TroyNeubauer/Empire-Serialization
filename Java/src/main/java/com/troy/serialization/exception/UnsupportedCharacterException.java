package com.troy.serialization.exception;

import com.troy.serialization.charset.EmpireCharset;

/**
 * Signals that a character is unsupported by a specific charset and that the charset unable to encode the string into bytes
 * @author Troy Neubauer
 *
 */
public class UnsupportedCharacterException extends RuntimeException {

	public UnsupportedCharacterException(char c, int index, EmpireCharset charset) {
		super("Invalid character \'" + c + "\' at index " + index + " for charset " + charset.name());
	}

	public UnsupportedCharacterException(char c, EmpireCharset charset) {
		super("Invalid character \'" + c + "\' for charset " + charset.name());
	}

	public UnsupportedCharacterException(EmpireCharset charset) {
		super("Invalid character for charset " + charset.name());
	}

}
