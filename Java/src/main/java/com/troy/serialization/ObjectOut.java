package com.troy.serialization;

import java.math.*;
import java.util.*;

public interface ObjectOut {

	public void writeObject(Object obj);

	public void writeByte(byte b);

	public void writeShort(short s);

	public void writeInt(int i);

	public void writeLong(long l);

	public void writeUnsignedByte(byte b);

	public void writeUnsignedShort(short s);

	public void writeUnsignedInt(int i);

	public void writeUnsignedLong(long l);

	public void writeFloat(float f);

	public void writeDouble(double d);

	public void writeChar(char c);

	public void writeBoolean(boolean b);
	
	public void writeString(String str);

	public void writeBigInteger(BigInteger integer);

	public void writeBigDecimal(BigDecimal decimal);

	public void writeArray(Object array);

	public void writeList(List<?> list);

	public void writeSet(Set<?> set);

	public void writeMap(Map<?, ?> set);

}
