package com.troy.serialization.exception;

/**
 * Thrown when a buffer attempts to map itself, but cannot because the caller has not unmapped it.
 * @author Troy Neubauer
 *
 */
public class AlreadyMappedException extends RuntimeException {
	public AlreadyMappedException(String message) {
		super(message);
	}
}
