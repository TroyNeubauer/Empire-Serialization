package com.troy.empireserialization.exception;

/**
 * Thrown to indicate that an IO exception occurred internally. Unlike {@link java.io.IOException}, 
 * {@link EmpireSerializationIOException} is an unchecked exception, meaning that callers of a method that throws
 * an {@link EmpireSerializationIOException} do not need to surround such call with a try and catch block. Although try
 * and catch should be used in production code, the unchecked nature of this class allows for writing significantly
 * cleaner test code. 
 * @author Troy Neubauer
 *
 */
public class EmpireSerializationIOException extends RuntimeException {

	public EmpireSerializationIOException() {
		super();
	}

	public EmpireSerializationIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmpireSerializationIOException(String message) {
		super(message);
	}

	public EmpireSerializationIOException(Throwable cause) {
		super(cause);
	}

}
