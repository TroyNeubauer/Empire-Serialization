package com.troy.serialization;

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
