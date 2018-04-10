package com.troy.empireserialization;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.troy.empireserialization.charset.*;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.serializers.Serializer;
import com.troy.empireserialization.serializers.Serializers;
import com.troy.empireserialization.util.*;

public class EmpireOutput implements ObjectOut {

	private Output out;
	private IntCache<String> stringCache;
	private IntCache<Class<?>> classCache;
	private IntCache<Object> objectCache;

	public EmpireOutput(Output out) {
		this.out = out;
	}

	public EmpireOutput(File file) {
		this.out = new NativeFileOutput(file);
	}

	public <T> void writeObject(T obj) {
		Class<T> clazz = (Class<T>) obj.getClass();
		if (checkForPrimitive(obj, clazz))
			return;
		else
			writeObjectImpl(obj, clazz);

	}

	/**
	 * Handles cases where the user passes a "primitive" into the write object method
	 * 
	 * @return {@code true} If the type passed in was a primitive and was written using the correct writeX() method
	 *         {@code false} otherwise
	 */
	private boolean checkForPrimitive(Object obj, Class<?> clazz) {
		if (clazz == String.class) {
			writeString((String) obj);
			return true;
		} else if (clazz == List.class) {
			writeList((List<?>) obj);
			return true;
		} else if (clazz == Set.class) {
			writeSet((Set<?>) obj);
			return true;
		} else if (clazz == Map.class) {
			writeMap((Map<?, ?>) obj);
			return true;
		} else if (clazz == Byte.class) {
			writeByte(((Byte) obj).byteValue());
			return true;
		} else if (clazz == Short.class) {
			writeShort(((Short) obj).shortValue());
			return true;
		} else if (clazz == Integer.class) {
			writeInt(((Integer) obj).intValue());
			return true;
		} else if (clazz == Long.class) {
			writeLong(((Long) obj).longValue());
			return true;
		} else if (clazz == Float.class) {
			writeFloat(((Float) obj).floatValue());
			return true;
		} else if (clazz == Double.class) {
			writeDouble(((Double) obj).doubleValue());
			return true;
		} else if (clazz == Character.class) {
			writeChar(((Character) obj).charValue());
			return true;
		} else if (clazz == Boolean.class) {
			writeBoolean(((Boolean) obj).booleanValue());
			return true;
		}
		return false;
	}

	private <T> void writeObjectImpl(T obj, Class<T> type) {
		IntValue<Class<?>> classEntry = classCache.get(type);
		if (classEntry == null) {// We need to define the class and object
			classCache.add(type, classCache.size());

			out.writeByte(EmpireOpCodes.TYPE_DEF_OBJ_DEF_TYPE);
			writeTypeDefinition(type);
			writeObjectDefinition(obj, type);
		} else {
			IntValue<Object> objEntry = objectCache.get(obj);
			if (objEntry == null) {// We need to define the object but not the type
				objectCache.add(obj, objectCache.size());

				out.writeByte(EmpireOpCodes.TYPE_REF_OBJ_DEF_TYPE);
				// Write the type's id
				out.writeVLEInt(classEntry.value);
				writeObjectDefinition(obj, type);
			} else {// The object already exists so just reference it
				out.writeByte(EmpireOpCodes.OBJ_REF_TYPE);
				// Write only the object's id
				out.writeVLEInt(objEntry.value);
			}
		}
	}

	private <T> void writeObjectDefinition(T obj, Class<T> type) {
		Serializer<T> serializer = Serializers.getSerializer(type);
		serializer.writeFields(obj, out);
	}

	private void writeTypeDefinition(Class<?> type) {

	}

	@Override
	public void writeByte(byte b) {
		out.writeByte(b);
	}

	@Override
	public void writeShort(short s) {
		out.writeShort(s);
	}

	@Override
	public void writeInt(int i) {
		out.writeInt(i);
	}

	@Override
	public void writeLong(long l) {
		out.writeLong(l);
	}

	@Override
	public void writeUnsignedByte(byte b) {
		out.writeByte(b);
	}

	@Override
	public void writeUnsignedShort(short s) {
		out.writeUnsignedShort(s);
	}

	@Override
	public void writeUnsignedInt(int i) {
		out.writeInt(i);
	}

	@Override
	public void writeUnsignedLong(long l) {
		out.writeLong(l);
	}

	@Override
	public void writeFloat(float f) {
		out.writeFloat(f);
	}

	@Override
	public void writeDouble(double d) {
		out.writeDouble(d);
	}

	@Override
	public void writeChar(char c) {
		out.writeChar(c);
	}

	@Override
	public void writeBoolean(boolean b) {
		out.writeBoolean(b);
	}

	@Override
	public void writeString(String str) {
		if (str.equals(EmpireConstants.HELLO_WORLD_STRING)) {
			out.writeByte(EmpireOpCodes.HELLO_WORLD_STRING_CONST);
		} else {
			int len = str.length();
			if (len == 0) {
				out.writeByte(EmpireOpCodes.EMPTY_STRING_CONST);
			} else {
				StringInfo info = EmpireCharsets.identifyCharset(str, 0, str.length());
				EmpireCharset charset = info.charset;
				int opCode = EmpireOpCodes.STRING_TYPE_MAJOR_CODE;
				opCode |= (charset.getCharsetCode() & 0b11) << 4;
				boolean lengthFitsIntoOpCode = len < (1 << 4);
				if (lengthFitsIntoOpCode) {
					opCode |= len;
				} else {
					opCode |= 0b0000;
				}
				out.writeByte(opCode);
				if (!lengthFitsIntoOpCode) {
					out.writeVLEInt(len);
				}
				charset.encode(MiscUtil.getCharsFast(str), out, 0, len, info.info);
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
	public void writeMap(Map<?, ?> map) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		out.close();
	}

	@Override
	public void flush() {
		out.flush();
	}

}
