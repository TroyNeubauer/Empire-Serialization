package com.troy.empireserialization;

import static com.troy.empireserialization.EmpireOpCodes.*;

import java.io.File;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;

import com.troy.empireserialization.cache.IntValue;
import com.troy.empireserialization.cache.IntValueCache;
import com.troy.empireserialization.charset.EmpireCharset;
import com.troy.empireserialization.charset.EmpireCharsets;
import com.troy.empireserialization.io.out.NativeFileOutput;
import com.troy.empireserialization.io.out.Output;
import com.troy.empireserialization.serializers.Serializers;
import com.troy.empireserialization.util.ClassHelper;
import com.troy.empireserialization.util.MiscUtil;
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

	/**
	 * Sets the settings to use and writes them out if nessarry. Subclasses should
	 * override this method if plugins are present or for any other reason in which
	 * certain settings must be specific values.
	 * 
	 * @param newSettings
	 */
	protected void setSettings(SerializationSettings newSettings) {
		SerializationSettings oldSettings = this.settings;
		this.settings = newSettings;
		if ((oldSettings == null && newSettings.getFlagsData() != 0)
				|| (oldSettings != null && (newSettings.getFlagsData() != oldSettings.getFlagsData()))) {
			out.setByteOrder(settings.useLittleEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
			writeFlags();
		}
	}

	private void writeFlags() {
		out.writeByte(EmpireOpCodes.FLAGS_TYPE);
		out.writeByte(settings.getFlagsData());
	}

	public <T> void writeObjectImpl(T obj, boolean writeHeader, long extra) {
		if (obj == null) {
			out.writeByte(EmpireOpCodes.NULL_REF_CONST);
			return;
		}
		Class<T> type = (Class<T>) obj.getClass();
		if (ClassHelper.isPrimitive(type)) {
			writeObjectDefinition(obj, type, writeHeader, false, extra);
		} else {
			IntValue<Class<?>> classEntry = classCache.get(type);
			if (classEntry == null) {// Determine if the class hasn't been written before
				// We need to define the class and object
				out.writeByte(TYPE_DEF_OBJ_DEF_TYPE);
				writeTypeDefinition(type);
				writeObjectDefinition(obj, type, writeHeader, true, extra);
			} else {// The class has been written before - we either need to define the fields, or
					// reference the previously
					// written object
				IntValue<Object> objEntry = null;
				if (settings.useObjectCache) {
					objEntry = objectCache.get(obj);
				}
				if (objEntry == null) {// We need to define the object but not the type
					if (settings.useObjectCache)
						registerObject(obj);

					out.writeByte(TYPE_REF_OBJ_DEF_TYPE);
					// Write the type's id
					out.writeVLEInt(classEntry.value);
					writeObjectDefinition(obj, type, writeHeader, true, extra);
				} else {// The object already exists so just reference it
					out.writeByte(OBJ_REF_TYPE);
					// Write only the object's id
					out.writeVLEInt(objEntry.value);
				}
			}
		}
	}

	public <T> void writeObject(T obj) {
		writeObjectImpl(obj, true, -1L);
	}

	@Override
	public <T> void writeObjectRecursive(T obj, long extra) {
		writeObjectImpl(obj, false, extra);
	}

	private <T> void writeObjectDefinition(T obj, Class<T> type, boolean writeHeader, boolean registerObject, long extra) {
		if(registerObject) registerObject(obj);
		Serializers.getSerializer(type, this).write(this, (T) obj, out, writeHeader, settings, extra);
	}

	private void registerClass(Class<?> type) {
		if (classCache.get(type) != null)
			throw new IllegalArgumentException(type + " already exists in the class cache!");
		classCache.add(type, classCache.size());
	}

	private void registerObject(Object obj) {
		objectCache.add(obj, objectCache.size());
	}

	private void registerString(String str) {
		stringCache.add(str, stringCache.size());
	}

	public void writeTypeReference(IntValue<Class<?>> entry) {
		out.writeVLEInt(entry.value);
	}

	public void writeTypeDefinition(Class<?> type) {
		if (classCache.get(type) != null)
			throw new IllegalArgumentException();
		registerClass(type);
		Serializers.getSerializer(type, this).writeTypeDefinition(this);
	}

	public void writeString(String str) {
		if (str.equals(EmpireConstants.HELLO_WORLD_STRING)) {
			out.writeByte(HELLO_WORLD_STRING_CONST);
		} else {
			int len = str.length();
			if (len == 0) {
				out.writeByte(EMPTY_STRING_CONST);
			} else {
				IntValue<String> cached = null;
				if (settings.useStringCache)
					cached = stringCache.get(str);
				if (cached != null) {
					out.writeByte(STRING_REF_TYPE);
					out.writeVLEInt(cached.value);
				} else {
					if (settings.useStringCache)
						registerString(str);
					StringInfo info = EmpireCharsets.identifyCharset(str, 0, str.length());
					EmpireCharset charset = info.charset;
					int opCode = STRING_MAJOR_CODE;
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

	/**
	 * Writes a full type descriptor for all types, primitive, user defined, writes
	 * a full type definition for a non primitive type if necessary
	 * 
	 * @param elementType
	 */
	public void writeTypeComplete(Class<?> type) {
		int opcode;
		if (type == null) {
			throw new NullPointerException();
		} else if (ClassHelper.isPrimitive(type)) {
			opcode = EmpireConstants.PRIMITIVE_TYPE;
			if (ClassHelper.isDataStructure(type)) {
				opcode |= EmpireConstants.DATA_STRUCTURE_TYPE;
			} else {
				opcode |= EmpireConstants.GENERAL_TYPE;
			}
			if (Map.class.isAssignableFrom(type)) {// Put in the correct opcode for the primitive
				opcode |= (EmpireOpCodes.POLYMORPHIC_MAP_TYPE & EmpireOpCodes.MINIOR_CODE_MASK);
			} else if (type.isArray() || List.class.isAssignableFrom(type)) {
				opcode |= (EmpireOpCodes.POLYMORPHIC_ARRAY_TYPE & EmpireOpCodes.MINIOR_CODE_MASK);
			} else {// Non data structure primitive type
				if (settings.useVLE) {
					opcode |= (PRIMITIVE_TYPE_VLE_MAPPING.get(type) & EmpireOpCodes.MINIOR_CODE_MASK);
				} else {
					opcode |= (PRIMITIVE_TYPE_MAPPING.get(type) & EmpireOpCodes.MINIOR_CODE_MASK);
				}
			}
			out.writeByte(opcode);
		} else {
			opcode = EmpireConstants.USER_DEFINED_TYPE;
			IntValue<Class<?>> entry = classCache.get(type);
			boolean idFitsInOpCode = true;
			int typeID = -1;
			if (entry == null) {// Type def
				opcode |= EmpireConstants.TYPE_DEF_TYPE;
			} else {// Type ref
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
			if (entry == null) {// We have a type definition
				writeTypeDefinition(type);
			} else {// If we are using a type reference
				if (!idFitsInOpCode) {
					out.writeVLEInt(typeID);
				}
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

	public Output getImpl() {
		return out;
	}

	public IntValueCache<Object> getObjectCache() {
		return objectCache;
	}

	public IntValueCache<Class<?>> getClassCache() {
		return classCache;
	}

	public IntValueCache<String> getStringCache() {
		return stringCache;
	}
	
	public SerializationSettings getSettings() {
		return settings;
	}
}
