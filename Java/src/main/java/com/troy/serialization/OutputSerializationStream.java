package com.troy.serialization;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.troy.serialization.charset.*;
import com.troy.serialization.io.NativeFileOutput;
import com.troy.serialization.io.Output;
import com.troy.serialization.util.*;

public class OutputSerializationStream implements ObjectOut {

	private Output out;
	private IntCache<String> stringCache;
	private IntCache<Class<?>> classCache;
	private IntCache<Object> objectCache;

	public OutputSerializationStream(Output out) {
		this.out = out;
	}

	public OutputSerializationStream(File file) {
		this.out = new NativeFileOutput(file);
	}

	public void writeObject(Object obj) {
		Class<?> clazz = obj.getClass();
		//format:off
		if (
				clazz == Byte.class      || clazz == Byte.TYPE      || clazz == Short.class   || clazz == Short.TYPE || 
				clazz == Integer.class   || clazz == Integer.TYPE   || clazz == Long.class    || clazz == Long.TYPE || 
				clazz == Float.class     || clazz == Float.TYPE     || clazz == Double.class  || clazz == Double.TYPE || 
				clazz == Character.class || clazz == Character.TYPE || clazz == Boolean.class || clazz == Boolean.TYPE || 
				clazz == String.class    ||clazz == List.class      || clazz == Set.class) 
		{
			throw new IllegalArgumentException("The class " + clazz +" is primitive. Cannot be written as an object! Use writeX(obj) method instead!");
		}
		//format:on

		if (obj instanceof List) {

		} else if (obj instanceof Set) {

		} else if (obj instanceof Map) {

		} else {
			writeObjectImpl(obj, clazz);
		}

	}

	private void writeObjectImpl(Object obj, Class<?> clazz) {
		IntValue<Class<?>> classEntry = classCache.get(obj);
		if (classEntry == null) {// We need to define the class and object
			out.writeByte(OpCodes.TYPE_DEF_OBJ_DEF_TYPE);
		} else {
			IntValue<Object> objEntry = objectCache.get(obj);
			if (objEntry == null) {// We need to define the object but not the type

			} else {// The object already exists so just reference it

			}
		}
	}

	@Override
	public void writeByte(byte b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeShort(short s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeInt(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeLong(long l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeUnsignedByte(byte b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeUnsignedShort(short s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeUnsignedInt(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeUnsignedLong(long l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeFloat(float f) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeDouble(double d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeChar(char c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeBoolean(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeString(String str) {
		if (str.equals(TroyConstants.HELLO_WORLD_STRING)) {
			out.writeByte(OpCodes.HELLO_WORLD_STRING_CONST);
		} else {
			int len = str.length();
			if (len == 0) {
				out.writeByte(OpCodes.EMPTY_STRING_CONST);
			} else {
				TroyCharset charset = TroyCharsets.identifyCharset(str);
				int opCode = OpCodes.STRING_TYPE_MAJOR_CODE;
				opCode |= (charset.getCharsetCode() & 0b11) << 4;
				boolean lengthFitsIntoOpCode = len < 16;
				if (lengthFitsIntoOpCode) {
					opCode |= len;
				}
				out.writeByte(opCode);
			}
		}
	}

	@Override
	public void writeBigInteger(BigInteger integer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeBigDecimal(BigDecimal decimal) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeArray(Object array) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeList(List<?> list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeSet(Set<?> set) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeMap(Map<?, ?> set) {
		// TODO Auto-generated method stub

	}

}
