package com.troy.empireserialization.serializers;

import java.lang.reflect.*;
import java.util.*;

import com.troy.empireserialization.charset.EmpireCharsets;
import com.troy.empireserialization.io.out.*;
import com.troy.empireserialization.util.*;

import sun.misc.*;

public class ClassData<T> {
	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	private Class<?> type;

	private Field[] rawFields;
	private String[] fieldNames;
	private Class<?>[] fieldTypes;
	private long[] fieldOffsets;

	private byte[] typeDefinition;

	public ClassData(Class<T> type) {
		this.type = type;
		init();
	}

	private void init() {
		Class<?> superType = type.getSuperclass();
		if (type == null) {
		} else {
			Field[] fields = type.getDeclaredFields();
			int fieldsLength = fields.length;
			if (superType == Object.class) {// We are a child of the object class the only fields are in this class
				if (fieldsLength != 0) {
					this.fieldNames = new String[fieldsLength];
					this.fieldTypes = new Class[fieldsLength];
					this.fieldOffsets = new long[fieldsLength];
					this.rawFields = new Field[fieldsLength];
					for (int i = 0; i < fieldsLength; i++) {
						Field field = fields[i];
						if (isValidField(field))
							addField(field, i);
					}

				} else {
					// Nop
					// We dont need to do anything because this class has no fields
				}
			} else {// There are other classes between out super and the object class. We must
					// account for fields in superclasses
				ArrayList<Field> fieldsList = new ArrayList<Field>();
				for (int i = 0; i < fieldsLength; i++) {// Dump all into arraylist
					Field field = fields[i];
					if (isValidField(field))
						fieldsList.add(field);
				}
				while (superType != null && superType != Object.class) {
					fields = superType.getDeclaredFields();
					fieldsLength = fields.length;
					for (int i = 0; i < fieldsLength; i++) {// Dump all into arraylist
						Field field = fields[i];
						if (isValidField(field))
							fieldsList.add(field);
					}
					superType = superType.getSuperclass();
				}
				this.fieldNames = new String[fieldsList.size()];
				this.fieldTypes = new Class[fieldsList.size()];
				this.rawFields = new Field[fieldsList.size()];
				if (unsafe != null)
					this.fieldOffsets = new long[fieldsList.size()];

				int i = 0;
				for (Field field : fieldsList) {
					addField(field, i++);
				}
			}
		}
		writeTypeDefinition();
	}

	private boolean isValidField(Field field) {
		int mods = field.getModifiers();
		return !Modifier.isStatic(mods) && !Modifier.isTransient(mods);
	}

	private void writeTypeDefinition() {
		ByteArrayOutput out = new ByteArrayOutput();
		int length = rawFields.length;
		int bitFieldBytes = (length + 3) / 4;
		byte[] bitfield = ByteArrayPool.aquire(bitFieldBytes);
		for (int i = 0; i < length; i++) {
			int shift = 6 - (i % 4) * 2;
		}

		EmpireCharsets.write(type.getName(), out);
		out.writeVLEInt(length);

		// Write it
		typeDefinition = Arrays.copyOf(out.getBuffer(), out.getBufferPosition());
		out.close();
	}

	private void addField(Field field, int index) {
		fieldNames[index] = field.getName();
		fieldTypes[index] = field.getType();
		System.out.println(field);
		if (unsafe != null)
			fieldOffsets[index] = unsafe.objectFieldOffset(field);
	}

	public byte[] getTypeDefinition() {
		return typeDefinition;
	}
}
