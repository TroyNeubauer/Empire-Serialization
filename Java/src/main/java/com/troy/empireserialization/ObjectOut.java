package com.troy.empireserialization;

import java.io.*;
import java.math.*;
import java.util.*;

public interface ObjectOut extends Flushable, AutoCloseable {

	public void writeObject(Object obj);

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

	public void writeList(List<?> list);

	public void writeSet(Set<?> set);

	public void writeMap(Map<?, ?> map);
	
	/**
	 * Handles cases where the user passes a "primitive" into the write object method as an object 
	 * (ie a set, list, map, Integer, Float, Character)
	 * This ,method checks all primitives as opposed to {@link #checkForPrimitiveFast(Object, Class)}
	 * @return {@code true} If the type passed in was a primitive and was written using the correct writeX() method
	 *         {@code false} otherwise
	 */
	public boolean checkForPrimitiveSlow(Object obj, Class<?> clazz);
	
	/**
	 * Handles cases where the user passes a "primitive" into the write object method as an object 
	 * (ie a set, list, map, Integer, Float, Character)
	 * This method only checks the primitive non wrapper classes.
	 * 
	 * @return {@code true} If the type passed in was a primitive and was written using the correct writeX() method
	 *         {@code false} otherwise
	 */
	public boolean checkForPrimitiveFast(Object obj, Class<?> clazz);

	public void flush();

	public void close();

}
