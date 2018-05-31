package com.troy.empireserialization;

public interface ObjectIn extends AutoCloseable {
	
	public Object readObject();
	
	Object readObjectRecursive();

	public void close();
}
