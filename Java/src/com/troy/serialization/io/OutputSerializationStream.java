package com.troy.serialization.io;

import java.io.File;

import com.troy.serialization.util.*;

public class OutputSerializationStream {

	private Output out;
	private EncodingCache<String> stringCache;
	private EncodingCache<Class> classCache;

	public OutputSerializationStream(Output out) {
		this.out = out;
	}

	public OutputSerializationStream(File file) {
		this.out = new NativeFileOutput(file);
	}

	public void writeObject(Object obj) {
	}

	private void writeByteFull(byte b) {

	}

	private void writeShortFull(short s) {

	}

	private void writeIntFull(int i) {

	}

	private void writeLongFull(long l) {

	}

	private void writeFloatFull(float f) {

	}

	private void writeDoubleFull(double d) {

	}

	private void writeCharFull(char c) {

	}

	private void writeBooleanFull(boolean b) {

	}

}
