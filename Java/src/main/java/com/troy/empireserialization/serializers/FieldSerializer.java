package com.troy.empireserialization.serializers;

import java.lang.reflect.Field;

import com.troy.empireserialization.ObjectIn;
import com.troy.empireserialization.ObjectOut;
import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.out.Output;
import com.troy.empireserialization.util.MiscUtil;

import sun.misc.Unsafe;

public class FieldSerializer<T> extends AbstractSerializer<T> {

	public FieldSerializer(Class<T> type) {
		super(type);
	}

	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	@Override
	public void writeFields(ObjectOut objectOut, T obj, Output out) {
		int size = data.rawFields.length;
		for (int i = 0; i < size; i++) {
			Field field = data.rawFields[i];
			Class<?> type = field.getType();
			Object fieldObject;
			try {
				if (type.isPrimitive()) {
					if (unsafe != null) {
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
					} else {
						if (type == int.class) {
							out.writeInt(field.getInt(obj));
						} else if (type == long.class) {
							out.writeLong(field.getLong(obj));
						} else if (type == double.class) {
							out.writeDouble(field.getDouble(obj));
						} else if (type == float.class) {
							out.writeFloat(field.getFloat(obj));
						} else if (type == char.class) {
							out.writeChar(field.getChar(obj));
						} else if (type == boolean.class) {
							out.writeBoolean(field.getBoolean(obj));
						} else if (type == byte.class) {
							out.writeByte(field.getByte(obj));
						} else if (type == short.class) {
							out.writeShort(field.getShort(obj));
						}
					}
				} else {
					try {
						fieldObject = field.get(obj);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException(e);
					}
					if (objectOut.checkForPrimitiveFast(fieldObject, type)) {
						continue;// We were able to write the "primitive"
					}
					if (type.isArray()) {
						Class<?> baseType = type.getComponentType();
						if (baseType.isPrimitive()) {

						} else {
							objectOut.writeArray((Object[]) fieldObject);
						}
					} else {// refrence variable

					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void readFields(ObjectIn out, T obj, Input in, Class<T> type) {

	}
}
