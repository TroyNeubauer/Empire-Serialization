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
	public void writeFields(ObjectOut objectOut, Object obj, Output out) {
		int size = data.getFields().length;
		for (int i = 0; i < size; i++) {
			Field field = data.getFields()[i];
			Class<?> type = field.getType();
			if (type.isPrimitive()) {
				if (unsafe != null) {
					long offset = MiscUtil.sizeof(type);
				} else {

				}
			} else {
				
			}
		}
	}

	@Override
	public T readFields(ObjectIn out, Object obj, Input in) {
		return null;
	}
}
