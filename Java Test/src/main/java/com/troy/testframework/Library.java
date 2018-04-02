package com.troy.testframework;

import java.io.*;

public abstract class Library {
	private String name;
	
	public abstract void writeObjectToFile(File file, Object obj);
	public abstract byte[] writeObjectToBytes(Object obj);
}
