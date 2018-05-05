package com.troy.empireserialization;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.troy.empireserialization.cache.IntCache;
import com.troy.empireserialization.cache.IntValue;
import com.troy.empireserialization.charset.*;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.serializers.Serializer;
import com.troy.empireserialization.serializers.Serializers;
import com.troy.empireserialization.util.*;

public class EmpireOutput implements ObjectOut {

	private Output out;
	private IntCache<String> stringCache = new IntCache<String>();
	private IntCache<Class<?>> classCache = new IntCache<Class<?>>();
	private IntCache<Object> objectCache = new IntCache<Object>();
	private SerializationSettings settings;

	public EmpireOutput(File file) {
		this(new NativeFileOutput(file), SerializationSettings.defaultSettings);
	}

	public EmpireOutput(File file, SerializationSettings settings) {
		this(new NativeFileOutput(file), settings);
	}

	public EmpireOutput(Output out) {
		this(out, SerializationSettings.defaultSettings);
	}

	public EmpireOutput(Output out, SerializationSettings settings) {
		this.out = out;
		setSettings(settings);
	}

	private void setSettings(SerializationSettings newSettings) {
		SerializationSettings oldSettings = this.settings;
		this.settings = newSettings;

		out.setByteOrder(settings.useLittleEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
		boolean pluginsPresent = pluginsPresent();
		if (newSettings.getFlagsData(pluginsPresent) != newSettings.getFlagsData(pluginsPresent)) {
			writeFlags();
		}
	}

	// Let subclasses override this in case they want to use plugins
	protected boolean pluginsPresent() {
		return false;
	}

	private void writeFlags() {

	}

	private boolean requireRewrite(SerializationSettings newSettings, SerializationSettings oldSettings) {
		return false;
	}

	public void writeObject(Object obj) {
		Class<?> clazz = (Class<?>) obj.getClass();
		if (checkForPrimitiveSlow(obj, clazz))
			return;
		else
			writeObjectImpl(obj, clazz);

	}

	public boolean checkForPrimitiveFast(Object obj, Class<?> clazz) {
		if (clazz == String.class) {
			writeString((String) obj);
			return true;
		} else if (List.class.isAssignableFrom(clazz)) {
			writeList((List<?>) obj);
			return true;
		} else if (Set.class.isAssignableFrom(clazz)) {
			writeSet((Set<?>) obj);
			return true;
		} else if (Map.class.isAssignableFrom(clazz)) {
			writeMap((Map<?, ?>) obj);
			return true;
		}
		return false;
	}

	public boolean checkForPrimitiveSlow(Object obj, Class<?> clazz) {
		// Ew! Wrapper classes
		if (clazz == Byte.class) {
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
		return checkForPrimitiveFast(obj, clazz);
	}

	private void writeObjectImpl(Object obj, Class<?> type) {
		IntValue<Class<?>> classEntry = classCache.get(type);
		if (classEntry == null) {// Determine if the class hasn't been written before
			// We need to define the class and object
			classCache.add(type, classCache.size());

			out.writeByte(EmpireOpCodes.TYPE_DEF_OBJ_DEF_TYPE);
			writeTypeDefinition(type);
			writeObjectDefinition(obj);
		} else {// The class has been written before - we either need to define the fields, or reference the previously
				// written object
			IntValue<Object> objEntry = objectCache.get(obj);
			if (objEntry == null) {// We need to define the object but not the type
				objectCache.add(obj, objectCache.size());

				out.writeByte(EmpireOpCodes.TYPE_REF_OBJ_DEF_TYPE);
				// Write the type's id
				out.writeVLEInt(classEntry.value);
				writeObjectDefinition(obj);
			} else {// The object already exists so just reference it
				out.writeByte(EmpireOpCodes.OBJ_REF_TYPE);
				// Write only the object's id
				out.writeVLEInt(objEntry.value);
			}
		}
	}

	private <T> void writeObjectDefinition(T obj) {
		Serializer<T> serializer = Serializers.getSerializer((Class<T>) obj.getClass());
		serializer.writeFields(this, obj, out);
	}

	private void writeTypeDefinition(Class<?> type) {
		Serializers.getSerializer(type).writeTypeDefinition(out);
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
				IntValue<String> cached = stringCache.get(str);
				if (cached != null) {
					out.writeByte(EmpireOpCodes.STRING_REF_TYPE);
					out.writeVLEInt(cached.value);
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
	}

	@Override
	public void writeBigInteger(BigInteger integer) {
		byte[] bytes = integer.toByteArray();
		out.writeVLEInt(bytes.length);
		out.writeBytes(bytes);
	}

	@Override
	public void writeBigDecimal(BigDecimal decimal) {
		writeBigInteger(ReflectionUtils.getBigInteger(decimal));
		out.writeVLEInt(decimal.scale());
	}

	@Override
	public void writeArray(Object[] array) {
		int length = array.length;
		out.writeVLEInt(length);
		for (int i = 0; i < length; i++) {
			writeObject(array[i]);
		}
	}

	@Override
	public void writeList(List<?> list) {
		int size = list.size();
		if (list instanceof LinkedList) {
			out.writeByte(EmpireOpCodes.LINKED_LIST_TYPE);
		} else if (list instanceof ArrayList) {
			out.writeByte(EmpireOpCodes.ARRAY_LIST_TYPE);
			Object[] listData = ReflectionUtils.getData((ArrayList<?>) list);
			Class<?> lastType = null;
			boolean sameType = true;
			for (int i = 0; i < size; i++) {
				Object obj = listData[i];
				Class<?> currentType = obj.getClass();
				if (lastType != null) {
					if (lastType != currentType) {
						sameType = false;
						break;
					}
				}
				lastType = currentType;
			}

			if (sameType) {
				Class<?> elementType = listData[0].getClass();
				writeTypeComplete(elementType);
				for (int i = 0; i < size; i++) {
					Object obj = listData[i];
				}
			} else {
				for (int i = 0; i < size; i++) {

				}
			}

		}
	}
	
	@Override
	public void writeSet(Set<?> set) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeMap(Map<?, ?> map) {
		// TODO Auto-generated method stub

	}

	/**
	 * Writes a full type descriptor for all types, primitive, user defined, writes a full type definition for a non
	 * primitive type if necessary
	 * 
	 * @param elementType
	 */
	private void writeTypeComplete(Class<?> type) {
		int opcode;
		//format:off
		if (type.isPrimitive() 
				|| type == Byte.class 		|| type == Short.class 
				|| type == Integer.class 	|| type == Long.class 
				|| type == Float.class 		|| type == Double.class 
				|| type == Character.class 	|| type == Boolean.class) {
			//format:on
			opcode = EmpireConstants.PRIMITIVE_TYPE;
			if (settings.useVLE) {
				opcode |= EmpireOpCodes.PRIMITIVE_TYPE_VLE_MAPPING.get(type);
			} else {
				opcode |= EmpireOpCodes.PRIMITIVE_TYPE_MAPPING.get(type);
			}
			out.writeByte(opcode);
		} else {
			opcode = EmpireConstants.USER_DEFINED_TYPE;
			IntValue<Class<?>> entry = classCache.get(type);
			if (entry == null) {

			} else {
				int typeID = entry.value;
			}
		}
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
