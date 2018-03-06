package com.troy.serialization.exception;

/**
 * Indicates that some IO object has no internal buffer to obtain
 * @author Troy Neubauer
 *
 */
public class NoBufferException extends RuntimeException {

	public NoBufferException() {
		super();
	}

	public NoBufferException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoBufferException(String message) {
		super(message);
	}

	public NoBufferException(Throwable cause) {
		super(cause);
	}

}
