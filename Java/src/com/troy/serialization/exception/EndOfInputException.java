package com.troy.serialization.exception;

/**
 * Signals that the end of some input has occurred
 * 
 * @author Troy Neubauer
 *
 */
public class EndOfInputException extends RuntimeException {

	public EndOfInputException() {
		super();
	}

	public EndOfInputException(String message, Throwable cause) {
		super(message, cause);
	}

	public EndOfInputException(String message) {
		super(message);
	}

	public EndOfInputException(Throwable cause) {
		super(cause);
	}

}
