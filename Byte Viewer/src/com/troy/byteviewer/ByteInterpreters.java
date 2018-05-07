package com.troy.byteviewer;

import java.nio.ByteBuffer;

public class ByteInterpreters {
	public static final ByteInterpreter ASCII = new ByteInterpreter() {

		@Override
		public char[] interpret(ByteBuffer buffer, int length) {
			char[] result = new char[length];
			for (int i = 0; i < length; i++) {
				result[i] = (char) buffer.get(i);
			}
			return result;
		}
	};
}
