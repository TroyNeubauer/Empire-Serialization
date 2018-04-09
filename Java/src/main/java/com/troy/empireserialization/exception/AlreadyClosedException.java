package com.troy.empireserialization.exception;

/**
 * Signals that some closable source or destination has already been closed and is unusable
 * 
 * @author Troy Neubauer
 *
 */
public class AlreadyClosedException extends RuntimeException {

	public AlreadyClosedException() {
		super();
	}
}
