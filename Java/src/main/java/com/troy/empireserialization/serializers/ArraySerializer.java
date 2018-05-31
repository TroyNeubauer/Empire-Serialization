package com.troy.empireserialization.serializers;

import java.lang.reflect.Array;
import java.util.Arrays;

import com.troy.empireserialization.EmpireOpCodes;
import com.troy.empireserialization.ObjectIn;
import com.troy.empireserialization.ObjectOut;
import com.troy.empireserialization.SerializationSettings;
import com.troy.empireserialization.cache.IntValue;
import com.troy.empireserialization.io.in.Input;
import com.troy.empireserialization.io.out.Output;
import com.troy.empireserialization.util.ClassHelper;

public class ArraySerializer extends AbstractObjectSerializer<Object[]> {

	public static final ArraySerializer SERIALIZER = new ArraySerializer();

	public ArraySerializer() {
		super(Object[].class);
	}

	@Override
	public void write(ObjectOut objectOut, Object[] obj, Output out, boolean writeHeader, SerializationSettings settings, long extra) {
		if (obj == null) {
			out.writeByte(EmpireOpCodes.NULL_REF_CONST);
			return;
		}
		if (obj.length == 0) {
			out.writeByte(EmpireOpCodes.EMPTY_STRING_CONST);
			return;
		}
		int offset;
		int length;
		if (extra == -1) {
			offset = 0;
			length = obj.length;
		} else {
			offset = (int) ((extra >>> 32) & 0xFFFFFFFF);
			length = (int) ((extra >>> 0) & 0xFFFFFFFF);
		}
		boolean sameType = true, typeRef;
		Class<?> lastType = null;
		for (int i = offset; i < offset + length; i++) {
			Class<?> currentType = obj[i].getClass();
			if (lastType != null && currentType != lastType) {
				sameType = false;
				break;
			}
			lastType = currentType;
		}
		if (sameType && ClassHelper.isPrimitive(lastType)) {
			Object array = Array.newInstance(lastType, length);
			for (int i = 0; i < length; i++) {
				Array.set(array, i, obj[i]);
			}
			objectOut.writeObjectRecursive(array, extra);
			return;
		} else if (sameType) {
			IntValue<Class<?>> cached = objectOut.getClassCache().get(lastType);
			if (cached == null) {// We need to define the type
				out.writeByte(EmpireOpCodes.TYPE_DEF_OBJ_DEF_TYPE);
				objectOut.writeTypeDefinition(lastType);
			} else {// Reference the type
				out.writeByte(EmpireOpCodes.TYPE_REF_OBJ_DEF_TYPE);
				objectOut.writeTypeReference(cached);
			}
			// Write the data
			for (int i = offset; i < offset + length; i++) {
				objectOut.writeObjectRecursive(obj[i], -1L);
			}
		} else {// Polymorphic
			out.writeByte(EmpireOpCodes.POLYMORPHIC_ARRAY_TYPE);
			for (int i = offset; i < offset + length; i++) {
				Object element = obj[i];
				objectOut.writeTypeComplete(element.getClass());
				objectOut.writeObjectRecursive(element, -1L);
			}
		}
	}

	@Override
	public void read(ObjectIn objIn, Object[] obj, Input in, Class<Object[]> type, long extra) {
		// TODO
	}

}
