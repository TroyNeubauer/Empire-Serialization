package com.troy.serialization.io;

import java.io.File;

import com.troy.serialization.util.*;

public class OutputSerializationStream {
	
	private Output out;
	private EncodingCache<String> stringCache;
	private EncodingCache<ClassInfo> classCache;

	public OutputSerializationStream(Output out) {
		this.out = out;
	}
	
	public OutputSerializationStream(File file) {
		this.out = new NativeFileOutput(file);
	}
	
	public void writeObject(Object obj) {
		
	}
	
	private void o_writeByte(byte b) {
		
	}
	
	private void o_writeShort(short s) {
		
	}
	
	private void o_writeInt(int i) {
		
	}
	
	private void o_writeLong(long l) {
		
	}
	
	private void o_writeFloat(float f) {
		
	}
	
	private void o_writeDouble(double d) {
		
	}
	
	private void o_writeChar(char c) {
		
	}
	
	private void o_writeBoolean(boolean b) {
		
	}

}
