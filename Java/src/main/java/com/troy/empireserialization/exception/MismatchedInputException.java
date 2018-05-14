package com.troy.empireserialization.exception;

import com.troy.empireserialization.io.in.Input;

/**
 * Thrown when an {@link Input} expects one type but detects another. 
 * @author Troy Neubauer
 *
 */
public class MismatchedInputException extends RuntimeException {

	public MismatchedInputException() {
		super();
	}
	public MismatchedInputException(String message, Throwable cause) {
		super(message, cause);
	}

	public MismatchedInputException(String message) {
		super(message);
	}

	public MismatchedInputException(Throwable cause) {
		super(cause);
	}
	
}
