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

import com.troy.empireserialization.cache.IntValueCache;
import com.troy.empireserialization.cache.IntValue;
import com.troy.empireserialization.charset.EmpireCharset;
import com.troy.empireserialization.charset.EmpireCharsets;
import com.troy.empireserialization.io.out.NativeFileOutput;
import com.troy.empireserialization.io.out.Output;
import com.troy.empireserialization.serializers.Serializer;
import com.troy.empireserialization.serializers.Serializers;
import com.troy.empireserialization.util.ClassHelper;
import com.troy.empireserialization.util.MiscUtil;
import com.troy.empireserialization.util.ReflectionUtils;
import com.troy.empireserialization.util.StringInfo;

public class EmpireOutput implements ObjectOut {

	private Output out;
	private IntValueCache<String> stringCache = new IntValueCache<String>(100, 1.0);
	private IntValueCache<Class<?>> classCache = new IntValueCache<Class<?>>(100, 1.0);
	private IntValueCache<Object> objectCache = new IntValueCache<Object>(200, 1.0);
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
	public boolean pluginsPresent() {
		return false;
	}

	private void writeFlags() {

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
			out.writeByte(EmpireOpCodes.TYPE_DEF_OBJ_DEF_TYPE);
			writeTypeDefinition(type);
			writeObjectDefinition(obj);
		} else {// The class has been written before - we either need to define the fields, or reference the previously
				// written object
			writeObjectImplWithoutTypeDef(obj, type, classEntry);
		}
	}

	private void writeObjectImplWithoutTypeDef(Object obj, Class<?> type, IntValue<Class<?>> classEntry) {
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

	private <T> void writeObjectDefinition(T obj) {
		Serializer<T> serializer = Serializers.getSerializer((Class<T>) obj.getClass(), this);
		serializer.writeFields(this, obj, out);
	}

	private void writeTypeDefinition(Class<?> type) {
		// Assign the type an id based on its order
		int id = classCache.size() + 1;// Start with id #1
		classCache.add(type, id);
		Serializers.getSerializer(type, this).writeTypeDefinition(out);
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
		boolean sameType = true;
		Class<?> lastType = null;
		for (int i = 0; i < length; i++) {
			Object element = array[i];
			Class<?> current = element.getClass();
			if (lastType != null && current != lastType) {
				sameType = false;
				break;
			}
			lastType = current;
		}
		if (sameType) {
			IntValue<Class<?>> entry = classCache.get(lastType);
			if (ClassHelper.isPrimitive(lastType)) {
				out.writeByte(EmpireOpCodes.PRIMITIVE_ARRAY_TYPE);
			} else if (entry == null) {
				out.writeByte(EmpireOpCodes.USER_DEFINED_ARRAY_TYPE_DEF_TYPE);
			} else {
				out.writeByte(EmpireOpCodes.USER_DEFINED_ARRAY_TYPE_REF_TYPE);
			}
		} else {
			out.writeByte(EmpireOpCodes.WILD_CARD_ARRAY_TYPE);
		}
		out.writeVLEInt(length);
		if (sameType) {
			writeTypeComplete(lastType);
			if (ClassHelper.isPrimitive(lastType)) {
				for (int i = 0; i < length; i++) {
					checkForPrimitiveSlow(array[i], lastType);
				}
			} else {
				for (int i = 0; i < length; i++) {
					writeObjectImplWithoutTypeDef(array[i], lastType, classCache.get(lastType));
				}
			}
		} else {
			for (int i = 0; i < length; i++) {
				writeObject(array[i]);
			}
		}

	}

	@Override
	public void writeList(List<?> list) {
		out.writeByte(EmpireOpCodes.LIST_TYPE);
		int size = list.size();
		if (list instanceof LinkedList) {
		} else if (list instanceof ArrayList) {
			Object[] listData = ReflectionUtils.getData((ArrayList<?>) list);
			writeArray(listData);

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
	public void writeTypeComplete(Class<?> type) {
		int opcode;
		if (ClassHelper.isPrimitive(type)) {
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
			boolean idFitsInOpCode = true;
			int typeID = -1;
			if (entry == null) {
				opcode |= EmpireConstants.TYPE_DEF_TYPE;
			} else {
				opcode |= EmpireConstants.TYPE_REF_TYPE;
				typeID = entry.value;
				idFitsInOpCode = typeID < EmpireConstants.TYPE_DEF_TYPE;
				if (idFitsInOpCode) {
					opcode |= typeID;
				} else {
					opcode |= 0b000000;// Zero indicates that we will encode the Id after the opcode using VLE
				}
			}
			out.writeByte(opcode);
			if (!idFitsInOpCode) {
				out.writeVLEInt(typeID);
			}
			if (entry == null) {
				writeTypeDefinition(type);
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

	@Override
	public int getTypeID(Class<?> type) {
		IntValue<Class<?>> entry = classCache.get(type);
		if (entry == null) {
			return -1;
		} else
			return entry.value;
	}

}
