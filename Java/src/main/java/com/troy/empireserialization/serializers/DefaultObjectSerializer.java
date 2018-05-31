package com.troy.empireserialization.serializers;

import com.troy.empireserialization.EmpireOutput;
import com.troy.empireserialization.ObjectIn;
import com.troy.empireserialization.ObjectOut;
import com.troy.empireserialization.SerializationSettings;
import com.troy.empireserialization.clazz.ClassData;
import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.out.Output;
import com.troy.empireserialization.util.ClassHelper;
import com.troy.empireserialization.util.MiscUtil;

import sun.misc.Unsafe;

public class DefaultObjectSerializer<T> extends AbstractObjectSerializer<T> {

	public DefaultObjectSerializer(Class<T> type, EmpireOutput out) {
		super(type);
	}

	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	@Override
	public void write(ObjectOut objectOut, T obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
		ClassData<T> data = ClassHelper.getClassData(type);

		int size = data.rawFields.length;
		for (int i = 0; i < size; i++) {
			Class<?> type = data.fieldTypes[i];
			if (type.isPrimitive()) {
				long offset = data.fieldOffsets[i];
				if (type == int.class) {
					out.writeInt(unsafe.getInt(obj, offset));
				} else if (type == long.class) {
					out.writeLong(unsafe.getLong(obj, offset));
				} else if (type == double.class) {
					out.writeDouble(unsafe.getDouble(obj, offset));
				} else if (type == float.class) {
					out.writeFloat(unsafe.getFloat(obj, offset));
				} else if (type == char.class) {
					out.writeChar(unsafe.getChar(obj, offset));
				} else if (type == boolean.class) {
					out.writeBoolean(unsafe.getBoolean(obj, offset));
				} else if (type == byte.class) {
					out.writeByte(unsafe.getByte(obj, offset));
				} else if (type == short.class) {
					out.writeShort(unsafe.getShort(obj, offset));
				}

			} else if (type == String.class) {
				objectOut.writeString((String) unsafe.getObject(obj, data.fieldOffsets[i]));
			} else {
				objectOut.writeObjectRecursive(unsafe.getObject(obj, data.fieldOffsets[i]), -1L);
			}
		}
	}

	@Override
	public void read(ObjectIn out, T obj, Input in, Class<T> type, long extra) {
		ClassData<T> info = ClassHelper.getClassData(type);
		for (int i = 0; i < info.rawFields.length; i++) {
			Class<?> fieldType = info.fieldTypes[i];
			if (fieldType.isPrimitive()) {
				if (type == int.class) {
					unsafe.putInt(obj, info.fieldOffsets[i], in.readInt());
				} else if (type == long.class) {
					unsafe.putLong(obj, info.fieldOffsets[i], in.readLong());
				} else if (type == double.class) {
					unsafe.putDouble(obj, info.fieldOffsets[i], in.readDouble());
				} else if (type == float.class) {
					unsafe.putFloat(obj, info.fieldOffsets[i], in.readFloat());
				} else if (type == char.class) {
					unsafe.putChar(obj, info.fieldOffsets[i], in.readChar());
				} else if (type == boolean.class) {
					unsafe.putBoolean(obj, info.fieldOffsets[i], in.readBoolean());
				} else if (type == byte.class) {
					unsafe.putByte(obj, info.fieldOffsets[i], in.readByte());
				} else if (type == short.class) {
					unsafe.putShort(obj, info.fieldOffsets[i], in.readShort());
				}
			}
			try {
				info.rawFields[i].set(obj, out.readObjectRecursive());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
