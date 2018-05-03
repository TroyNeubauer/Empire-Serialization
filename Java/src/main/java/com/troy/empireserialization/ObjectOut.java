package com.troy.empireserialization;

import java.io.*;
import java.math.*;
import java.util.*;

public interface ObjectOut extends Flushable, AutoCloseable {

	public <T> void writeObject(T obj);

	// For writing "primitives"

	public void writeByte(byte b);

	public void writeShort(short s);

	public void writeInt(int i);

	public void writeLong(long l);

	public void writeFloat(float f);

	public void writeDouble(double d);

	public void writeChar(char c);

	public void writeBoolean(boolean b);

	public void writeString(String str);

	public void writeBigInteger(BigInteger integer);

	public void writeBigDecimal(BigDecimal decimal);

	public void writeArray(Object[] array);

	public <T> void writeList(List<T> list, Class<List<?>> type);

	public <T> void writeSet(Set<T> set, Class<Set<?>> type);

	public <K, V> void writeMap(Map<K, V> map, Class<Map<?, ?>> type);

	public void flush();

	public void close();

}
