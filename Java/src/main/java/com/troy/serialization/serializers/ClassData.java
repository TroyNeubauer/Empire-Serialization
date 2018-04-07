package com.troy.serialization.serializers;

import java.lang.reflect.*;
import java.util.*;

import com.troy.serialization.io.out.*;
import com.troy.serialization.util.*;

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
					for (int i = 0; i < fieldsLength; i++)
						addField(fields[i], i);

				} else {
					// Nop
					// We dont need to do anything because this class has no fields
				}
			} else {// There are other classes between out super and the object class. We must
					// account for fields in superclasses
				ArrayList<Field> fieldsList = new ArrayList<Field>();
				for (int i = 0; i < fieldsLength; i++)// Dump all into arraylist
					fieldsList.add(fields[i]);
				while (superType != null && superType != Object.class) {
					fields = superType.getDeclaredFields();
					fieldsLength = fields.length;
					for (int i = 0; i < fieldsLength; i++)// Dump all into arraylist
						fieldsList.add(fields[i]);
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

	private void writeTypeDefinition() {
		ByteArrayOutput out = new ByteArrayOutput();
		//Write it
		typeDefinition = Arrays.copyOf(out.getBuffer(), out.getBufferPosition());
		out.close();
	}

	private void addField(Field field, int index) {
		fieldNames[index] = field.getName();
		fieldTypes[index] = field.getType();
		if (unsafe != null)
			fieldOffsets[index] = unsafe.objectFieldOffset(field);
	}

	public byte[] getTypeDefinition() {
		return typeDefinition;
	}
}
