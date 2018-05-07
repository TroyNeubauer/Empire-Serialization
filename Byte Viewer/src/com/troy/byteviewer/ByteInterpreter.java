package com.troy.byteviewer;

import java.nio.ByteBuffer;

public interface ByteInterpreter {
	public char[] interpret(ByteBuffer buffer, int length);
}
