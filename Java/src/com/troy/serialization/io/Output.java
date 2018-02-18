package com.troy.serialization.io;

public interface Output extends TroyIO {

	public void writeByte(byte b);

	public void writeShort(short b);

	public void writeInt(int b);

	public void writeLong(long b);

	public void writeFloat(float b);

	public void writeDouble(double b);

	public void writeChar(char b);

	public void writeBoolean(boolean b);

}
